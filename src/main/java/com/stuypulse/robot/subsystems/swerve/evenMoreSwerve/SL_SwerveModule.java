/************************ PROJECT JIM *************************/
/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.subsystems.swerve.evenMoreSwerve;

import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.angle.AngleController;
import com.stuypulse.stuylib.control.angle.feedback.AnglePIDController;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.control.feedforward.MotorFeedforward;
import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.streams.angles.filters.ARateLimit;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Settings.Swerve;
import com.stuypulse.robot.constants.Settings.Swerve.Drive;
import com.stuypulse.robot.constants.Settings.Swerve.Encoder;
import com.stuypulse.robot.constants.Settings.Swerve.Turn;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;



public class SL_SwerveModule extends SwerveModule {

    // module data
    private final String id;
    private final Translation2d location;
    private SwerveModuleState targetState;

    // turn
    private final CANSparkMax turnMotor;
    private final AbsoluteEncoder absoluteEncoder;

    // drive
    private final CANSparkMax driveMotor;
    private final RelativeEncoder driveEncoder;

    private Rotation2d angleOffset;

    // controllers
    private Controller driveController;
    private AngleController turnController;

    public SL_SwerveModule(String id, Translation2d location, int turnCANId, Rotation2d angleOffset, int driveCANId) {

        // module data
        this.id = id;
        this.location = location;

        // turn
        turnMotor = new CANSparkMax(turnCANId, MotorType.kBrushless);

        // double check this
        absoluteEncoder = turnMotor.getAbsoluteEncoder();
        absoluteEncoder.setPositionConversionFactor(Encoder.Turn.POSITION_CONVERSION);
        absoluteEncoder.setVelocityConversionFactor(Encoder.Turn.VELOCITY_CONVERSION);
        absoluteEncoder.setZeroOffset(0.0);

        // TODO: add as configurable setting
        absoluteEncoder.setInverted(true);
        turnMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 20);
        Motors.disableStatusFrames(turnMotor, 3, 4);

        turnController = new AnglePIDController(Turn.kP, Turn.kI, Turn.kD)
            .setSetpointFilter(new ARateLimit(Swerve.MAX_TURNING));

        this.angleOffset = angleOffset;

        // drive
        driveMotor = new CANSparkMax(driveCANId, MotorType.kBrushless);

        driveEncoder = driveMotor.getEncoder();
        driveEncoder.setPositionConversionFactor(Encoder.Drive.POSITION_CONVERSION);
        driveEncoder.setVelocityConversionFactor(Encoder.Drive.VELOCITY_CONVERSION);
        Motors.disableStatusFrames(driveMotor, 3, 4, 5, 6);

        driveController = new PIDController(Drive.kP, Drive.kI, Drive.kD)
                .setOutputFilter(x -> (double) 0) /* old filter (x -> true ? 0 : x)*/
            .add(new MotorFeedforward(Drive.kS, Drive.kV, Drive.kA).velocity());

        targetState = new SwerveModuleState();

        Motors.Swerve.TURN.configure(turnMotor);
        Motors.Swerve.DRIVE.configure(turnMotor);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Translation2d getOffset() {
        return location;
    }

    @Override
    public SwerveModuleState getState() {
        return new SwerveModuleState(getVelocity(), getAngle());
    }

    private double getVelocity() {
        return driveEncoder.getVelocity();
    }

    private Rotation2d getAngle() {
        return Rotation2d.fromRotations(absoluteEncoder.getPosition()).minus(angleOffset);
    }

    @Override
    public void setTargetState(SwerveModuleState state) {
        targetState = SwerveModuleState.optimize(state, getAngle());
    }

    @Override
    public SwerveModulePosition getModulePosition() {
        return new SwerveModulePosition(driveEncoder.getPosition(), getAngle());
    }

    @Override
    public void periodic() {
        // turn
        turnMotor.setVoltage(turnController.update(
            Angle.fromRotation2d(targetState.angle),
            Angle.fromRotation2d(getAngle())));

        // drive
        driveMotor.setVoltage(driveController.update(
            targetState.speedMetersPerSecond,
            getVelocity()));


        SmartDashboard.putNumber("Swerve/" + id + "/Raw Angle (deg)", Units.rotationsToDegrees(absoluteEncoder.getPosition()));
        SmartDashboard.putNumber("Swerve/" + id + "/Target Angle", targetState.angle.getDegrees());
        SmartDashboard.putNumber("Swerve/" + id + "/Angle", getAngle().getDegrees());
        SmartDashboard.putNumber("Swerve/" + id + "/Angle Error", turnController.getError().toDegrees());
        SmartDashboard.putNumber("Swerve/" + id + "/Angle Voltage", turnController.getOutput());
        SmartDashboard.putNumber("Swerve/" + id + "/Angle Current", turnMotor.getOutputCurrent());
        SmartDashboard.putNumber("Swerve/" + id + "/Target Velocity", targetState.speedMetersPerSecond);
        SmartDashboard.putNumber("Swerve/" + id + "/Velocity", getVelocity());
        SmartDashboard.putNumber("Swerve/" + id + "/Velocity Error", driveController.getError());
        SmartDashboard.putNumber("Swerve/" + id + "/Velocity Voltage", driveController.getOutput());
        SmartDashboard.putNumber("Swerve/" + id + "/Velocity Current", driveMotor.getOutputCurrent());
    }
}
package com.stuypulse.robot.subsystems.odometry;

import com.stuypulse.robot.subsystems.swerve.SwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class OdometryImpl extends Odometry {

    private final SwerveDriveOdometry odometry;
    private final Field2d field;

    private final FieldObject2d odometryPose2d;

    protected OdometryImpl() {
        var swerve = SwerveDrive.getInstance();
        var startingPose = new Pose2d(8, 4, Rotation2d.fromDegrees(0));

        odometry =
            new SwerveDriveOdometry(
                swerve.getKinematics(),
                swerve.getGyroAngle(),
                swerve.getModulePositions(),
                startingPose);

        field = new Field2d();

        odometryPose2d = field.getObject("Odometry Pose2d");

        swerve.initFieldObjects(field);
        SmartDashboard.putData("Field", field);
    }

    @Override
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    @Override
    public void reset(Pose2d pose) {
        SwerveDrive drive = SwerveDrive.getInstance();

        odometry.resetPosition(
            drive.getGyroAngle(),
            drive.getModulePositions(),
            pose);
    }

    @Override
    public Field2d getField() {
        return field;
    }

    @Override
    public void periodic() {
        SwerveDrive drive = SwerveDrive.getInstance();
        odometry.update(drive.getGyroAngle(), drive.getModulePositions());

        odometryPose2d.setPose(odometry.getPoseMeters());

        SmartDashboard.putNumber("Odometry/Odometry Pose X", odometry.getPoseMeters().getX());
        SmartDashboard.putNumber("Odometry/Odometry Pose Y", odometry.getPoseMeters().getY());
        SmartDashboard.putNumber("Odometry/Odometry Rotation", odometry.getPoseMeters().getRotation().getDegrees());
    }
}
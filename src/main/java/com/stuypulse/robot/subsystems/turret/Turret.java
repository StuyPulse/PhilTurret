package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Turret.Feedback;
import com.stuypulse.robot.subsystems.swerve.SwerveDrive;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Turret extends SubsystemBase {

    public static final Turret instance;
    
    static {
        if (Robot.isReal()) {
            instance = new TurretImpl(Ports.Turret.TURN);
        }
        else {
            instance = new TurretSim();
        }
    }

    public static Turret getInstance() {
        return instance;
    }

    private final SmartNumber targetAngle;
    private final Controller controller;

    public Turret() {
        controller = new PIDController(Feedback.kP, Feedback.kI, Feedback.kD); 

        targetAngle = new SmartNumber("Turret/Target Angle", 0);
    }

    public abstract double getTurretAngle();
    public abstract void setTurretVoltage(double voltage);

    public void stop() {
        setTurretVoltage(0);
    }

    public void setTargetAngle(double angle, double minTarget, double maxTarget) {
        // keep in mind that this assumes that the minimum angle is 360, when it very
        // much could be less, in which case I'll change it in like 5 minutes lol

        // // number of rotations needed to bring angle back in (min, max) range
        double deltaRotations = 0;

        if (angle > maxTarget) {
            deltaRotations = -Math.ceil((angle - maxTarget) / 180.0); // minimum number of turns possible to reach it
        }
        else if (angle < minTarget) {
            deltaRotations = +Math.ceil(-(angle - minTarget) / 180.0);
        }

        targetAngle.set(angle + deltaRotations * 360); // 360 makes sure that right side of robot is tracking
    }
    
    @Override
    public final void periodic() {
        controller.update(
            targetAngle.get(), 
            getTurretAngle()
        );

        double output = controller.getOutput();
        setTurretVoltage(output);

        SmartDashboard.putNumber("Turret/Calculated Voltage (V)", output);
        SmartDashboard.putNumber("Turret/Angle (deg)", getTurretAngle());

        periodic2();
    }

    public abstract void periodic2();
}


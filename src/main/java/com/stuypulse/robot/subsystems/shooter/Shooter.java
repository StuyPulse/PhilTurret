package com.stuypulse.robot.subsystems.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.control.feedforward.MotorFeedforward;

// import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Shooter class contains the hardware logic for the Shooter class.
 *
 * <p>Contains a simple feedforward model of the shooter based on the voltage-balance equation and a
 * PID controller to correct for any error.
 *
 * @author Richie Xue 
 */
public abstract class Shooter extends SubsystemBase{

    private static final Shooter instance;

    static {
        if (RobotBase.isReal()) {
            instance = new ShooterImpl();
        }
        else {
            instance = new ShooterSim();
        }
    }

    public static Shooter getInstance() {
        return instance;
    }

    private final Controller controller;

    private double targetRPM;
 
    public Shooter() {
        this.targetRPM = 0.0;

        controller = new PIDController(Settings.Shooter.Feedback.kP, Settings.Shooter.Feedback.kI, Settings.Shooter.Feedback.kD)
            .add(new MotorFeedforward(Settings.Shooter.Feedforward.kS, Settings.Shooter.Feedforward.kV, Settings.Shooter.Feedforward.kA).velocity());
    }

    public void setTargetRPM(double targetRPM) {
        if (targetRPM < Settings.Shooter.MIN_RPM) {
            this.targetRPM = 0.0;
        } 
        else if (targetRPM > Settings.Shooter.MAX_RPM) {
            this.targetRPM = Settings.Shooter.MAX_RPM;
        }
        else {
            this.targetRPM = targetRPM;
        }
    }

    public double getTargetRPM() {
        return this.targetRPM;
    }

    public abstract double getVelocity();
    public abstract void setVoltage(double voltage);
    
    @Override
    public void periodic() {
        if (getTargetRPM() < Settings.Shooter.MIN_RPM) {
            this.setVoltage(0.0);
        } 
        else {
            controller.update(targetRPM, getVelocity());
            this.setVoltage(controller.getOutput());
        }
        SmartDashboard.putNumber("Shooter/Target RPM", getTargetRPM());
        periodicallyCalled();
    }

    public abstract void periodicallyCalled();
}

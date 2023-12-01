package com.stuypulse.robot.subsystems.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
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
    // Singleton (makes it so that there is only one instance of the Shooter class)
    private static final Shooter instance;

    static {
        if (RobotBase.isReal()) {
            instance = new ShooterImpl();
        }
        else {
            // it literally does nothing lol
            instance = new ShooterSim();
        }
    }

    public static Shooter getInstance() {
        return instance;
    }
    // Don't worry about anything above

    private Controller controller;
    private double targetRPM;
    private final SimpleMotorFeedforward feedforward;
    private final Controller feedback;

 
    public Shooter() {

        controller = new PIDController(1, 0, 0);

        this.targetRPM = 0.0;

        this.feedforward = Settings.Shooter.ShooterFF.getController();
        this.feedback = Settings.Shooter.ShooterPID.getController();
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
    public abstract double getVoltage();
    
    @Override
    public void periodic() {
        if (getTargetRPM() < Settings.Shooter.MIN_RPM) {
            this.setVoltage(0.0);
        } 
        else {
            // Calculate feedforward and feedback (inputting a desired RPM and outputting needed voltage)
            double ff = feedforward.calculate(getTargetRPM());
            double fb = feedback.update(getTargetRPM(), getVelocity());
            this.setVoltage(ff + fb);
        }
        SmartDashboard.putNumber("Shooter/Target RPM", getTargetRPM());
        periodicallyCalled();
    }

    public abstract void periodicallyCalled();
}

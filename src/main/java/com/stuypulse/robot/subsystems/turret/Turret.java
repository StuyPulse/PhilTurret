package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Turret.Feedback;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.control.feedforward.MotorFeedforward;
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
        controller = new MotorFeedforward(Settings.Turret.Feedforward.kS, Settings.Turret.Feedforward.kV, Settings.Turret.Feedforward.kA).position()
        .add(new PIDController(Feedback.kP, Feedback.kI, Feedback.kD)); 

        targetAngle = new SmartNumber("Turret/Target Angle", 0);
    }

    public abstract double getTurretAngle();
    public abstract void setTurretVoltage(double voltage);

    public void stop() {
        setTurretVoltage(0);
    }

    public void setTargetAngle(double angle) {

        // keep in mind that this assumes that the minimum angle is 360, when it very
        // much could be less, in which case I'll change it in like 5 minutes lol

        if (angle < 0) {
            targetAngle.set(angle + 300);
        }
        else {
            targetAngle.set(angle % 300);
        }
    }
    
    @Override
    public final void periodic() {
        controller.update(
            targetAngle.get(), 
            getTurretAngle()
        );

        if (targetAngle.get() < 0) {
            targetAngle.set(targetAngle.get() + 300);
        }
        if (targetAngle.get() < Settings.Turret.MAX_ANGLE) {
            targetAngle.set(targetAngle.get() % 300);
        }

        double output = controller.getOutput();
        setTurretVoltage(output);

        SmartDashboard.putNumber("Turret/Calculated Voltage (V)", output);
        SmartDashboard.putNumber("Turret/Angle (deg)", getTurretAngle());

        periodic2();
    }

    public abstract void periodic2();
}


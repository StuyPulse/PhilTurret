package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Turret extends SubsystemBase {

    public static final Turret instance;
    
    static {
        if(Robot.isReal()) {
            instance = new TurretImpl(Settings.Turret.port);
        }
        else {
            instance = new TurretSim();
        }
    }

    public static Turret getInstance() {
        return instance;
    }

    public Controller controller;
    public SmartNumber targetAngle = new SmartNumber("Target Angle", 0);

    public void stop() {
        setTurretVoltage(0);
    }

    public Turret() {
        controller = new PIDController(3, 0, 0); 
    }

    public abstract double getTurretAngle();
    public abstract void setTurretVoltage(double voltage);

    public void setTurretAngle(double angle) {
        targetAngle.set(angle);
    }

    @Override
    public final void periodic() {
        controller.update(
            targetAngle.get(), 
            getTurretAngle()
        );

        double output = controller.getOutput();
        setTurretVoltage(output);
        SmartDashboard.putNumber("Calculated Voltage", output);
        SmartDashboard.putNumber("Turret Angle", getTurretAngle());

        periodic2();
    }

    public abstract void periodic2();

}


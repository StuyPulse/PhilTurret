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

    private Controller controller;
    private SmartNumber targetAngle = new SmartNumber("Target Angle", 0);
    private SmartNumber fakeTargetAngle = new SmartNumber("Fake Target Angle", 0);

    public void stop() {
        setTurretVoltage(0);
    }

    public Turret() {
        controller = new PIDController(3, 0, 0); 
    }

    public abstract double getTurretAngle();
    public abstract void setTurretVoltage(double voltage);

    public void setTargetAngle(double angle, double max, double min) {
        // closest to current angle as possible
        double currentAngle = getTurretAngle() + (angle % 360);
        
        if (currentAngle > max) {
            targetAngle.set(currentAngle - (360 * (Math.ceil(angle / 360.0))));
        } else if (currentAngle < min) {
            targetAngle.set(currentAngle + (360 * (Math.ceil(angle / 360))));
        } else {
            targetAngle.set(angle % 360);
        }
    }

    @Override
    public final void periodic() {
        controller.update(
            targetAngle.get(), 
            getTurretAngle()
        );

        setTargetAngle(fakeTargetAngle.get(), 300, -300);

        double output = controller.getOutput();
        setTurretVoltage(output);

        //SmartDashboard.putNumber("Calculated Voltage", output);
        SmartDashboard.putNumber("Calculated Voltage", 0);
        SmartDashboard.putNumber("Turret Angle", 0);
        SmartDashboard.putNumber("Target Angle", targetAngle.get());

        periodic2();
    }

    public abstract void periodic2();

}


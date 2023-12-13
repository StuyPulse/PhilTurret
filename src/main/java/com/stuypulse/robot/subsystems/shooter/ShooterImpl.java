package com.stuypulse.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.stuypulse.robot.constants.Ports;

/**
 * ShooterImpl class contains the hardware logic for the Shooter class.
 *
 * <p>Contains a simple feedforward model of the shooter based on the voltage-balance equation and a
 * PID controller to correct for any error.
 *
 * @author Richie Xue 
 */
public class ShooterImpl extends Shooter {

    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    public ShooterImpl() {
        super();

        this.motor = new CANSparkMax(Ports.Shooter.MOTOR, CANSparkMax.MotorType.kBrushless);
        this.encoder = motor.getEncoder();
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }

    @Override
    public void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }   

    @Override
    public void periodicallyCalled() {
        SmartDashboard.putNumber("Shooter/RPM", getVelocity());
        SmartDashboard.putNumber("Shooter/Error", getTargetRPM() - getVelocity());
    }   
}
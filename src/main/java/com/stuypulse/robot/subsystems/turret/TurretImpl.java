package com.stuypulse.robot.subsystems.turret;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.SparkMaxAbsoluteEncoder;

public class TurretImpl extends Turret {

    private CANSparkMax motor;
    private SparkMaxAbsoluteEncoder encoder;

    public TurretImpl(int port) {
        motor = new CANSparkMax(port, MotorType.kBrushless);
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
    }

    @Override
    public double getTurretAngle() {
        return encoder.getPosition();
    }

    @Override
    public void setTurretVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    @Override
    public void periodic2() {
        
    }
}


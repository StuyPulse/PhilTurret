package com.stuypulse.robot.subsystems.turret;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.SparkMaxAbsoluteEncoder;

public class TurretImpl extends Turret {

    private final CANSparkMax motor;
    private final SparkMaxAbsoluteEncoder encoder;

    public TurretImpl(int port) {
        motor = new CANSparkMax(port, MotorType.kBrushless);
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
    }

    @Override
    public double getTurretAngle() {
        return encoder.getPosition() / 50;
        // divide by 50 because encoder gear to turret gear conversion factor is 1:50
    }

    @Override
    public void setTurretVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    @Override
    public void periodic2() {
        
    }
}


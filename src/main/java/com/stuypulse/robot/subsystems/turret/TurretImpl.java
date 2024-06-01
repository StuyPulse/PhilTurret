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
        encoder.setPositionConversionFactor(-1); //TODO: figure out the conversion factor
    }

    @Override
    public double getTurretAngle() {
        return encoder.getPosition();
    }

    @Override
    public void setTurretVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    public boolean isStalling() {
        return motor.getOutputCurrent() > 40;
    }

    @Override
    public void periodic2() {
    }
}


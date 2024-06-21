package com.stuypulse.robot.subsystems.turret;

import com.revrobotics.CANSparkMax;

// import javax.swing.JButton;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TurretImpl extends Turret {

    private final CANSparkMax motor;
    private final AbsoluteEncoder encoder;

    public TurretImpl(int port) {
        motor = new CANSparkMax(port, MotorType.kBrushless);
        encoder = motor.getAbsoluteEncoder();
    }

    @Override
    public double getTurretAngle() {
        return encoder.getPosition() / 50;
        // divide by 50 because encoder gear to turret gear conversion factor is 1:50
    }

    // private final JButton bumpSwitch = new JButton();

    // public void hardstop(){
    //     if (bumpSwitch.isEnabled()){
    //         motor.setVoltage(0);
    //     }
    // }
    
    @Override
    public void setTurretVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    @Override
    public void periodic2() {
        
    }
}


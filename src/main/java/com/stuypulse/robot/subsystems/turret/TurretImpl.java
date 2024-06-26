package com.stuypulse.robot.subsystems.turret;

import com.revrobotics.CANSparkMax;
import com.stuypulse.robot.constants.Motors;

import edu.wpi.first.wpilibj.DigitalInput;

// import javax.swing.JButton;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TurretImpl extends Turret {

    private final CANSparkMax motor;
    private final AbsoluteEncoder encoder;

    public TurretImpl(int port) {
        motor = new CANSparkMax(port, MotorType.kBrushless);
        encoder = motor.getAbsoluteEncoder();

        Motors.Turret.TURRET_MOTOR.configure(motor);
    }

    DigitalInput bumpSwitch = new DigitalInput(0);

    public boolean bumpSwitchState() {
        return bumpSwitch.get();
    }

    public void bumpSwitchStop(){
    if (bumpSwitchState() == false){
        setTurretVoltage(0);
        }
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


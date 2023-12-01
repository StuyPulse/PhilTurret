package com.stuypulse.robot.subsystems.shooter;

public class ShooterSim extends Shooter {

    private double velocity;

    public ShooterSim() {
        super();
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public void periodicallyCalled() {
        velocity = getTargetRPM();
    }

    @Override
    public void setVoltage(double voltage) {
        return;
    }

    @Override
    public double getVoltage() {
        return 0.0;
    }
}
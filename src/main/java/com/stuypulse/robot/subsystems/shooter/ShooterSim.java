package com.stuypulse.robot.subsystems.shooter;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;

public class ShooterSim extends Shooter {

    private double velocity;
    private LinearSystemSim<N1, N1, N1> shooterSim;

    public ShooterSim() {
        shooterSim = new LinearSystemSim<N1, N1, N1>(
            LinearSystemId.identifyVelocitySystem(Settings.Shooter.Feedforward.kV, Settings.Shooter.Feedforward.kA)
        );
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
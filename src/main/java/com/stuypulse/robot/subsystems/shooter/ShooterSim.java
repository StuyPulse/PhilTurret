package com.stuypulse.robot.subsystems.shooter;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Shooter.Feedforward;

import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSim extends Shooter {

    private LinearSystemSim<N1, N1, N1> shooterSim;

    public ShooterSim() {
        shooterSim = new LinearSystemSim<N1, N1, N1>(
            LinearSystemId.identifyVelocitySystem(Feedforward.kV, Feedforward.kA)
        );
    }

    @Override
    public double getVelocity() {
        return Units.radiansPerSecondToRotationsPerMinute(shooterSim.getOutput(0));
    }

    @Override
    public void setVoltage(double voltage) {
        shooterSim.setInput(voltage);
    }

    @Override
    public void simulationPeriodic() {
        shooterSim.update(Settings.DT);
    }

    @Override
    public void periodicallyCalled() {
        SmartDashboard.putNumber("Shooter/RPM", getVelocity());
    }
}
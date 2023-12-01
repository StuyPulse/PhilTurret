package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.odometry.Odometry;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretSim extends Turret {

    private LinearSystemSim<N2, N1, N1> turretSim;
    private final FieldObject2d turretPose2d;

    public TurretSim() {
        turretSim = new LinearSystemSim<N2, N1, N1>(
            LinearSystemId.identifyPositionSystem(Settings.Turret.Feedforward.kV.get(), Settings.Turret.Feedforward.kA.get())
        );

        turretPose2d = Odometry.getInstance().getField().getObject("Turret Pose 2d");

        turretPose2d.setPose(new Pose2d(4, 4, Rotation2d.fromDegrees(0)));
        SmartDashboard.putData(Odometry.getInstance().getField());
    }

    @Override
    public double getTurretAngle() {
        return turretSim.getOutput(0);
    }

    @Override
    public void setTurretVoltage(double voltage) {
        turretSim.setInput(MathUtil.clamp(voltage, -12, 12));
    }

    @Override
    public void periodic2() {
        turretSim.update(Settings.DT);
        turretPose2d.setPose(new Pose2d(
            Odometry.getInstance().getTranslation(),
            Rotation2d.fromDegrees(getTurretAngle())
        ));
    }
}

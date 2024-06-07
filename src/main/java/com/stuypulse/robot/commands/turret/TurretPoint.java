package com.stuypulse.robot.commands.turret;

import com.stuypulse.robot.subsystems.odometry.Odometry;
import com.stuypulse.robot.subsystems.turret.Turret;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurretPoint extends CommandBase {

    private final Turret turret;
    private final Odometry odometry;

    private Translation2d target;

    private FieldObject2d targetPose;

    public TurretPoint(Translation2d target) {
        this.target = target;

        turret = Turret.getInstance();
        odometry = Odometry.getInstance();

        targetPose = Odometry.getInstance().getField().getObject("Turret Target Pose");
        targetPose.setPose(new Pose2d(target, new Rotation2d()));

        addRequirements(turret);
    }

    @Override
    public final void execute() {
        Pose2d robotPose = odometry.getPose();

        turret.setTargetAngle(
            Math.toDegrees(Math.atan2(target.getY() - robotPose.getY() , target.getX() - robotPose.getX()))
        );
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

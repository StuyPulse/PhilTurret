package com.stuypulse.robot.commands;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.subsystems.odometry.Odometry;
import com.stuypulse.robot.subsystems.turret.Turret;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurretPoint extends CommandBase {

    private final Turret turret;
    private final Odometry odometry;

    private Translation2d target;

    public TurretPoint(Translation2d target) {
        this.target = target;

        turret = Turret.getInstance();
        odometry = Odometry.getInstance();

        addRequirements(turret);
    }

    @Override
    public final void execute() {
        Pose2d robotPose = odometry.getPose();

        if ((Math.abs(robotPose.getX()) < .001) && (Math.abs(target.getX()) < .001)) {
            turret.setTargetAngle(0, 360, -360);
        }

        else {turret.setTargetAngle(
            (180 + Math.toDegrees(
                Math.atan((robotPose.getY() - target.getY()) / (robotPose.getX() - target.getX()))
            )), 180, -180
            );
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

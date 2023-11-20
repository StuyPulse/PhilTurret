package com.stuypulse.robot.commands;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Settings.Turret;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurretPoint extends CommandBase {

    private FieldObject2d robot;
    private double targetAngle;
    
    private double newTargetAngle() {
        return targetAngle - robot.getPose();
    }

    private void trackWhenMoving() {
        Turret.setTargetAngle(newTargetAngle());
    }

}

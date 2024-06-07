package com.stuypulse.robot;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Robot extends TimedRobot {
    
    public static final RobotType ROBOT;

    static {
        if (Robot.isSimulation())
            ROBOT = RobotType.SIM;
        else
            ROBOT = RobotType.fromString(System.getenv("serialnum"));
    }

    public static boolean isBlue() {
        return DriverStation.getAlliance().isPresent()
                && DriverStation.getAlliance().get() == DriverStation.Alliance.Blue;
    }

   }
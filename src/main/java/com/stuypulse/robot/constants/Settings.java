/************************ PROJECT PHIL ************************/
/* Copyright (c) 2023 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.pathplanner.lib.auto.PIDConstants;
import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public interface Settings {

    double DT = 0.02;

    double WIDTH = Units.inchesToMeters(32);
    double LENGTH = Units.inchesToMeters(36);

    public interface Turret {
        int MIN_ANGLE = 0;
        int MAX_ANGLE = +360;

        public interface Feedback {
            SmartNumber kP = new SmartNumber("Turret/kP", 3.0);
            SmartNumber kI = new SmartNumber("Turret/kI", 0.0);
            SmartNumber kD = new SmartNumber("Turret/kD", 0.0);
        }

        public interface Feedforward {
            SmartNumber kS = new SmartNumber("Turret/kS", 0.0);
            SmartNumber kV = new SmartNumber("Turret/kV", 0.1);
            SmartNumber kA = new SmartNumber("Turret/kA", 0.01);
        }
    }

    public interface Shooter {    
        static double MAX_RPM = 5700.0;
        static double MIN_RPM = 100.0;
        static double MAX_RPM_CHANGE = 2000.0;
        static double MAX_RPM_ERROR = 100.0;

        public interface Feedback {
            double kP = 0.005;
            double kI = 0.0;
            double kD = 0.00033;
        }
    
        public interface Feedforward {
            double kS = 0.17118;
            double kV = 0.0020763;
            double kA = 0.00011861;
        }
    }

    public interface Swerve {
        double WIDTH = Units.inchesToMeters(26.504);
        double LENGTH = Units.inchesToMeters(20.508);

        SmartNumber MAX_MODULE_SPEED = new SmartNumber("Swerve/Max Module Speed (meter per s)", 5.0);
        SmartNumber MAX_TURNING = new SmartNumber("Swerve/Max Turn Velocity (rad per s)", 6.28);

        SmartNumber MODULE_VELOCITY_DEADBAND = new SmartNumber("Swerve/Module Velocity Deadband (m per s)", 0.02);

        public interface Motion {
            PIDConstants XY = new PIDConstants(0.7, 0, 0.02);
            PIDConstants THETA = new PIDConstants(10, 0, 0.1);
        }

        public interface Turn {
            SmartNumber kP = new SmartNumber("Swerve/Turn/kP", 3.5);
            double kI = 0.0;
            SmartNumber kD = new SmartNumber("Swerve/Turn/kD", 0.1);

            SmartNumber kV = new SmartNumber("Swerve/Turn/kV", 0.25);
            SmartNumber kA = new SmartNumber("Swerve/Turn/kA", 0.007);
        }

        public interface Drive {
            double kP = 0.8;
            double kI = 0.0;
            double kD = 0.0;

            double kS = 0.22304;
            double kV = 2.4899;
            double kA = 0.41763;
        }

        public interface FrontRight {
            String ID = "Front Right";
            Rotation2d ABSOLUTE_OFFSET = Rotation2d.fromDegrees(122.949092) // recalibrated 11/11        
                .plus(Rotation2d.fromDegrees(0));
            Translation2d MODULE_OFFSET = new Translation2d(WIDTH * +0.5, LENGTH * -0.5);
        }

        public interface FrontLeft {
            String ID = "Front Left";
            Rotation2d ABSOLUTE_OFFSET = Rotation2d.fromDegrees(249.731491) // recalibrated 3/24
                .plus(Rotation2d.fromDegrees(270));
            Translation2d MODULE_OFFSET = new Translation2d(WIDTH * +0.5, LENGTH * +0.5);
        }

        public interface BackLeft {
            String ID = "Back Left";
            Rotation2d ABSOLUTE_OFFSET = Rotation2d.fromDegrees(125.371964) // recalibrated 4/6
                .plus(Rotation2d.fromDegrees(180));
            Translation2d MODULE_OFFSET = new Translation2d(WIDTH * -0.5, LENGTH * +0.5);
        }

        public interface BackRight {
            String ID = "Back Right";
            Rotation2d ABSOLUTE_OFFSET = Rotation2d.fromDegrees(117.311604) // recalibrated 10/1
                .plus(Rotation2d.fromDegrees(90));
            Translation2d MODULE_OFFSET = new Translation2d(WIDTH * -0.5, LENGTH * -0.5);
        }

        public interface Encoder {
            public interface Drive {
                double WHEEL_DIAMETER = Units.inchesToMeters(3);
                double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
                double GEAR_RATIO = 1.0 / 4.71;

                double POSITION_CONVERSION = WHEEL_CIRCUMFERENCE * GEAR_RATIO;
                double VELOCITY_CONVERSION = POSITION_CONVERSION / 60.0;
            }

            public interface Turn {
                double POSITION_CONVERSION = 1;
                double VELOCITY_CONVERSION = POSITION_CONVERSION / 60.0;

                double MIN_PID_INPUT = 0;
                double MAX_PID_INPUT = POSITION_CONVERSION;
            }
        }
    }

    public interface Operator {
        SmartNumber DEADBAND = new SmartNumber("Operator Settings/Deadband", 0.2);

        SmartNumber WRIST_TELEOP_SPEED = new SmartNumber("Operator Settings/Wrist Adjust Speed", 120); // deg per second
        SmartNumber SHOULDER_TELEOP_SPEED = new SmartNumber("Operator Settings/Shoulder Adjust Speed", 120); // deg per second

        SmartNumber VOLTAGE_DEADBAND = new SmartNumber("Operator Settings/Voltage Deadband", 0.05);
        SmartNumber SHOULDER_DRIVE_VOLTAGE = new SmartNumber("Operator Settings/Shoulder Drive Voltage", 9.0);
        SmartNumber WRIST_DRIVE_VOLTAGE = new SmartNumber("Operator Settings/Wrist Drive Voltage", 9.0);
    }

    public interface Driver {
        public interface Drive {
            SmartNumber DEADBAND = new SmartNumber("Driver Settings/Drive/Deadband", 0.03);

            SmartNumber RC = new SmartNumber("Driver Settings/Drive/RC", 0.2);
            SmartNumber POWER = new SmartNumber("Driver Settings/Drive/Power", 1.5);

            SmartNumber MAX_TELEOP_SPEED = new SmartNumber("Driver Settings/Drive/Max Speed", Swerve.MAX_MODULE_SPEED.get());
            SmartNumber MAX_TELEOP_ACCEL = new SmartNumber("Driver Settings/Drive/Max Accleration", 15);
        }

        public interface Turn {
            SmartNumber DEADBAND = new SmartNumber("Driver Settings/Turn/Deadband", 0.05);

            SmartNumber RC = new SmartNumber("Driver Settings/Turn/RC", 0.15);
            SmartNumber POWER = new SmartNumber("Driver Settings/Turn/Power", 2);

            SmartNumber MAX_TELEOP_TURNING = new SmartNumber("Driver Settings/Turn/Max Turning", 6.0);

            public interface GyroFeedback {
                SmartBoolean GYRO_FEEDBACK_ENABLED = new SmartBoolean("Driver Settings/Gyro Feedback/Enabled", true);

                SmartNumber P = new SmartNumber("Driver Settings/Gyro Feedback/kP", 0.5);
                SmartNumber I = new SmartNumber("Driver Settings/Gyro Feedback/kI", 0.0);
                SmartNumber D = new SmartNumber("Driver Settings/Gyro Feedback/kD", 0.1);
            }
        }

    }

    public static Vector2D vpow(Vector2D vec, double power) {
        return vec.mul(Math.pow(vec.magnitude(), power - 1));
    }
    
}

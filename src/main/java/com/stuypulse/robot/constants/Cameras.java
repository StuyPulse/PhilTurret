package com.stuypulse.robot.constants;

import com.stuypulse.robot.Robot;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

/** This interface stores information about each camera. */
public interface Cameras {

    public interface Limelight {

        Pose3d PHIL_POSE = new Pose3d(
                new Translation3d(Units.inchesToMeters(16.156338), 0, Units.inchesToMeters(13.833919)),
                new Rotation3d(0, Units.degreesToRadians(15), 0));

        String[] LIMELIGHTS = { "limelight" };

        int[] PORTS = {5800, 5801, 5802, 5803, 5804, 5805};

        Pose3d[] POSITIONS = switch (Robot.ROBOT) {
            default -> new Pose3d[] {PHIL_POSE};
        };
    }

    public CameraConfig[] APRILTAG_CAMERAS = switch (Robot.ROBOT) {
       
        default ->
            new CameraConfig[] {
                new CameraConfig("samera0", new Pose3d(
                    new Translation3d(),
                    new Rotation3d()),
                    "100",
                    3000)
            };

    };

    /*** LINEAR REGRESSION ***/

    // XY Standard Deviation vs Distance
    Translation2d[] xyStdDevs = new Translation2d[] {
        new Translation2d(0.5, 0.001368361309),
        new Translation2d(1, 0.001890508681),
        new Translation2d(1.5, 0.003221746028),
        new Translation2d(2, 0.009352868105),
        new Translation2d(2.5, 0.009364899366),
        new Translation2d(3, 0.01467209516),
        new Translation2d(3.5, 0.01837679393),
        new Translation2d(4, 0.03000858409),
        new Translation2d(4.5, 0.03192817984)
    };

    // Theta Standard Deviation vs Distance
    Translation2d[] thetaStdDevs = new Translation2d[] {
        new Translation2d(0.5, 0.2641393115),
        new Translation2d(1, 0.4433426481),
        new Translation2d(1.5, 0.660331025),
        new Translation2d(2, 0.6924061873),
        new Translation2d(2.5, 4.624662415),
        new Translation2d(3, 8.000007273),
        new Translation2d(3.5, 6.39384055),
        new Translation2d(4, 9.670544639),
        new Translation2d(4.5, 7.576406229)
    };

    public static class CameraConfig {
        private String name;
        private Pose3d location;
        private String ip;
        private int forwardedPort;

        public CameraConfig(String name, Pose3d location, String ip, int port) {
            this.name = name;
            this.location = location;
            this.ip = ip;
            this.forwardedPort = port;
        }

        public String getName() {
            return name;
        }

        public Pose3d getLocation() {
            return location;
        }

        public String getIP() {
            return ip;
        }

        public int getForwardedPort() {
            return forwardedPort;
        }
    }
}
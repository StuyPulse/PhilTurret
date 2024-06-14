package com.stuypulse.robot.util.vision;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.constants.Settings.RobotType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    
    public static final RobotType ROBOT;

    static {
        if (Robot.isSimulation())
            ROBOT = RobotType.SIM;
        else
            ROBOT = RobotType.fromString(System.getenv("serialnum"));
    }

    private RobotContainer robot;
    private CommandScheduler scheduler;
    private Command auto;

    /************************/
    /*** ROBOT SCHEDULING ***/
    /************************/

    @Override
    public void robotInit() {

        robot = new RobotContainer();

        SmartDashboard.putString("Robot State", "DISABLED");
        SmartDashboard.putString("Robot", ROBOT.name());

        SmartDashboard.putData(CommandScheduler.getInstance());
    }

    @Override
    public void robotPeriodic() {
        scheduler.run();

        SmartDashboard.putBoolean("Is DS Attached", DriverStation.isDSAttached());

        SmartDashboard.putNumber("PDP/Total Power (watts)", robot.pdp.getTotalPower());
        SmartDashboard.putNumber("PDP/Total Current (amps)", robot.pdp.getTotalCurrent());
        SmartDashboard.putNumber("PDP/Battery Voltage (volts)", robot.pdp.getVoltage());
        SmartDashboard.putNumber("PDP/Calculated Resistance (ohms)", robot.pdp.getVoltage() / robot.pdp.getTotalCurrent());
        
        for (int i = 0; i < robot.pdp.getNumChannels(); i++) {
            SmartDashboard.putNumber("PDP/Currents/" + i, robot.pdp.getCurrent(i));
        }
    }

    public static boolean isBlue() {
        return DriverStation.getAlliance().isPresent()
                && DriverStation.getAlliance().get() == DriverStation.Alliance.Blue;
    }

    // @Override
    // public void disabledPeriodic() {
    //     if (robot.amper.hasNote()) {
    //         DriverStation.reportWarning("Amper IR sensor reporting note while disabled!", false);
    //     }

    //     // if (Settings.LED.LED_AUTON_TOGGLE.get()) {
    //     //     scheduler.schedule(new LEDSet(new LEDAlign()));
    //     // } else {
    //     //     scheduler.schedule(new LEDSet(new LEDAutonChooser()));
    //     // }

    //     // reload whitelist in case of alliance change
    //     scheduler.schedule(new VisionReloadWhiteList());
    // }

    /***********************/
    /*** AUTONOMOUS MODE ***/
    /***********************/

    @Override
    public void autonomousInit() {
        auto = robot.getAutonomousCommand();

        if (auto != null) {
            auto.schedule();
        }

        // robot.climber.stop();
        // robot.amper.setTargetHeight(Lift.MIN_HEIGHT);
        // scheduler.schedule(new LEDReset());

        // robot.intake.setIdleMode(IdleMode.kBrake);
        // robot.conveyor.setIdleMode(IdleMode.kBrake);
        // robot.amper.setLiftIdleMode(IdleMode.kBrake);

        SmartDashboard.putString("Robot State", "AUTON");
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    /*******************/
    /*** TELEOP MODE ***/
    /*******************/

    // @Override
    // public void teleopInit() {
    //     if (auto != null) {
    //         auto.cancel();
    //     }

    //     robot.climber.stop();
    //     robot.amper.setTargetHeight(Lift.MIN_HEIGHT);
    //     scheduler.schedule(new LEDReset());
    //     scheduler.schedule(new ShooterStop()
    //         .andThen(new WaitCommand(Settings.Shooter.TELEOP_SHOOTER_STARTUP_DELAY))
    //         .andThen(new ShooterPodiumShot()));

    //     robot.intake.setIdleMode(IdleMode.kBrake);
    //     robot.conveyor.setIdleMode(IdleMode.kBrake);
    //     robot.amper.setLiftIdleMode(IdleMode.kBrake);

    //     SmartDashboard.putString("Robot State", "TELEOP");
    // }

    @Override
    public void teleopPeriodic() {
        // if (Timer.getMatchTime() < Score.SCORE_TIME) {
        //     robot.amper.score();
        // }
    }

    @Override
    public void teleopExit() {}

    /*****************/
    /*** TEST MODE ***/
    /*****************/

    // @Override
    // public void testInit() {
    //     CommandScheduler.getInstance().cancelAll();

    //     scheduler.schedule(new LEDReset());

    //     robot.intake.setIdleMode(IdleMode.kBrake);
    //     robot.conveyor.setIdleMode(IdleMode.kBrake);
    //     robot.amper.setLiftIdleMode(IdleMode.kBrake);

    //     SmartDashboard.putString("Robot State", "TEST");
    // }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}
}
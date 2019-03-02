/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.climb.ClimbSubsystem;
import frc.robot.drive.DriveSubsystem;
import frc.robot.drive.RobotStateUpdater;
import frc.robot.input.HumanInputManipulatorXbox;
import frc.robot.intake.IntakeSubsystem;
import frc.robot.manipulator.ManipulatorSubsystem;
import frc.robot.test.PreLaunchSequence;
import frc.lib.motion.Pose2d;
import frc.lib.motion.RobotStateEstimator;
import frc.lib.vision.VisionInput;
import frc.robot.arm.ArmSubsystem;
import frc.robot.auto.VisionAlignmentCommand;
import frc.robot.cameras.Cameras;

public class Robot extends TimedRobot {
	public static HumanInputManipulatorXbox input;
    public static DriveSubsystem driveSys;
    public static IntakeSubsystem intakeSys;
    public static ArmSubsystem armSys;
    public static ClimbSubsystem climbSys;
    public static ManipulatorSubsystem manipSys;
    public static PowerDistributionPanel pdp;
    public static VisionInput visionInput;
    public static RobotStateEstimator robotState;
    private static Cameras cameras;

    // Use this function for all initialization code
    @Override
    public void robotInit() {
        input = new HumanInputManipulatorXbox();
        climbSys = new ClimbSubsystem();
        intakeSys = new IntakeSubsystem();
        driveSys = new DriveSubsystem();
        armSys = new ArmSubsystem();
        manipSys = new ManipulatorSubsystem();

        visionInput = new VisionInput();
        cameras = new Cameras();
        robotState = new RobotStateEstimator(Specs.DRIVE_SPECS, new Pose2d());
        //pdp = new PowerDistributionPanel();

        input.registerButtons();
        cameras.start();
    }

    // Called periodically regardless of the game period
    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
    }

    /* Sandstorm Period */
    // Called at the beginning of the Sandstorm
    @Override
    public void autonomousInit() {
        Scheduler.getInstance().add(new RobotStateUpdater());
        Scheduler.getInstance().add(new VisionAlignmentCommand());
    }

    // Called periodically during the Sandstorm
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

    }

    /* Teleop Period */
    // Called at the beginning of the Teleop period
    @Override
    public void teleopInit() {
        Scheduler.getInstance().removeAll();
    }

    // Called periodically during the Teleop period
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        System.out.println("ARM POT: "+armSys.getArmPotentiometer());
        System.out.println("WST POT: "+armSys.getWristPotentiometer());
        System.out.println();
    }

    @Override
    public void testInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(new PreLaunchSequence());

    }

    @Override
    public void testPeriodic() {

    }
}

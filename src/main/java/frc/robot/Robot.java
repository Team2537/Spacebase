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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.climb.ClimbSubsystem;
import frc.robot.drive.DriveSubsystem;
import frc.robot.input.HumanInputManipulatorXbox;
import frc.robot.intake.IntakeSubsystem;
import frc.robot.manipulator.ManipulatorSubsystem;
// import frc.robot.test.PreLaunchSequence;
import frc.lib.motion.RobotStateEstimator;
import frc.lib.vision.VisionInput;
import frc.robot.arm.ArmSubsystem;
import frc.robot.cameras.Cameras;
import frc.robot.CustomDashboardCommand;

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

        //visionInput = new VisionInput();
        cameras = new Cameras();

        //robotState = new RobotStateEstimator(Specs.DRIVE_SPECS, new Pose2d());
        //pdp = new PowerDistributionPanel();

        input.registerButtons();
    }

    // Called periodically regardless of the game period
    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("ARM POT", armSys.getArmPotentiometer());
        SmartDashboard.putNumber("WRIST POT", armSys.getWristPotentiometer());
        SmartDashboard.putNumber("ULTRASONIC", Robot.driveSys.getUltrasonic());
        //SmartDashboard.putNumber("VISION TARGET", Target.getMidpoint(Robot.visionInput.getVisionPacket()).x);
        
    }

    /* Sandstorm Period */
    // Called at the beginning of the Sandstorma
    @Override
    public void autonomousInit() {
        //Scheduler.getInstance().add(new RobotStateUpdater());
        // Scheduler.getInstance().add(new VisionAlignmentCommand());
        Scheduler.getInstance().add(new CustomDashboardCommand());
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
    }

    @Override
    public void testInit() {
        Scheduler.getInstance().removeAll();
        // Scheduler.getInstance().add(new PreLaunchSequence());

    }

    @Override
    public void testPeriodic() {

    }
}

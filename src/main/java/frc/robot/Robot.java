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
import frc.robot.input.HumanInput;
import frc.robot.intake.IntakeSubsystem;
import frc.lib.motion.RobotStateEstimator;
import frc.lib.util.Vec2;
import frc.lib.vision.VisionInput;
import frc.robot.arm.ArmSubsystem;
import frc.robot.cameras.Cameras;

public class Robot extends TimedRobot {
    public static HumanInput input;
    public static DriveSubsystem driveSys;
    public static IntakeSubsystem intakeSys;
    public static ArmSubsystem armSys;
    public static ClimbSubsystem climbSys;
    public static PowerDistributionPanel pdp;
    public static VisionInput visionInput;
    public static RobotStateEstimator robotState;
    private static Cameras cameras;

    // Use this function for all initialization code
    @Override
    public void robotInit() {
        input = new HumanInput();

        climbSys = new ClimbSubsystem();
        intakeSys = new IntakeSubsystem();
        driveSys = new DriveSubsystem();
        pdp = new PowerDistributionPanel();
        armSys = new ArmSubsystem();
        visionInput = new VisionInput();
        cameras = new Cameras();
        robotState = new RobotStateEstimator(Specs.CONSTRAINTS, new Vec2(0, 0), 0);

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
        Robot.driveSys.resetGyro();
        Scheduler.getInstance().add(new RobotStateUpdater());

        // Scheduler.getInstance().add(new VisionAlignmentCommand());
    }

    // Called periodically during the Sandstorm
    @Override
    public void autonomousPeriodic() {
    }

    /* Teleop Period */
    // Called at the beginning of the Teleop period
    @Override
    public void teleopInit() {
    }

    // Called periodically during the Teleop period
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }
}

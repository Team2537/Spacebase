/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.lib.motion.RobotStateEstimator;
import frc.lib.util.Vec2;
import frc.lib.vision.VisionInput;
import frc.robot.auto.VisionAlignmentCommand;
import frc.robot.drive.DriveSubsystem;
import frc.robot.drive.RobotStateUpdater;

public class Robot extends TimedRobot {
    public Robot(){
        super(Specs.ROBOT_PERIOD_SECONDS);
    }

    public static DriveSubsystem driveSys;
    public static VisionInput visionInput;
    public static RobotStateEstimator robotState;

    // Use this function for all initialization code
    @Override
    public void robotInit() {
        driveSys = new DriveSubsystem();
        visionInput = new VisionInput();
        
        robotState = new RobotStateEstimator(Specs.CONSTRAINTS, new Vec2(0,0), 0);
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
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(new VisionAlignmentCommand());
        //Robot.driveSys.resetGyro();
        //Scheduler.getInstance().add(new RobotStateUpdater());
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
        Robot.driveSys.setMotors(.3, .3);
    }

    // Called periodically during the Teleop period
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

        // driveSys.getIR_frontUpper();
        // driveSys.getIR_frontLower();
        // driveSys.getIR_center();

    }
}

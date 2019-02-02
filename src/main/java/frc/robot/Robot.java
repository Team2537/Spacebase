/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.lib.vision.VisionInput;
import frc.robot.auto.DriveTurnCommand;
import frc.robot.drive.DriveSubsystem;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSys;
  public static DriveTurnCommand DriveTurn;
  double leftEncoderStartValue, rightEncoderStartValue;

  // Use this function for all initialization code
  @Override
  public void robotInit() {
    driveSys = new DriveSubsystem();
    driveSys.initDefaultCommand();
    VisionInput.initialize();

    DriveTurn = new DriveTurnCommand();
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
    //COMMENTED FOR PEANUT TESTING
    //leftEncoderStartValue = Robot.driveSys.getEncoderPosLeft();
    //rightEncoderStartValue = Robot.driveSys.getEncoderPosRight();
    Scheduler.getInstance().add(new DriveTurnCommand());

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

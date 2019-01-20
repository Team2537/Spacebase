/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.drive.DriveSubsystem;

public class Robot extends TimedRobot {
  public static DriveSubsystem drivesys;
  double leftEncoderStartValue, rightEncoderStartValue;


  // Use this function for all initialization code
  @Override
  public void robotInit() {
    drivesys = new DriveSubsystem();
    drivesys.initDefaultCommand();
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
    leftEncoderStartValue = Robot.drivesys.getEncoderPosLeft();
    rightEncoderStartValue = Robot.drivesys.getEncoderPosRight();
  }

  // Called periodically during the Sandstorm
  @Override
  public void autonomousPeriodic() {
    double leftSpeed, rightSpeed;
    if((Robot.drivesys.getEncoderPosLeft() - leftEncoderStartValue) <= 50){
      leftSpeed = 0.5;
    } else {
      leftSpeed = 0;
    }

    if((Robot.drivesys.getEncoderPosRight() - rightEncoderStartValue) <= 50){
      rightSpeed = 0.5;
    } else {
      rightSpeed = 0;
    }

    Robot.drivesys.setMotors(leftSpeed,rightSpeed);

    System.out.println(Robot.drivesys.encoderStatus());
  }


  /* Teleop Period */
  // Called at the beginning of the Teleop period
  @Override
  public void teleopInit() {
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

  }
}

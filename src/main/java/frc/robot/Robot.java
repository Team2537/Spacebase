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
import frc.robot.auto.AlignToHatchCommand;
import frc.robot.drive.DriveSubsystem;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSys;
  public static VisionInput visionInput;

  // Use this function for all initialization code
  @Override
  public void robotInit() {
    driveSys = new DriveSubsystem();
    
    VisionInput.initialize();
    visionInput = VisionInput.getInstance();
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
    Scheduler.getInstance().add(new AlignToHatchCommand());
    
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
    Robot.driveSys.resetGyro();
  }
  
  // Called periodically during the Teleop period
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    System.out.println(Robot.driveSys.getGyroDegrees());
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

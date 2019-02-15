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
import frc.lib.units.Units;
import frc.robot.climb.ClimbSubsystem;
import frc.robot.drive.DriveSubsystem;
import frc.robot.intake.IntakeSubsystem;
import frc.robot.arm.ArmSubsystem;

  
public class Robot extends TimedRobot {
  public static DriveSubsystem drivesys;
  public static IntakeSubsystem intakeSys;
  public static ArmSubsystem armsys;
  double leftEncoderStartValue, rightEncoderStartValue;
  public static ClimbSubsystem climbsys;
  public static PowerDistributionPanel pdp;


  // Use this function for all initialization code
  @Override
  public void robotInit() {

    climbsys = new ClimbSubsystem();
    intakeSys = new IntakeSubsystem();
    drivesys = new DriveSubsystem();
    drivesys.initDefaultCommand();

    pdp = new PowerDistributionPanel();
    armsys = new ArmSubsystem();
    /*armSys.initDefaultCommand();
    armSys.registerButtons();*/
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

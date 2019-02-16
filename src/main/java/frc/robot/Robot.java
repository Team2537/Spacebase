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
import frc.robot.intake.IntakeSubsystem;
import frc.robot.arm.ArmSubsystem;
import frc.robot.cameras.Cameras;

public class Robot extends TimedRobot {
<<<<<<< HEAD
  public static DriveSubsystem drivesys;
  public static IntakeSubsystem intakesys;
  public static ArmSubsystem armsys;
  double leftEncoderStartValue, rightEncoderStartValue;
  public static Cameras cameras;
  public static ClimbSubsystem climbsys;
  public static PowerDistributionPanel pdp;


  // Use this function for all initialization code
  @Override
  public void robotInit() {

    climbsys = new ClimbSubsystem();
    intakesys = new IntakeSubsystem();
    drivesys = new DriveSubsystem();
    pdp = new PowerDistributionPanel();
    armsys = new ArmSubsystem();
    cameras = new Cameras();
    cameras.start();

    drivesys.initDefaultCommand();
    HumanInput.registerButtons();
  }

  // Called periodically regardless of the game period
  @Override
  public void robotPeriodic() {
    
  }


  /* Sandstorm Period */
  // Called at the beginning of the Sandstorm
  @Override
  public void autonomousInit() {

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
    Scheduler.getInstance().run();
  }



  @Override
  public void testInit() {
    
  }
  @Override
  public void testPeriodic() {
    Scheduler.getInstance().run();
  }
=======
    public static DriveSubsystem drivesys;
    public static IntakeSubsystem intakesys;
    public static ArmSubsystem armsys;
    public static ClimbSubsystem climbsys;
    public static PowerDistributionPanel pdp;

    // Use this function for all initialization code
    @Override
    public void robotInit() {
        climbsys = new ClimbSubsystem();
        intakesys = new IntakeSubsystem();
        drivesys = new DriveSubsystem();
        pdp = new PowerDistributionPanel();
        armsys = new ArmSubsystem();

        HumanInput.registerButtons();
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
>>>>>>> 2113606d9adc68a6d1b8246db2d79a94351dd3ad
}

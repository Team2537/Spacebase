/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class ExpelCommand extends Command {
  public ExpelCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if(Robot.manipSys.getPlacementMode() == PlacementMode.HATCH){
      Scheduler.getInstance().add(new ArmPneumaticCommand());
    } else {
      Scheduler.getInstance().add(new ArmFlywheelCommand(1000));
    }
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called repeatedly when this Command is scheduled to run
  
}

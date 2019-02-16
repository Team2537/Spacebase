/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class XboxIntakeCommand extends Command {
  public XboxIntakeCommand() {
    requires(Robot.intakeSys);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(/*Robot.input.xbox.getRawAxis(2) >= 0.8*/ true){
      Robot.intakeSys.setArmFlywheel(1);
    } else if (/*Robot.input.xbox.getRawAxis(3) >= 0.8 */ true){
      Robot.intakeSys.setArmFlywheel(-1);
    } else {
      Robot.intakeSys.setArmFlywheel(0);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.intakeSys.setArmFlywheel(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.intakeSys.setArmFlywheel(0);
  }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class FlywheelCommand extends Command {
  public FlywheelCommand() {
    requires(Robot.intakesys);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.intakesys.turnOnFlywheels();
  
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(Robot.intakesys.getInfrared()){
      return true;
    }else{
      return false;
    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.intakesys.turnOffFlywheels();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.intakesys.turnOffFlywheels();
  }
}

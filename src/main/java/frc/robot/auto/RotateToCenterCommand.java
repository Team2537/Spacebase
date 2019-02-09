/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/*
direction is based off of our initial prediction, 
in short it just tells it which direction the bot needs 
to turn to realign facing forward
*/

public class RotateToCenterCommand extends Command {
  private final double rotateSpeed;
  private final boolean direction;
  public RotateToCenterCommand(boolean direction, double rotateSpeed) {
    requires(Robot.driveSys);
    this.rotateSpeed = rotateSpeed;
    this.direction = direction;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(direction) {
      Robot.driveSys.setMotors(-rotateSpeed, rotateSpeed);
    }
    else if(!direction) {
      Robot.driveSys.setMotors(rotateSpeed, -rotateSpeed);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.driveSys.getIR_frontUpper();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveSys.setMotors(0,0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

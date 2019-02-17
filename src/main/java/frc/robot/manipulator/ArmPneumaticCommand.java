/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ArmPneumaticCommand extends Command {
  private long startTime;

  public ArmPneumaticCommand() {
    requires(Robot.manipSys);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startTime = System.currentTimeMillis();
    Robot.manipSys.setArmPneumatic(true);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {


  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return System.currentTimeMillis() >= startTime + 500;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.manipSys.setArmPneumatic(false);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}

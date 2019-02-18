/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ArmFlywheelCommand extends Command {
  private boolean commandState;
    private static final double FLYWHEEL_SPEED = 0.6;

    public ArmFlywheelCommand(boolean state) {
        requires(Robot.manipSys);
        commandState = state;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      if (commandState) {
        Robot.manipSys.setArmFlywheelMotor(FLYWHEEL_SPEED);
        Robot.intakeSys.setIntakeFlywheels(-0);
    } else {
        Robot.manipSys.setArmFlywheelMotor(-FLYWHEEL_SPEED);
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
        Robot.manipSys.setArmFlywheelMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ArmManualCommand extends Command {
    public ArmManualCommand() {
        requires(Robot.armSys);
        requires(Robot.wristSys);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.armSys.disable();
        Robot.wristSys.disable();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        // flipped because joystick y-axes are flipped
        Robot.armSys.setMotor(-Robot.input.getXboxAxis(1));
        Robot.wristSys.setMotor(-Robot.input.getXboxAxis(5));

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.armSys.enable();
        Robot.wristSys.enable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

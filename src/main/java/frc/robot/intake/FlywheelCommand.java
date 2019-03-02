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
    private boolean commandState;
    private static final double FLYWHEEL_SPEED = 0.4;

    public FlywheelCommand(boolean state) {
        //requires(Robot.intakeSys);
        //commandState = state;

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("flywheels");
       /*
        if (commandState) {
            Robot.intakeSys.setIntakeFlywheels(FLYWHEEL_SPEED);
        } else {
            Robot.intakeSys.setIntakeFlywheels(-FLYWHEEL_SPEED);
        }
        */
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
       // Robot.intakeSys.setIntakeFlywheels(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        //Robot.intakeSys.setIntakeFlywheels(0);
    }
}

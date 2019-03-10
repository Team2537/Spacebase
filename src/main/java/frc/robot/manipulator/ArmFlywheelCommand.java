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
    private long timeout;
    private long startTime;

    public ArmFlywheelCommand(long timeout) {
        requires(Robot.manipSys);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        Robot.manipSys.setArmFlywheelMotor(1);
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (System.currentTimeMillis() - startTime >= timeout);
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

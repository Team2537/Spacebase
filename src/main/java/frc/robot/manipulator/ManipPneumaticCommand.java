/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Specs;
import frc.robot.arm.ArmSubsystem.ArmSetpoint;

public class ManipPneumaticCommand extends Command {

    public static final double WRIST_TILT_OFFSET = -20.0;

    private ArmSetpoint prevSetpoint, curSetpoint;

    private static enum CurrentState {
        UP_AND_IN, DOWN_AND_OUT, UP_AND_OUT
    }
    private CurrentState currentState;

    public ManipPneumaticCommand() {
        requires(Robot.manipSys);
        currentState = CurrentState.UP_AND_IN;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

        switch(currentState) {

        case UP_AND_IN:
            currentState = CurrentState.DOWN_AND_OUT;
            Robot.manipSys.setArmPneumatic(true);
            prevSetpoint = Robot.armSys.getSetpoint();
            if(prevSetpoint != null) {
                curSetpoint = new ArmSetpoint(prevSetpoint.arm, prevSetpoint.wrist + WRIST_TILT_OFFSET, prevSetpoint.name+" + TILT DOWN");
                Robot.armSys.setSetpoint(curSetpoint);
            }
            break;

        case DOWN_AND_OUT:
            currentState = CurrentState.UP_AND_OUT;
            if(prevSetpoint != null && Robot.armSys.getSetpoint() == curSetpoint) {
                Robot.armSys.setSetpoint(prevSetpoint);
            }
            break;

        case UP_AND_OUT:
            currentState = CurrentState.UP_AND_IN;
            Robot.manipSys.setArmPneumatic(false);
            break;
            
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

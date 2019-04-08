/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;
import frc.robot.arm.WaitForWristCommand;

public class ManipPneumaticCommand extends Command {

    public static final double WRIST_TILT_OFFSET_DOWN = -15.0, WRIST_TILT_OFFSET_UP = 5.0;

    private double prevSetpointArm, prevSetpointWrist;

    private static enum CurrentState {
        UP_AND_IN, DOWN_AND_OUT
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

            prevSetpointArm = Robot.armSys.getSetpoint();
            prevSetpointWrist = Robot.awSetpoints.getCurrentLevel().wrist;
            Robot.wristSys.setSetpoint(prevSetpointWrist + WRIST_TILT_OFFSET_DOWN);
            break;

        case DOWN_AND_OUT:
            currentState = CurrentState.UP_AND_IN;
            if(Robot.armSys.getSetpoint() == prevSetpointArm) {

                CommandGroup group = new CommandGroup();
                group.addSequential(new WaitForWristCommand());
                group.addSequential(new Command(){
                    protected void initialize() { Robot.manipSys.setArmPneumatic(false); }
                    protected boolean isFinished() { return true; }
                });

                Robot.wristSys.setSetpoint(prevSetpointWrist + WRIST_TILT_OFFSET_UP);
                Scheduler.getInstance().add(group);

            } else {
                Robot.manipSys.setArmPneumatic(false);
            }
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

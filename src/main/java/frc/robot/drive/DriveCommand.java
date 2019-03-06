/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.input.HumanInput;

public class DriveCommand extends Command {
    private double PERCENT_OUTPUT_MAX = 0.5;

    public DriveCommand() {
        requires(Robot.driveSys);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("we rollin");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(!Robot.climbSys.getClimbSolenoid()){
            Robot.driveSys.setMotors(
                -Robot.input.getJoystickAxisLeft(HumanInput.AXIS_Y) * PERCENT_OUTPUT_MAX,
                -Robot.input.getJoystickAxisRight(HumanInput.AXIS_Y) * PERCENT_OUTPUT_MAX
            );
        } else if(Robot.climbSys.getClimbSolenoid()) {
            Robot.driveSys.setMotors(
                -Robot.input.getJoystickAxisLeft(Math.abs(HumanInput.AXIS_Y)) * PERCENT_OUTPUT_MAX,
                -Robot.input.getJoystickAxisRight(Math.abs(HumanInput.AXIS_Y)) * PERCENT_OUTPUT_MAX
            );
        } else {
            Robot.driveSys.setMotors(
                -Robot.input.getJoystickAxisLeft(HumanInput.AXIS_Y) * PERCENT_OUTPUT_MAX,
                -Robot.input.getJoystickAxisRight(HumanInput.AXIS_Y) * PERCENT_OUTPUT_MAX
            );
        }
        
        //System.out.println("ULTRASONIC: " + Robot.driveSys.getUltrasonic());

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}

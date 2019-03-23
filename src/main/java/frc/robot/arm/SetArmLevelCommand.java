/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class SetArmLevelCommand extends Command {
    private int level;
    private int setSetpoint;

    /**
     * sets the arm level on the robot, need to run arm command to actually move the arm
     * @param level
     * @param setSetpoint 0 if just want to set level, 1 to set lowest point, 2 to set highest point
     */
    public SetArmLevelCommand(int level, int setSetpoint){
        this.level = level;
        this.setSetpoint = setSetpoint;
    }

    public SetArmLevelCommand(int setSetpoint){
        this(0, setSetpoint);
    }



    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(setSetpoint == 0){
            Robot.armSys.setLevel(level);
        } else if (setSetpoint == 1){
            if(Robot.manipSys.getPlacementMode() == PlacementMode.CARGO){
                Robot.armSys.setLevel(2);
            } else {
                Robot.armSys.setLevel(0);
            }
        }  else {
            Robot.armSys.setLevel(Robot.armSys.getCurrentSetpointArray().length - 1);
        }
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
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
    }
}

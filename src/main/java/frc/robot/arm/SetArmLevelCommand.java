/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetArmLevelCommand extends Command {
    private int level;
    private int setSetpoint;

    /**
     * 
     * @param level
     * @param setSetpoint 0 if just want to set level, 1 to set lowest point, 2 to set highest point
     */
    public SetArmLevelCommand(int level, int setSetpoint){
        requires(Robot.armSys);
        this.level = level;
        this.setSetpoint = setSetpoint;
    }

    public SetArmLevelCommand(int level){
        this(level, 0);
    }



    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(setSetpoint == 0){
            Robot.armSys.setArmLevel(Robot.armSys.getArmArray(), level);
        } else if (setSetpoint == 1){
            Robot.armSys.setArmLevel(Robot.armSys.getArmArray(), 0);
        }  else {
            Robot.armSys.setArmLevel(Robot.armSys.getArmArray(), Robot.armSys.getArmArray().length - 1);
            // TODO fix the hell out of this
        }
        
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}

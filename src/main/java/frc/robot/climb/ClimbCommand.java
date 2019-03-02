/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.climb;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ClimbCommand extends Command {
    private int caseNumber;

    public ClimbCommand(int caseNumber) {
        requires(Robot.climbSys);
        this.caseNumber = caseNumber;
    }

    public ClimbCommand(){
        this(0);
        
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        switch(caseNumber){
            case 0:
                Robot.climbSys.setClimbSolenoid(!Robot.climbSys.getClimbSolenoid());
                break;
            case 1:
                Robot.climbSys.setBoosterSolenoid(!Robot.climbSys.getBoosterSolenoid());
                break;
            default:
                Robot.climbSys.setClimbSolenoid(!Robot.climbSys.getClimbSolenoid());
                
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

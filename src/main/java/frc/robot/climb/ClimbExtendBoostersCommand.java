/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.climb;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ClimbExtendBoostersCommand extends Command {

    public ClimbExtendBoostersCommand() {
        requires(Robot.climbSys);
    }

    @Override
    protected void initialize() {
        if(!Robot.climbSys.getBoosterSolenoid()){
            if(Robot.climbSys.getClutchSolenoid()) {
                // Only extend boosters if the clutch is already engaged.
                Robot.climbSys.setBoosterSolenoid(true);
                
                // Disengage clutch so that the robot can drive forward
                // without the cams bumping against the HAB.
                Robot.climbSys.setClutchSolenoid(false);
            }

        } else {
            // We should always be able to retract the boosters.
            Robot.climbSys.setBoosterSolenoid(false);
        }
        
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
        end();
    }
}

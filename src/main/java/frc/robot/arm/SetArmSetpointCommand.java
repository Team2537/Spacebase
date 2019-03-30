/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetArmSetpointCommand extends Command {
    private Double setpoint;

    public SetArmSetpointCommand(Double setpoint){
        requires(Robot.armSys);
        updateSetpoint(setpoint);
    }

    public SetArmSetpointCommand(){
        this(null);
    }

    public void updateSetpoint(Double setpoint){
        this.setpoint = setpoint;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(setpoint == null){
            throw new IllegalStateException("A SetArmSetpointCommand was run with a null setpoint");
        }
        Robot.armSys.setSetpoint(setpoint);
        Robot.armSys.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return true;
    }
}

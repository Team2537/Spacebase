/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmWristSetpoints.ArmSetpoint;

public class IncreaseAWLevelCommand extends CommandGroup {
    private long startTime;
    private boolean stoppedFlywheels;
    private SetArmSetpointCommand armCommand;
    private SetWristSetpointCommand wristCommand;

    public IncreaseAWLevelCommand(){
        this.armCommand = new SetArmSetpointCommand();
        this.wristCommand = new SetWristSetpointCommand();
        this.stoppedFlywheels = false;

        addParallel(armCommand);
        addSequential(wristCommand);
    }

    @Override
    protected void initialize(){
        startTime = System.currentTimeMillis();

        Robot.awSetpoints.increaseLevelIndex();
        final ArmSetpoint currentSetpoint = Robot.awSetpoints.getCurrentLevel();
        armCommand.updateSetpoint(currentSetpoint.arm);
        wristCommand.updateSetpoint(currentSetpoint.wrist);
    }

    @Override
    protected void execute() {
        if(!stoppedFlywheels && System.currentTimeMillis() - startTime >= 500){
            Robot.intakeSys.setIntakeFlywheels(0);
            stoppedFlywheels = true;
        }
    }

    @Override
    protected void end(){
        
    }
}

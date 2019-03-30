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

public class DecreaseAWLevelCommand extends CommandGroup {
    private SetArmSetpointCommand armCommand;
    private SetWristSetpointCommand wristCommand;

    public DecreaseAWLevelCommand(){
        this.armCommand = new SetArmSetpointCommand();
        this.wristCommand = new SetWristSetpointCommand();

        addParallel(armCommand);
        addSequential(wristCommand);
    }

    @Override
    protected void initialize(){
        Robot.awSetpoints.decreaseLevelIndex();
        final ArmSetpoint currentSetpoint = Robot.awSetpoints.getCurrentLevel();
        armCommand.updateSetpoint(currentSetpoint.arm);
        wristCommand.updateSetpoint(currentSetpoint.wrist);
    }
}

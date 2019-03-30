/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;
import frc.robot.arm.SetAWLevelCommand;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class SetPlacementMode extends Command {
    private PlacementMode mode;

    public SetPlacementMode(PlacementMode mode) {
        this.mode = mode;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.manipSys.setPlacementMode(mode);
        Scheduler.getInstance().add(new SetAWLevelCommand(Robot.awSetpoints.getCurrentLevelIndex()));
        System.out.println("ARM CONFIGURATION NOW " + Robot.awSetpoints.getCurrentLevel());

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

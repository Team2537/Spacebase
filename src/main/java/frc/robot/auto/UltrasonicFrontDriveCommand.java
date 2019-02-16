/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

//drives forward until Ultrasonic max distance reached

public class UltrasonicFrontDriveCommand extends Command {
    private double percentOutput, targetDistInches;

    public UltrasonicFrontDriveCommand(double targetDistInches, double percentOutput) {
        requires(Robot.driveSys);
        // this.percentOutput = Math.abs(percentOutput);
        // this.targetDistInches = targetDistInches;
    }

    @Override
    protected void initialize() {
        Robot.driveSys.setMotors(percentOutput, percentOutput);
    }

    @Override
    protected boolean isFinished() {
        return Robot.driveSys.getUltrasonic() <= targetDistInches;
    }

    @Override
    protected void end() {
        Robot.driveSys.setMotors(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}

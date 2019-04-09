/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Specs;
import frc.robot.input.HumanInputManipulatorXbox;

public class DriveCommand extends Command {
    private double PERCENT_OUTPUT_MAX_DEFAULT = 0.9, PERCENT_OUTPUT_MAX_PRECISION = 0.4;
    private double RAMP_UP_TIME = 0.7;
    private double RAMP_UP_AMT =  Specs.ROBOT_PERIOD_SECONDS / RAMP_UP_TIME;
    private double inputLeftPrev, inputRightPrev;

    public DriveCommand() {
        requires(Robot.driveSys);
        inputLeftPrev = 0;
        inputRightPrev = 0;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("we rollin");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        // joystick Y-axes are backwards, so invert
        double 
            inputLeft = -Robot.input.getJoystickAxisLeft(HumanInputManipulatorXbox.AXIS_Y),
            inputRight = -Robot.input.getJoystickAxisRight(HumanInputManipulatorXbox.AXIS_Y)
        ;


        // only go backwards when climb is enabled
        if(Robot.climbSys.getClutchSolenoid()) {
            inputLeft = Math.min(0, inputLeft);
            inputRight = Math.min(0, inputRight);
        }

        if(RAMP_UP_TIME != 0) {
            if(inputLeft - inputLeftPrev > 0) {
                inputLeft = Math.min(inputLeft, inputLeftPrev + RAMP_UP_AMT);
            } else {
                inputLeft = Math.max(inputLeft, inputLeftPrev - RAMP_UP_AMT);
            }

            if(inputRight - inputRightPrev > 0) {
                inputRight = Math.min(inputRight, inputRightPrev + RAMP_UP_AMT);
            } else {
                inputRight = Math.max(inputRight, inputRightPrev - RAMP_UP_AMT);
            }
        }
        inputLeftPrev = inputLeft;
        inputRightPrev = inputRight;


        inputLeft *= getPercentOutputMax();
        inputRight *= getPercentOutputMax();

        Robot.driveSys.setMotors(
            inputLeft, inputRight
        );
    
        //System.out.println("ULTRASONIC: " + Robot.driveSys.getUltrasonic());

    }

    public double getPercentOutputMax() {
        if(Robot.driveSys.getDrivePrecision()){
            return PERCENT_OUTPUT_MAX_PRECISION;
        } else {
            return PERCENT_OUTPUT_MAX_DEFAULT;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveSys.setMotors(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

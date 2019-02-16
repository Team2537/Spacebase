/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ArmCommand extends Command {
    private double errorSum_arm, actualPrev_arm;
    private double errorSum_wrist, actualPrev_wrist;
    private double error_arm, error_wrist;
    private static final double kP_arm = 0.004, kI_arm = 0.0002, kD_arm = 0.003;
    private static final double kP_wrist = 0.004, kI_wrist = 0.0002, kD_wrist = 0.003;
    private static final double TOLERANCE_arm = 2, TOLERANCE_wrist = 2;

    public ArmCommand() {
        requires(Robot.armSys);
    }

    private XboxController xbox; // temp

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        errorSum_arm = 0;
        actualPrev_arm = 0;
        errorSum_wrist = 0;
        actualPrev_wrist = 0;

        xbox = new XboxController(2); // temp
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (xbox.getBumperPressed(Hand.kRight)) {
            Robot.armSys.increaseArmLevel();
        }
        if (xbox.getBumperPressed(Hand.kLeft)) {
            Robot.armSys.decreaseArmLevel();
        }

        double setpoint_arm = Robot.armSys.getArmSetpoint();
        double actual_arm = Robot.armSys.getArmEncoder();
        error_arm = setpoint_arm - actual_arm;
        errorSum_arm += error_arm;
        double actualChange_arm = actual_arm - actualPrev_arm;
        actualPrev_arm = actual_arm;
        if (Math.abs(error_arm) > TOLERANCE_arm) {
            Robot.armSys.setArmMotor(kP_arm * error_arm + kI_arm * errorSum_arm - kD_arm * actualChange_arm);
        }

        double setpoint_wrist = Robot.armSys.getWristSetpoint();
        double actual_wrist = Robot.armSys.getPotentiometer();
        error_wrist = setpoint_wrist - actual_wrist;
        errorSum_wrist += error_wrist;
        double actualChange_wrist = actual_wrist - actualPrev_wrist;
        actualPrev_wrist = actual_wrist;
        if (Math.abs(error_wrist) > TOLERANCE_wrist) {
            Robot.armSys.setWristMotor(
                    -(kP_wrist * error_wrist + kI_wrist * errorSum_wrist - kD_wrist * actualChange_wrist));
            // temporarily negative for testing
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
        Robot.armSys.setArmMotor(0);
        Robot.armSys.setWristMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

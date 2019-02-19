/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ArmCommand extends Command {
    private double errorSum_arm, actualPrev_arm;
    private double errorSum_wrist, actualPrev_wrist;
    private double error_arm, error_wrist;
    private double lastSetpoint_arm, lastSetpoint_wrist;
    private static final double kP_arm = 0.002, kI_arm = 0.00002, kD_arm = 0;
    private static final double kP_wrist = 0.001, kI_wrist = 0.00002, kD_wrist = 0;
    private static final double TOLERANCE_arm = 2, TOLERANCE_wrist = 2;

    public ArmCommand() {
        requires(Robot.armSys);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        errorSum_arm = 0;
        actualPrev_arm = 0;
        errorSum_wrist = 0;
        actualPrev_wrist = 0;

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        double setpoint_arm = Robot.armSys.getArmSetpoint();
        if(setpoint_arm != lastSetpoint_arm){
            errorSum_arm = 0;
        }
        lastSetpoint_arm = setpoint_arm;

        double actual_arm = Robot.armSys.getArmPotentiometer();
        error_arm = -(setpoint_arm - actual_arm);
        errorSum_arm += error_arm;
        double actualChange_arm = actual_arm - actualPrev_arm;
        actualPrev_arm = actual_arm;
        if (Math.abs(error_arm) > TOLERANCE_arm) {
            double percentOutput = kP_arm * error_arm + kI_arm * errorSum_arm - kD_arm * actualChange_arm;
            Robot.armSys.setArmMode(IdleMode.kCoast);
            Robot.armSys.setArmMotor(percentOutput);
        } else {
            Robot.armSys.setArmMode(IdleMode.kBrake);
            Robot.armSys.setArmMotor(0);
        }

        double setpoint_wrist = Robot.armSys.getWristSetpoint();
        if(setpoint_wrist != lastSetpoint_wrist){
            errorSum_wrist = 0;
        }
        lastSetpoint_wrist = setpoint_wrist;

        double actual_wrist = Robot.armSys.getWristPotentiometer();
        error_wrist = setpoint_wrist - actual_wrist;
        System.out.println(error_wrist);
        errorSum_wrist += error_wrist;
        double actualChange_wrist = actual_wrist - actualPrev_wrist;
        actualPrev_wrist = actual_wrist;
        if (Math.abs(error_wrist) > TOLERANCE_wrist) {
            Robot.armSys.setWristMode(NeutralMode.Coast);
            Robot.armSys.setWristMotor(
                (kP_wrist * error_wrist + kI_wrist * errorSum_wrist - kD_wrist * actualChange_wrist));
            // temporarily negative for testing
        } else {
            Robot.armSys.setWristMode(NeutralMode.Brake);
            Robot.armSys.setWristMotor(0);
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
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.PID;
import frc.robot.Robot;
import frc.robot.arm.ArmSubsystem.ArmSetpoint;

public class ArmCommand extends Command {
    private PID armPID, wristPID;
    private static final double kP_arm = 0.01, kI_arm = 0.00004, kD_arm = 0.008;
    private static final double kP_wrist = 0.004, kI_wrist = 0.00003, kD_wrist = 0.002;
    private static final double TOLERANCE_arm = 1, TOLERANCE_wrist = 1;

    public ArmCommand() {
        requires(Robot.armSys);
        armPID = new PID(kP_arm, kI_arm, kD_arm, TOLERANCE_arm);
        wristPID = new PID(kP_wrist, kI_wrist, kD_wrist, TOLERANCE_wrist);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        ArmSetpoint setpoint = Robot.armSys.getSetpoint();
        Robot.armSys.updateSmartDash();
 
        if(setpoint == null){
            Robot.armSys.setArmMotor(0);
            Robot.armSys.setWristMotor(0);
        } else {
            
            armPID.setSetpoint(-setpoint.arm);
            armPID.update(-Robot.armSys.getArmPotentiometer());
            Robot.armSys.setArmMotor(armPID.getOutput());

            wristPID.setSetpoint(setpoint.wrist);
            wristPID.update(Robot.armSys.getWristPotentiometer());
            Robot.armSys.setWristMotor(wristPID.getOutput());
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
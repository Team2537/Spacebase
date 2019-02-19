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
import frc.lib.util.PID;
import frc.robot.Robot;
import frc.robot.arm.ArmSubsystem.ArmSetpoint;

public class ArmCommand extends Command {
    private PID armPID, wristPID;
    private static final double kP_arm = 0.02, kI_arm = 0.0008, kD_arm = 0.01;
    private static final double kP_wrist = 0.02, kI_wrist = 0.0008, kD_wrist = 0.01;
    private static final double TOLERANCE_arm = 2, TOLERANCE_wrist = 2;

    public ArmCommand() {
        requires(Robot.armSys);
        armPID = new PID(kP_arm, kI_arm, kD_arm);
        wristPID = new PID(kP_wrist, kI_wrist, kD_wrist);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        ArmSetpoint setpoint = Robot.armSys.getSetpoint();
        if(setpoint == null){
            Robot.armSys.setArmMode(IdleMode.kBrake);
            Robot.armSys.setArmMotor(0);
            Robot.armSys.setWristMode(NeutralMode.Brake);
            Robot.armSys.setWristMotor(0);
        } else {
            
            armPID.setSetpoint(-setpoint.arm);
            armPID.update(-Robot.armSys.getArmPotentiometer());

            if (!armPID.withinTolerance(TOLERANCE_arm)) {
                Robot.armSys.setArmMode(IdleMode.kCoast);
                Robot.armSys.setArmMotor(armPID.getOutput());
            } else {
                Robot.armSys.setArmMode(IdleMode.kBrake);
                Robot.armSys.setArmMotor(0);
            }


            wristPID.setSetpoint(setpoint.wrist);
            wristPID.update(Robot.armSys.getWristPotentiometer());

            if (!wristPID.withinTolerance(TOLERANCE_wrist)) {
                Robot.armSys.setWristMode(NeutralMode.Coast);
                Robot.armSys.setWristMotor(wristPID.getOutput());
            } else {
                Robot.armSys.setWristMode(NeutralMode.Brake);
                Robot.armSys.setWristMotor(0);
            }
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
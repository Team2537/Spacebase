/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;

public class IntakeSubsystem extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private TalonSRX intakeFlywheelLeft, intakeFlywheelRight;
    private Solenoid intakePneumatic;

    public IntakeSubsystem() {
        intakeFlywheelLeft = new TalonSRX(Ports.INTAKE_FLYWHEEL_LEFT);
        intakeFlywheelRight = new TalonSRX(Ports.INTAKE_FLYWHEEL_RIGHT);
        intakePneumatic = new Solenoid(Ports.INTAKE_SOLENOID);
        
        intakeFlywheelLeft.setNeutralMode(NeutralMode.Coast);
        intakeFlywheelRight.setNeutralMode(NeutralMode.Coast);

    }

    public void setIntakeFlywheels(double speed) {
        intakeFlywheelLeft.set(ControlMode.PercentOutput, /*speed*/ -speed); // TODO: intake motor messed up on test bot, REVERT BEFORE COMP
        intakeFlywheelRight.set(ControlMode.PercentOutput, speed);
    }

    public double getIntakeAmperage() {
        return intakeFlywheelLeft.getOutputCurrent();
    }

    public void setPneumatic(boolean state){
        intakePneumatic.set(state);
        System.out.println("INTAKE PNEUMATIC "+state);
    }
    public boolean getIntakePneumatic() {
        return intakePneumatic.get();
    }

    @Override
    public void initDefaultCommand() {
        
    }


}

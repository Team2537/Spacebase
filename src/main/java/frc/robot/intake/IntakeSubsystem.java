/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.Ports;

public class IntakeSubsystem extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private TalonSRX intakeFlywheelOne, intakeFlywheelTwo;
    private DigitalInput infrared;
    private Solenoid intakePneumatic;
    private static final double FLYWHEEL_SPEED = 0.8;

    public IntakeSubsystem() {
        intakeFlywheelOne = new TalonSRX(Ports.INTAKE_FLYWHEEL_ONE);
        intakeFlywheelTwo = new TalonSRX(Ports.INTAKE_FLYWHEEL_TWO);
        infrared = new DigitalInput(Ports.INTAKE_INFRARED);
        //intakePneumatic = new Solenoid(Ports.INTAKE_PNEUMATIC_ONE);

    }

    public void setIntakeFlywheels(double speed) {
        intakeFlywheelOne.set(ControlMode.PercentOutput, speed);
        intakeFlywheelTwo.set(ControlMode.PercentOutput, speed);

    }



    public void setPneumatic(boolean state){
        intakePneumatic.set(state);
    }
    public boolean getIntakePneumatic() {
        return intakePneumatic.get();
    }

    public boolean getInfrared() {
        return infrared.get();
    }

    @Override
    public void initDefaultCommand() {
        
    }

	public void setArmFlywheelMotor(int i) {
	}

}

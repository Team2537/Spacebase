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

    private TalonSRX intakeFlywheelOne, intakeFlywheelTwo;
    private Solenoid intakePneumatic;

    public IntakeSubsystem() {
        intakeFlywheelOne = new TalonSRX(Ports.INTAKE_FLYWHEEL_ONE);
        intakeFlywheelTwo = new TalonSRX(Ports.INTAKE_FLYWHEEL_TWO);
        intakePneumatic = new Solenoid(Ports.INTAKE_SOLENOID);
        
        intakeFlywheelOne.setNeutralMode(NeutralMode.Coast);
        intakeFlywheelTwo.setNeutralMode(NeutralMode.Coast);

    }

    public void setIntakeFlywheels(double speed) {
        intakeFlywheelOne.set(ControlMode.PercentOutput, speed);
        intakeFlywheelTwo.set(ControlMode.PercentOutput, speed);

    }

    public double getIntakeAmperage() {
        return intakeFlywheelOne.getOutputCurrent();
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

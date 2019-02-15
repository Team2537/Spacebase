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
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.Ports;
 


public class IntakeSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

private TalonSRX armFlywheel, intakeFlywheelOne, intakeFlywheelTwo;
private DigitalInput infrared;
private Solenoid pneumatic1;
private Solenoid pneumatic2;
private Potentiometer pot;
private static final double FLYWHEEL_SPEED = 0.8; 
private static final int POT_OFFSET = 0; //TODO find the real value of this


public IntakeSubsystem(){
  
armFlywheel= new TalonSRX(Ports.INTAKE_ARM_FLYWHEEL);
intakeFlywheelOne = new TalonSRX(Ports.INTAKE_FLYWHEEL_ONE);
intakeFlywheelTwo = new TalonSRX(Ports.INTAKE_FLYWHEEL_TWO);
infrared = new DigitalInput(Ports.INTAKE_INFRARED);
pneumatic1 = new Solenoid(Ports.INTAKE_PNEUMATIC_ONE);
pneumatic2 = new Solenoid(Ports.INTAKE_PNEUMATIC_TWO);
pot = new AnalogPotentiometer(0, 3600, POT_OFFSET);

}

public void setIntakeFlywheels(double speed){
  intakeFlywheelOne.set(ControlMode.PercentOutput, speed);
  intakeFlywheelTwo.set(ControlMode.PercentOutput, speed);

}

public void setArmFlywheel(double speed){
  armFlywheel.set(ControlMode.PercentOutput, speed);
}

public void pneumaticExtend(){
  pneumatic1.set(true);
  pneumatic2.set(true);
}

public void pneumaticRetract(){
  pneumatic1.set(false);
  pneumatic2.set(false);

}

public boolean getIntakePneumatic(){
  return pneumatic1.get();
}


public double getPotentiometer(){
  return pot.get();
}

public boolean getInfrared(){
  return infrared.get();
}
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }


}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;
 


public class IntakeSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

Talon flywheel1;
Talon flywheel2;
DigitalInput infrared;
Solenoid pneumatic1;
Solenoid pneumatic2;
Double speed;


public IntakeSubsystem(){
  
flywheel1 = new Talon(Ports.INTAKE_MOTOR_ONE);
flywheel2 = new Talon(Ports.INTAKE_MOTOR_TWO);
infrared = new DigitalInput(Ports.INTAKE_INFRARED);
pneumatic1 = new Solenoid(Ports.INTAKE_PNEUMATIC_ONE);
pneumatic2 = new Solenoid(Ports.INTAKE_PNEUMATIC_TWO);

}

public void turnOnFlywheels(){
  flywheel1.set(speed);
  flywheel2.set(speed);

}

public void pneumaticExtend(){
  pneumatic1.set(true);
  pneumatic2.set(true);
}

public void pneumaticRetract(){
  pneumatic1.set(false);
  pneumatic2.set(false);

}

public void turnOffFlywheels(){
  flywheel1.set(0);
  flywheel2.set(0);


}
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }


}

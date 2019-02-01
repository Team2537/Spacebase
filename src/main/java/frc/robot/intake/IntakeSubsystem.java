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
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.HumanInput;
import frc.robot.Ports;
 


public class IntakeSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

Talon flywheel1;
Talon flywheel2;
DigitalInput infrared;
Solenoid pneumatic;
JoystickButton flywheelsOn;
JoystickButton solExtend;
JoystickButton solretract;
Double speed;


public IntakeSubsystem(){
  
Talon flywheel1 = new Talon(Ports.INTAKE_MOTOR_ONE);
Talon flywheel2 = new Talon(Ports.INTAKE_MOTOR_TWO);
DigitalInput infrared = new DigitalInput(Ports.INTAKE_INFRARED);
Solenoid pneumatic = new Solenoid(Ports.INTAKE_PNEUMATIC_ONE);
JoystickButton flywheelsOn = new JoystickButton(HumanInput.joystickLeft, Ports.INTAKE_ON);
JoystickButton solExtend = new JoystickButton(HumanInput.joystickLeft, Ports.PNEUMATIC_EXTEND);
JoystickButton solretract = new JoystickButton(HumanInput.joystickLeft, Ports.PNEUMATIC_RETRACT);

}



  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }


}

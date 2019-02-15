/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.climb;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;

/**
 * Add your docs here.
 */
public class ClimbSubsystem extends Subsystem {
  private Solenoid climbSol;

  public void ClimbSubsystem(){
    climbSol = new Solenoid(Ports.CLIMB_SOLENOID_ONE);
  }

  public void setSolenoid(boolean state){
    climbSol.set(state);
  }

  public boolean getSolenoid(){
    return climbSol.get();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

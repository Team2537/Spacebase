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
    private Solenoid climbSolOne, climbSolTwo, climbSolThree;

    public ClimbSubsystem() {
        climbSolOne = new Solenoid(Ports.CLIMB_SOLENOID_ONE);
        climbSolTwo = new Solenoid(Ports.CLIMB_SOLENOID_TWO);
        climbSolThree = new Solenoid(Ports.CLIMB_SOLENOID_THREE);
    }

    public void setClimbSolenoid(boolean state) {
        climbSolOne.set(state);
        climbSolTwo.set(!state);
    }

    public void setBoosterSolenoid(boolean state){
        climbSolThree.set(state);
    }

    public boolean getBoosterSolenoid(){
        return climbSolThree.get();
    }

    public boolean getClimbSolenoid() {
        return climbSolOne.get();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}

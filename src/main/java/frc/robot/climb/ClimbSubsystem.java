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
    private Solenoid clutchSolOne, clutchSolTwo, boosterSolOne, boosterSolTwo;

    public ClimbSubsystem() {
        clutchSolOne = new Solenoid(Ports.CLIMB_CLUTCH_SOLENOID_ONE);
        clutchSolTwo = new Solenoid(Ports.CLIMB_CLUTCH_SOLENOID_TWO);
        boosterSolOne = new Solenoid(Ports.CLIMB_SOLENOID_THREE);
        boosterSolTwo = new Solenoid(Ports.CLIMB_SOLENOID_FOUR);
    }

    public void setClimbSolenoid(boolean state) {
        clutchSolOne.set(state);
        clutchSolTwo.set(!state);
    }

    public void setBoosterSolenoid(boolean state){
        boosterSolOne.set(state);
        boosterSolTwo.set(!state);
    }

    public boolean getBoosterSolenoid(){
        return boosterSolOne.get();
    }

    public boolean getClutchSolenoid() {
        return clutchSolOne.get();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}

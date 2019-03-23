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
        boosterSolOne = new Solenoid(Ports.CLIMB_BOOSTER_SOLENOID_ONE);
        boosterSolTwo = new Solenoid(Ports.CLIMB_BOOSTER_SOLENOID_TWO);
    }

    public void setClutchSolenoid(boolean state) {
        clutchSolOne.set(!state);
        clutchSolTwo.set(state);  // TODO: test climb solenoid direction on the comp bot?
    }

    public void setBoosterSolenoid(boolean state){
        boosterSolOne.set(!state);  // TODO: test climb solenoid direction on the comp bot?
        boosterSolTwo.set(state);
    }

    public boolean getBoosterSolenoid(){
        return boosterSolTwo.get();
    }

    public boolean getClutchSolenoid() {
        return clutchSolTwo.get(); // TODO: test climb solenoid direction on the comp bot?
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.manipulator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;

/**
 * Add your docs here.
 */
public class ManipulatorSubsystem extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private TalonSRX armFlywheel;
    private Solenoid armPneumatic;
    private PlacementMode placementMode;

    public ManipulatorSubsystem() {
        armFlywheel = new TalonSRX(Ports.INTAKE_ARM_FLYWHEEL);
        armPneumatic = new Solenoid(Ports.ARM_SOLENOID_ONE);
        placementMode = PlacementMode.HATCH;
    }

    public void setPlacementMode(PlacementMode placementMode) {
        this.placementMode = placementMode;
    }

    public PlacementMode getPlacementMode() {
        return placementMode;
    }

    public void setArmFlywheelMotor(double speed) {
        armFlywheel.set(ControlMode.PercentOutput, speed);
    }

    public void setArmPneumatic(boolean state) {
        armPneumatic.set(state);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new XboxIntakeCommand());
    }

    public static enum PlacementMode {
        HATCH, CARGO
    }
}

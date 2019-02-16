/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.input;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Ports;
import frc.robot.Specs;
import frc.robot.arm.DecreaseArmCommand;
import frc.robot.arm.IncreaseArmCommand;
import frc.robot.climb.ClimbCommand;
import frc.robot.intake.ArmFlywheelCommand;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;

/**
 * Add your docs here.
 */
public class HumanInputManipulatorXbox {
    public Joystick joystickLeft, joystickRight, xbox;
    public JoystickButton 
        intakeFlywheelsForward, intakeFlywheelsBackward, intakeSolExtend,
        armFlywheelIn, armFlywheelOut, increasearmbutton, decreasearmbutton, 
        climbEngageClutch, 
        cameraButton
    ;
    


    public HumanInputManipulatorXbox() {
        joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
        joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);
        xbox = new Joystick(Ports.XBOX_CONTROLLER);
        
        
        

        
        // Button aliases
        intakeFlywheelsForward = new JoystickButton(joystickLeft, 1);
        intakeFlywheelsBackward = new JoystickButton(joystickRight, 1);
        armFlywheelIn = new JoystickButton(joystickLeft, Ports.ARM_INTAKE_FLYWHEEL_IN);
        armFlywheelOut = new JoystickButton(joystickRight, Ports.ARM_INTAKE_FLYWHEEL_OUT);
        intakeSolExtend = new JoystickButton(joystickRight, Ports.INTAKE_PNEUMATIC_EXTEND);
        climbEngageClutch = new JoystickButton(joystickLeft, Ports.CLIMB_ENGAGE_CLUTCH);
        increasearmbutton = new JoystickButton(joystickRight, Ports.ARM_UP_BUTTON);
        decreasearmbutton = new JoystickButton(joystickRight, Ports.ARM_DOWN_BUTTON);
        cameraButton = new JoystickButton(joystickLeft, Ports.CAMERA_BUTTON);
        
    }

    public void registerButtons() {
        whileHeldCommand(armFlywheelIn, new ArmFlywheelCommand(true));
        whileHeldCommand(armFlywheelOut, new ArmFlywheelCommand(false));
        whileHeldCommand(intakeFlywheelsForward, new FlywheelCommand(true));
        whileHeldCommand(intakeFlywheelsBackward, new FlywheelCommand(false));
        whenPressedCommand(intakeSolExtend, new PneumaticExtendCommand());
        whenPressedCommand(increasearmbutton, new IncreaseArmCommand());
        whenPressedCommand(decreasearmbutton, new DecreaseArmCommand());
        whenPressedCommand(climbEngageClutch, new ClimbCommand());
    }

    public static final int AXIS_X = 0, AXIS_Y = 1;

    private double getJoystickAxis(int axis, Joystick joystick) {
        double val = joystick.getRawAxis(axis);
        if (Math.abs(val) <= Specs.JOYSTICK_DEADZONE)
            return 0;
        else
            return val;
    }

    public double getJoystickAxisLeft(int axis) {
        return getJoystickAxis(axis, joystickLeft);
    }

    public double getJoystickAxisRight(int axis) {
        return getJoystickAxis(axis, joystickRight);
    }

    private static void whenPressedCommand(Button button, Command command) {
        button.whenPressed(command);
    }

    private static void whileHeldCommand(Button button, Command command) {
        button.whileHeld(command);
    }

    private static void whenReleasedCommand(Button button, Command command) {
        button.whenReleased(command);
    }
}

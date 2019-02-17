/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.input;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Ports;
import frc.robot.Specs;
import frc.robot.arm.ArmManualCommand;
import frc.robot.arm.DecreaseArmCommand;
import frc.robot.arm.IncreaseArmCommand;
import frc.robot.climb.ClimbCommand;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;

public class HumanInputManipulatorXbox {
    public static final int AXIS_X = 0, AXIS_Y = 1;

    public final Joystick joystickLeft, joystickRight, xbox;
    public final JoystickButton 
        intakeFlywheelsForward, intakeFlywheelsBackward, intakeSolExtend,
        // armFlywheelIn, armFlywheelOut, 
        increasearmbutton, decreasearmbutton, armSetIntakeButton, armSetHighButton, armManualButton,
        climbEngageClutch, 
        cameraButton
    ;

    public HumanInputManipulatorXbox() {
        /*   --- Controllers ---  */
        joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
        joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);
        xbox = new Joystick(Ports.XBOX_CONTROLLER);
          
        /* --- Button Aliases --- */
        // Left Joystick
        intakeFlywheelsForward  = new JoystickButton(joystickLeft, 1);
        cameraButton            = new JoystickButton(joystickLeft, 2);
        climbEngageClutch       = new JoystickButton(joystickLeft, 3);

        // Right Joystick
        intakeFlywheelsBackward = new JoystickButton(joystickRight, 1);

        // Xbox Controller
        decreasearmbutton   = new JoystickButton(xbox, 1);
        armSetHighButton    = new JoystickButton(xbox, 2);
        armSetIntakeButton  = new JoystickButton(xbox, 3);
        increasearmbutton   = new JoystickButton(xbox, 4);
        armManualButton     = new JoystickButton(xbox, 5);
        intakeSolExtend     = new JoystickButton(xbox, 6);
    }

    public void registerButtons() {
        //whileHeldCommand(armFlywheelIn, new ArmFlywheelCommand(true));
        //whileHeldCommand(armFlywheelOut, new ArmFlywheelCommand(false));
        // TODO: map these ^
        whileHeldCommand(intakeFlywheelsForward, new FlywheelCommand(true));
        whileHeldCommand(intakeFlywheelsBackward, new FlywheelCommand(false));
        whileHeldCommand(armManualButton, new ArmManualCommand());
        whenPressedCommand(intakeSolExtend, new PneumaticExtendCommand());
        whenPressedCommand(increasearmbutton, new IncreaseArmCommand());
        whenPressedCommand(decreasearmbutton, new DecreaseArmCommand());
        whenPressedCommand(climbEngageClutch, new ClimbCommand());
    }


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

    public double getXboxAxis(int axis){
        return getJoystickAxis(axis, xbox);
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

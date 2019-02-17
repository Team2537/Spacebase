package frc.robot.input;

import edu.wpi.first.wpilibj.Joystick;
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

public class HumanInput {
    public final Joystick joystickLeft, joystickRight;
    public final JoystickButton 
        intakeFlywheelsForward, intakeFlywheelsBackward, intakeSolExtend,
        armFlywheelIn, armFlywheelOut, increasearmbutton, decreasearmbutton, 
        climbEngageClutch, 
        cameraButton
    ;

    public HumanInput() {
        /*  --- Controllers ---   */
        joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
        joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);

        /* --- Button Aliases --- */
        // Left Joystick
        armFlywheelIn           = new JoystickButton(joystickLeft, 1);
        climbEngageClutch       = new JoystickButton(joystickLeft, 2);
        cameraButton            = new JoystickButton(joystickLeft, 3);
        intakeFlywheelsBackward = new JoystickButton(joystickLeft, 4);
        intakeFlywheelsForward  = new JoystickButton(joystickLeft, 5);

        // Right Joystick
        armFlywheelOut      = new JoystickButton(joystickRight, 1);
        decreasearmbutton   = new JoystickButton(joystickRight, 2);
        increasearmbutton   = new JoystickButton(joystickRight, 3);
        // UNUSED           = new JoystickButton(joystickRight, 4);
        intakeSolExtend     = new JoystickButton(joystickRight, 5);
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

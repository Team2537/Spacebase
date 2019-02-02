package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class HumanInput {
    public static Joystick joystickLeft  = new Joystick(Ports.LEFT_JOYSTICK);
    public static Joystick joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);

    // Button aliases
    // public static Button 
    public static Button increasearmbutton = new JoystickButton(joystickRight, 1);
    public static Button decreasearmbutton = new JoystickButton(joystickLeft, 1);
    public static final int AXIS_X = 0, AXIS_Y = 1;

    private static double getJoystickAxis(int axis, Joystick joystick){
        double val = joystick.getRawAxis(axis);
        if(Math.abs(val) <= Specs.JOYSTICK_DEADZONE) return 0;
        else return val;
    }
    public static double getJoystickAxisLeft(int axis){
        return getJoystickAxis(axis, joystickLeft);
    }
    public static double getJoystickAxisRight(int axis){
        return getJoystickAxis(axis, joystickRight);
    }

    public static void whenPressedCommand(Button a, Command b){
        a.whenPressed(b);
    }
}
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class HumanInput {
    /** Prevent this class from being instantiated. */
    private HumanInput() { }

    public static Joystick joystickLeft  = new Joystick(Ports.LEFT_JOYSTICK);
    public static Joystick joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);

    // Button aliases
    //JoystickLeft - Cargo
    public static Button decreaseheightCargo = new JoystickButton(joystickLeft, 4);
    public static Button setInitialHeightCargo = new JoystickButton(joystickLeft, 3);
    public static Button increaseHeightCargo = new JoystickButton(joystickLeft, 5);
    public static Button intakeCargo = new JoystickButton(joystickLeft, 2);
    public static Button placeCargo = new JoystickButton(joystickLeft, 1);
    
    //JoystickRight - Hatch
    public static Button decreaseheightHatch = new JoystickButton(joystickRight, 4);
    public static Button setInitialHeightHatch = new JoystickButton(joystickRight, 3);
    public static Button increaseHeightHatch = new JoystickButton(joystickRight, 5);
    public static Button intakeHatch = new JoystickButton(joystickRight, 2);
    public static Button placeHatch = new JoystickButton(joystickRight, 1);

    public static void registerCommands(){

    }

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

    public static void whenPressed(Button button, Command command){
        button.whenPressed(command);
    }
    public static void whileHeld(Button button, Command command){
        button.whileHeld(command);
    }
    public static void whenReleased(Button button, Command command){
        button.whenReleased(command);
    }
}
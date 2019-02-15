package frc.robot;

import static org.junit.Assert.assertEquals;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.XboxController;
public class HumanInput {

    /**
     *
     */

    private static final int _1 = 1;
    public static Joystick joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
    public static Joystick joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);
    // Button aliases
    // public static Button 

    //Xbox Button Control

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

    public static void registerCommandWhenPressed(Button button, Command command){
        button.whenPressed(command);
    }
    public static void registerCommandWhileHeld(Button button, Command command){
        button.whileHeld(command);
    }
    public static void registerCommandWhenReleased(Button button, Command command){
        button.whenReleased(command);
    }
    


}

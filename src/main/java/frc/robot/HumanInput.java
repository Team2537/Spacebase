package frc.robot;

import static org.junit.Assert.assertEquals;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;
import frc.robot.intake.PneumaticRetractCommand;
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


    public static Button flywheelsOn = new JoystickButton(HumanInput.joystickLeft, Ports.INTAKE_ON);
    public static Button solExtend = new JoystickButton(HumanInput.joystickLeft, Ports.PNEUMATIC_EXTEND);
    public static Button solRetract = new JoystickButton(HumanInput.joystickLeft, Ports.PNEUMATIC_RETRACT);

   
    public static void registerCommands(){
        whileHeldCommand(flywheelsOn, new FlywheelCommand());
        whenPressedCommand(solExtend, new PneumaticExtendCommand());
        whenPressedCommand(solRetract, new PneumaticRetractCommand());
    }
    
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

    private static void whenPressedCommand(Button button, Command command){
        button.whenPressed(command);
    }
    private static void whileHeldCommand(Button button, Command command){
        button.whileHeld(command);
    }
    private static void whenReleasedCommand(Button button, Command command){
        button.whenReleased(command);
    }
}

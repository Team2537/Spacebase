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
    public static XboxController armControl = new XboxController(Ports.XboxController);
    // Button aliases
    // public static Button 

    //Xbox Button Control
    public static Button decreaseHeight = new XboxButton(armControl,'X');
    public static Button increaseHeight = new XboxButton(armControl, 'A');
    public static Button setInitialHeightHatch = new XboxButton(armControl, 'Y');
    public static Button setInitialHeightCargo = new XboxButton(armControl, 'A');
    public static Button increaseHeightManual = new XboxButton(armControl, 'RT');
    public static Button decreaseHeightManual = new XboxButton(armControl, 'LT');


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
    
    public static boolean getButtonIncreaseHeight(){
        return(armControl.getBButton());
    }
    public static boolean getButtonDecreaseHeight(){
        return(armControl.getXButton());
    }
    public static boolean getButtonSetInitHeightHatch(){
        return(armControl.getYButton());
    }
    public static boolean getButtonSetInitHeightCargo(){
        return(armControl.getAButton());
    }
    public static boolean getButtonDecreaseHeightManual(){
        return(armControl.getAButton());
    }
    public static boolean getButtonIncreaseHeightManual(){
        return(armControl.getAButton());
    }
    public static double getTriggerAxis(GenericHID.Hand hand)
    {
        if(hand.equals("right"))
        {
            
        }
        if(hand.equals("left"))
        {

        }
    }


}

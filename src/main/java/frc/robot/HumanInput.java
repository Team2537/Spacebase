package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.arm.DecreaseArmCommand;
import frc.robot.arm.IncreaseArmCommand;
import frc.robot.climb.ClimbCommand;
import frc.robot.intake.ArmFlywheelCommand;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;

public class HumanInput {
    /** Prevent this class from being instantiated. */
    private HumanInput() {
    }

    public static Joystick joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
    public static Joystick joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);
    // Button aliases
    // public static Button

<<<<<<< HEAD
    public static JoystickButton intakeFlywheelsForward = new JoystickButton(joystickLeft, Ports.INTAKE_FLYWHEEL_OUT);
    public static JoystickButton intakeFlywheelsBackward = new JoystickButton(joystickLeft, Ports.INTAKE_FLYWHEEL_IN);
    public static JoystickButton armFlywheelIn = new JoystickButton(joystickLeft, Ports.ARM_INTAKE_FLYWHEEL_IN);
    public static JoystickButton armFlywheelOut = new JoystickButton(joystickRight, Ports.ARM_INTAKE_FLYWHEEL_OUT);
    public static JoystickButton intakeSolExtend = new JoystickButton(joystickRight, Ports.INTAKE_PNEUMATIC_EXTEND);
    public static JoystickButton climbEngageClutch = new JoystickButton(joystickLeft, Ports.CLIMB_ENGAGE_CLUTCH);
    public static JoystickButton increasearmbutton = new JoystickButton(joystickRight, Ports.ARM_UP_BUTTON);
    public static JoystickButton decreasearmbutton = new JoystickButton(joystickRight, Ports.ARM_DOWN_BUTTON);
    public static JoystickButton cameraButton = new JoystickButton(joystickLeft, Ports.CAMERA_BUTTON);
    
=======
    public static Button 
        intakeFlywheelsForward = new JoystickButton(joystickLeft, Ports.INTAKE_FLWYEEL_OUT),
        intakeFlywheelsBackward = new JoystickButton(joystickLeft, Ports.INTAKE_FLWYEEL_OUT),
        armFlywheelIn = new JoystickButton(joystickRight, Ports.ARM_INTAKE_FLYWHEEL_IN),
        armFlywheelOut = new JoystickButton(joystickRight, Ports.ARM_INTAKE_FLYWHEEL_OUT),
        intakeSolExtend = new JoystickButton(joystickLeft, Ports.INTAKE_PNEUMATIC_EXTEND),
        climbEngageClutch = new JoystickButton(joystickLeft, Ports.CLIMB_ENGAGE_CLUTCH),
        increasearmbutton = new JoystickButton(joystickRight, Ports.ARM_UP_BUTTON),
        decreasearmbutton = new JoystickButton(joystickRight, Ports.ARM_DOWN_BUTTON);
>>>>>>> 2113606d9adc68a6d1b8246db2d79a94351dd3ad

    // public static Button solRetract = new JoystickButton(HumanInput.joystickLeft,
    // Ports.PNEUMATIC_RETRACT);

    public static void registerButtons() {
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

    private static double getJoystickAxis(int axis, Joystick joystick) {
        double val = joystick.getRawAxis(axis);
        if (Math.abs(val) <= Specs.JOYSTICK_DEADZONE)
            return 0;
        else
            return val;
    }

    public static double getJoystickAxisLeft(int axis) {
        return getJoystickAxis(axis, joystickLeft);
    }

    public static double getJoystickAxisRight(int axis) {
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

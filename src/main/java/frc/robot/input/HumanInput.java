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
    public Joystick joystickLeft, joystickRight;
    public JoystickButton 
        intakeFlywheelsForward, intakeFlywheelsBackward, intakeSolExtend,
        armFlywheelIn, armFlywheelOut, increasearmbutton, decreasearmbutton, 
        climbEngageClutch, 
        cameraButton
    ;

    public HumanInput() {
        joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
        joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);

        // Button aliases
        intakeFlywheelsForward = new JoystickButton(joystickLeft, Ports.INTAKE_FLYWHEEL_OUT);
        intakeFlywheelsBackward = new JoystickButton(joystickLeft, Ports.INTAKE_FLYWHEEL_IN);
        armFlywheelIn = new JoystickButton(joystickLeft, Ports.ARM_INTAKE_FLYWHEEL_IN);
        armFlywheelOut = new JoystickButton(joystickRight, Ports.ARM_INTAKE_FLYWHEEL_OUT);
        intakeSolExtend = new JoystickButton(joystickRight, Ports.INTAKE_PNEUMATIC_EXTEND);
        climbEngageClutch = new JoystickButton(joystickLeft, Ports.CLIMB_ENGAGE_CLUTCH);
        increasearmbutton = new JoystickButton(joystickRight, Ports.ARM_UP_BUTTON);
        decreasearmbutton = new JoystickButton(joystickRight, Ports.ARM_DOWN_BUTTON);
        cameraButton = new JoystickButton(joystickLeft, Ports.CAMERA_BUTTON);
        // public static Button solRetract = new JoystickButton(HumanInput.joystickLeft,
        // Ports.PNEUMATIC_RETRACT);
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

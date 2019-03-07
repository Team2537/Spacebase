/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.input;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Ports;
import frc.robot.Specs;
import frc.robot.arm.ArmManualCommand;
import frc.robot.arm.ArmSubsystem;
import frc.robot.arm.DecreaseArmCommand;
import frc.robot.arm.IncreaseArmCommand;
import frc.robot.arm.SetArmLevelCommand;
import frc.robot.arm.SetArmSetpointCommand;
import frc.robot.auto.VisionAlignmentCommand;
import frc.robot.climb.ClimbCommand;
import frc.robot.manipulator.ArmFlywheelCommand;
import frc.robot.manipulator.ArmPneumaticCommand;
import frc.robot.manipulator.XboxIntakeCommand;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;

public class HumanInputManipulatorXbox {
    public static final int AXIS_X = 0, AXIS_Y = 1;
    public final Joystick joystickLeft, joystickRight;
    public final XboxController xbox;
    public final JoystickButton 
        intakeFlywheelsLeft, intakeFlywheelsRight, armSolExtend, intakeSolExtend,
        expelCargo, intakeCargo,
        increasearmbutton, decreasearmbutton, armSetIntakeButton, armSetHighButton, armManualButton,
        climbEngageClutch, 
        cameraButton,
        visionAlignment,
        hatchConfig, cargoConfig
    ;
    public boolean configState;

    public HumanInputManipulatorXbox() {
        /*   --- Controllers ---  */
        joystickLeft = new Joystick(Ports.LEFT_JOYSTICK);
        joystickRight = new Joystick(Ports.RIGHT_JOYSTICK);
        xbox = new XboxController(Ports.XBOX_CONTROLLER);
          
        /* --- Button Aliases --- */
        // Left Joystick
        intakeFlywheelsLeft     = new JoystickButton(joystickLeft, 1);
        cameraButton            = new JoystickButton(joystickLeft, 2);
        expelCargo              = new JoystickButton(joystickLeft, 3);
        // UNUSED               = new JoystickButton(joystickLeft, 4);
        // UNUSED               = new JoystickButton(joystickLeft, 5);
        armSolExtend            = new JoystickButton(joystickLeft, 8);

        // Right Joystick
        intakeFlywheelsRight    = new JoystickButton(joystickRight, 1);
        visionAlignment         = new JoystickButton(joystickRight, 2);
        intakeSolExtend         = new JoystickButton(joystickRight, 3);
        climbEngageClutch       = new JoystickButton(joystickRight, 10);


        // Xbox Controller
        decreasearmbutton       = new JoystickButton(xbox, 1); //A
        armSetHighButton        = new JoystickButton(xbox, 2); //B
        armSetIntakeButton      = new JoystickButton(xbox, 3); //X
        increasearmbutton       = new JoystickButton(xbox, 4); //Y
        armManualButton         = new JoystickButton(xbox, 5); //LB
        hatchConfig             = new JoystickButton(xbox, 6); //RB
        cargoConfig             = new JoystickButton(xbox, 7); //BACK
        intakeCargo             = new JoystickButton(xbox, 8); //START

    }

    public void registerButtons() {
        whileHeldCommand(intakeFlywheelsLeft, new FlywheelCommand(false));
        whileHeldCommand(intakeFlywheelsRight, new FlywheelCommand(false));
        whileHeldCommand(armManualButton, new ArmManualCommand());
        whenPressedCommand(armSolExtend, new ArmPneumaticCommand());
        whenPressedCommand(increasearmbutton, new IncreaseArmCommand());
        whenPressedCommand(decreasearmbutton, new DecreaseArmCommand());
        whenPressedCommand(climbEngageClutch, new ClimbCommand());
        whenPressedCommand(intakeSolExtend, new PneumaticExtendCommand());
        whenPressedCommand(armSetHighButton, new SetArmLevelCommand(0));//TODO FIX THIS
        whenPressedCommand(armSetIntakeButton, new SetArmSetpointCommand(ArmSubsystem.SETPOINT_INTAKE));
        whenPressedCommand(visionAlignment, new VisionAlignmentCommand());
        whenPressedCommand(expelCargo, new ArmFlywheelCommand(false));
        whenPressedCommand(intakeCargo, new XboxIntakeCommand());
    }


    private double getJoystickAxis(int axis, GenericHID joystick, double deadzone) {
        double val = joystick.getRawAxis(axis);
        if (Math.abs(val) <= deadzone)
            return 0;
        else
            return val;
    }

    public double getJoystickAxisLeft(int axis) {
        return getJoystickAxis(axis, joystickLeft, Specs.JOYSTICK_DEADZONE);
    }

    public double getJoystickAxisRight(int axis) {
        return getJoystickAxis(axis, joystickRight, Specs.JOYSTICK_DEADZONE);
    }

    public double getXboxAxis(int axis){
        return getJoystickAxis(axis, xbox, Specs.XBOX_DEADZONE);
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

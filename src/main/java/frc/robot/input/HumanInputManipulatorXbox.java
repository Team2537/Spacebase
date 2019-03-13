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
import frc.robot.arm.DecreaseArmCommand;
import frc.robot.arm.IncreaseArmCommand;
import frc.robot.arm.SetArmLevelCommand;
import frc.robot.auto.VisionAlignmentCommand;
import frc.robot.climb.ClimbEngageClutchCommand;
import frc.robot.manipulator.ManipExpelCommand;
import frc.robot.manipulator.SetPlacementMode;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;
import frc.robot.intake.IntakeFlywheelCommand;
import frc.robot.intake.IntakeExtendCommand;

public class HumanInputManipulatorXbox {
    public static final int AXIS_X = 0, AXIS_Y = 1;
    public final Joystick joystickLeft, joystickRight;
    public final XboxController xbox;
    private final JoystickButton 
        intakeFlywheelIntake, intakeFlywheelExpel, intakeExtend, 
        armIncreaseLevel, armDecreaseLevel, armSetLevelLowest, armSetLevelHighest, armManualToggle,
        climbEngageClutch,
        manipExpel_right, manipExpel_left,
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
        intakeFlywheelIntake    = new JoystickButton(joystickLeft, 1);
        // cameraButton         = new JoystickButton(joystickLeft, 2);
        // UNUSED               = new JoystickButton(joystickLeft, 3);
        // UNUSED               = new JoystickButton(joystickLeft, 4);
        manipExpel_left         = new JoystickButton(joystickLeft, 5);

        // Right Joystick
        intakeFlywheelExpel     = new JoystickButton(joystickRight, 1);
        visionAlignment         = new JoystickButton(joystickRight, 2);
        intakeExtend            = new JoystickButton(joystickRight, 3);
        manipExpel_right        = new JoystickButton(joystickRight, 5);
        climbEngageClutch       = new JoystickButton(joystickRight, 10);

        // Xbox Controller
        armDecreaseLevel        = new JoystickButton(xbox, 1); // A
        armSetLevelHighest      = new JoystickButton(xbox, 2); // B
        armSetLevelLowest       = new JoystickButton(xbox, 3); // X
        armIncreaseLevel        = new JoystickButton(xbox, 4); // Y
        armManualToggle         = new JoystickButton(xbox, 5); // LB
        // UNUSED               = new JoystickButton(xbox, 6); // RB
        cargoConfig             = new JoystickButton(xbox, 7); // BACK
        hatchConfig             = new JoystickButton(xbox, 8); // START

    }

    public void registerButtons() {
        whileHeldCommand(intakeFlywheelIntake, new IntakeFlywheelCommand(true));
        whileHeldCommand(intakeFlywheelExpel, new IntakeFlywheelCommand(false));
        whileHeldCommand(armManualToggle, new ArmManualCommand());
        whenPressedCommand(manipExpel_left, new ManipExpelCommand());
        whenPressedCommand(manipExpel_right, new ManipExpelCommand());
        whenPressedCommand(armIncreaseLevel, new IncreaseArmCommand());
        whenPressedCommand(armDecreaseLevel, new DecreaseArmCommand());
        whenPressedCommand(climbEngageClutch, new ClimbEngageClutchCommand());
        whenPressedCommand(intakeExtend, new IntakeExtendCommand());
        whenPressedCommand(armSetLevelHighest, new SetArmLevelCommand(2));
        whenPressedCommand(armSetLevelLowest, new SetArmLevelCommand(1));
        //whenPressedCommand(visionAlignment, new VisionAlignmentCommand());
        whenPressedCommand(hatchConfig, new SetPlacementMode(PlacementMode.HATCH));
        whenPressedCommand(cargoConfig, new SetPlacementMode(PlacementMode.CARGO));
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

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.arm.DecreaseAWLevelCommand;
import frc.robot.arm.IncreaseAWLevelCommand;
import frc.robot.arm.SetAWLevelCommand;
import frc.robot.arm.SetAWLevelCommand.AWLevelMode;
import frc.robot.drive.DrivePrecisionToggleCommand;
import frc.robot.manipulator.SetPlacementMode;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;
public class VisionAlignmentCommand extends CommandGroup {

    public VisionAlignmentCommand() {
        //addSequential(new SetPlacementMode(PlacementMode.CARGO));
        addSequential(new SetAWLevelCommand(AWLevelMode.HIGHEST));
        addSequential(new VisionTurnCommand());
        //addSequential(new SetAWLevelCommand(AWLevelMode.LOWEST));
        //addSequential(new SetAWLevelCommand(AWLevelMode.INDEX2));
        //addSequential(new UltrasonicFrontDriveCommand(30, 0.3, 2));
        //addSequential(new VisionTurnCommand());
        //addSequential(new UltrasonicFrontDriveCommand(20, 0.2));
    }

}
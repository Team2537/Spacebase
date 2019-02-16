package frc.robot.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.auto.VisionTurnCommand;
import frc.robot.auto.UltrasonicFrontDriveCommand;
public class VisionAlignmentCommand extends CommandGroup {

    public VisionAlignmentCommand() {
        addSequential(new VisionTurnCommand());
        addSequential(new UltrasonicFrontDriveCommand(6, 0.3));
    }

}
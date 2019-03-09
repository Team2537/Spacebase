package frc.robot.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
public class VisionAlignmentCommand extends CommandGroup {

    public VisionAlignmentCommand() {
        addSequential(new VisionTurnCommand());
        //addSequential(new UltrasonicFrontDriveCommand(30, 0.3, 2));
        addSequential(new VisionTurnCommand());
        //addSequential(new UltrasonicFrontDriveCommand(20, 0.2));
    }

}
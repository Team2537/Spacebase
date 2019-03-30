package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DrivePrecisionToggleCommand extends Command {

    @Override
    protected void initialize() {
        Robot.driveSys.toggleDrivePrecision();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
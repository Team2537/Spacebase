package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class WaitForWristCommand extends Command {

    @Override
    protected boolean isFinished() {
        return !Robot.wristSys.enabled() || Robot.wristSys.onTarget();
    }

}
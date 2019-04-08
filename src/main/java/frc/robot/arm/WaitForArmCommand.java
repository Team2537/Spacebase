package frc.robot.arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class WaitForArmCommand extends Command {

    @Override
    protected boolean isFinished() {
        return !Robot.armSys.enabled() || Robot.armSys.onTarget();
    }

}
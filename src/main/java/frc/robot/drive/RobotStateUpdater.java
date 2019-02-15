package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Specs;

public class RobotStateUpdater extends Command {

    double lastPosLeft, lastPosRight, lastAngle;

    @Override
    protected void initialize() {
        lastPosLeft = Robot.driveSys.getEncoderPosLeft();
        lastPosRight = Robot.driveSys.getEncoderPosRight();
        lastAngle = Robot.driveSys.getGyroRadians();
    }

    @Override
    protected void execute() {
        final double posLeft = Robot.driveSys.getEncoderPosLeft();
        final double posRight = Robot.driveSys.getEncoderPosRight();
        final double angle = Robot.driveSys.getGyroRadians();

        final double wheelDeltaLeft = posLeft - lastPosLeft;
        final double wheelDeltaRight = posRight - lastPosRight;
        final double angleDelta = angle - lastAngle;

        Robot.robotState.update(Specs.ROBOT_PERIOD_SECONDS, wheelDeltaLeft, wheelDeltaRight, angleDelta);

        lastPosLeft = posLeft;
        lastPosRight = posRight;
        lastAngle = angle;

        if(System.currentTimeMillis() % 500 == 0){
            System.out.println(Robot.robotState.getLatestState().pos);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
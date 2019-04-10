package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.PID;
import frc.robot.Robot;

public class DriveStraightCommand extends Command {

    public static final double kP = 0.015, kI = 0.0, kD = 0.0;
    public static final double TOLERANCE = 1;
    
    public final double PERCENT_OUTPUT;
    private PID pid;

    // ONLY RUN THIS AS A whileHeld COMMAND SO WE WON'T KILL ANYONE
    public DriveStraightCommand(double percentOutput) {
        requires(Robot.driveSys);
        pid = new PID(kP, kI, kD, TOLERANCE);
        PERCENT_OUTPUT = percentOutput;
    }

    @Override
    protected void initialize() {
        pid.setSetpoint(Robot.driveSys.getGyroDegrees());
    }

    @Override
    protected void execute() {
        pid.update(Robot.driveSys.getGyroDegrees());
        final double output = pid.getOutput();
        Robot.driveSys.setMotors(PERCENT_OUTPUT + output, PERCENT_OUTPUT - output);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.driveSys.setMotors(0.0, 0.0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
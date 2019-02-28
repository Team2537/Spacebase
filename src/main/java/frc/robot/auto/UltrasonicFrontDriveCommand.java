package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.PID;
import frc.robot.Robot;

//drives forward until Ultrasonic max distance reached

public class UltrasonicFrontDriveCommand extends Command {
  private double targetDistInches, startTime;
  private long timeout;
  private PID pid;
  public UltrasonicFrontDriveCommand(double targetDistInches, double percentOutput, double timeout) {
    requires(Robot.driveSys);
    pid = new PID(Math.abs(percentOutput)/30,0.005,0);
    pid.setSetpoint(-targetDistInches);
    this.targetDistInches = targetDistInches;
    this.timeout = (long)(1000*timeout);
  }

  public UltrasonicFrontDriveCommand(double targetDistInches, double percentOutput){
    this(targetDistInches, percentOutput, Long.MAX_VALUE);
  }

  @Override
  protected void initialize() {
    System.out.println("ULTRASONIC COMMAND STARTED!!!");
    startTime = System.currentTimeMillis();
  }

  @Override
    protected void execute(){
      pid.update(-Robot.driveSys.getUltrasonic());
      final double percentOutput = pid.getOutput();
      Robot.driveSys.setMotors(percentOutput, percentOutput);
     System.out.println(Robot.driveSys.getUltrasonic());
    }

  @Override
  protected boolean isFinished() {
    return (Robot.driveSys.getUltrasonic() <= targetDistInches ||
      System.currentTimeMillis() - startTime >= timeout);
  }

  @Override
  protected void end() {
    System.out.println("ULTRASONIC is DONE!!!");
    Robot.driveSys.setMotors(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}

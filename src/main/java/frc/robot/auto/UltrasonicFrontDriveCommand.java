package frc.robot.auto;
import frc.lib.util.PID;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

//drives forward until Ultrasonic max distance reached

public class UltrasonicFrontDriveCommand extends Command {
  private double targetDistInches, startTime;
  private double defaultPercentOutput;
  private long timeout;
  private PID pid;
  public UltrasonicFrontDriveCommand(double targetDistInches, double percentOutput, double timeout) {
    requires(Robot.driveSys);
    pid = new PID(percentOutput/30, 0.000005,0.000003); //I = 0.000007 //D controls oscillations
    pid.setSetpoint(-targetDistInches);
    this.targetDistInches = targetDistInches;
    this.timeout = (long)(1000*timeout);
    this.defaultPercentOutput = percentOutput;
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
      final double percentOutput;
      pid.update(-Robot.driveSys.getUltrasonic());
      if(Robot.driveSys.getUltrasonic() <= 35) { //gotta love arbitrary values
         percentOutput = pid.getOutput();
      } else {
        percentOutput = defaultPercentOutput;
      }
      
      Robot.driveSys.setMotors(percentOutput, percentOutput);
     System.out.println("ULTRA: " + Robot.driveSys.getUltrasonic());

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

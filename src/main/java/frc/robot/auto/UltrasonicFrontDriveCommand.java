package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

//drives forward until Ultrasonic max distance reached

public class UltrasonicFrontDriveCommand extends Command {
  private double percentOutput, targetDistInches;
  public UltrasonicFrontDriveCommand(double targetDistInches, double percentOutput) {
    requires(Robot.driveSys);
    this.percentOutput = Math.abs(percentOutput);
    this.targetDistInches = targetDistInches;
  }

  @Override
  protected void initialize() {
    Robot.driveSys.setMotors(percentOutput, -percentOutput);
    System.out.println("ULTRASONIC COMMAND STARTED!!!");
  }

  @Override
    protected void execute(){
     Robot.driveSys.getUltrasonic();
    }

  @Override
  protected boolean isFinished() {
    return Robot.driveSys.getUltrasonic() <= targetDistInches;
    //return false;
  }

  @Override
  protected void end() {
    System.out.println("2nd is DONE!!!");
    Robot.driveSys.setMotors(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}

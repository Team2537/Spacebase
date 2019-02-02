/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class PerpendicularOnLineCommand extends Command {

  private int stage;
  private boolean prediction; // false is left, true is right
  private boolean hasFlippedPrediction;
  private double turnAng;
  private double rotateSpeed, linearSpeed;

  public PerpendicularOnLineCommand(double linearSpeed, double rotateSpeed, boolean prediction) {
    requires(Robot.driveSys);
    this.linearSpeed = linearSpeed;
    this.rotateSpeed = rotateSpeed;
    this.prediction = prediction;
  }

  private void initMotors(){
    if(prediction){
      Robot.driveSys.setMotors(rotateSpeed, -rotateSpeed);
    } 
    else {
      Robot.driveSys.setMotors(-rotateSpeed, rotateSpeed);
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    hasFlippedPrediction = false;
    stage = 0;
    Robot.driveSys.resetGyro();

    initMotors();
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch(stage) {
      case 0:
        // Rotate until the upper sensor hits the line
        if(Math.abs(Robot.driveSys.getGyroDegrees()) >= 90){
          // we have gone 90 degrees and not hit the line at all, so flip the prediction
          if(!hasFlippedPrediction) {
            hasFlippedPrediction = true;
            prediction = !prediction;
            initMotors();
          } else if(Math.abs(Robot.driveSys.getGyroDegrees()) >= 180) {
            stage = 4;
          }
        }
        
        if(Robot.driveSys.getIR_frontUpper()) {
          Robot.driveSys.resetGyro();
          stage++;
        }
        break;


      case 1:
        // Rotate until the lower sensor hits the line
        if(Robot.driveSys.getIR_frontLower()) {
          double angle = Robot.driveSys.getGyroDegrees();
          turnAng = (90 - Math.abs(angle))*Math.signum(angle);
          Robot.driveSys.resetGyro();
          stage++;
        }
        break;


      case 2:
        // Rotate until we are perpendicular to the line
        double angle = Robot.driveSys.getGyroDegrees();
        if((prediction && angle <= turnAng) || (!prediction && angle >= turnAng)){
          Robot.driveSys.setMotors(linearSpeed, linearSpeed);
          stage++;
        }
        break;


      case 3:
        // Drive straight until we are on the line
        if(Robot.driveSys.getIR_center()){
          Robot.driveSys.setMotors(0, 0);
          stage++;
        }
        break;
    }
  }

  @Override
  protected boolean isFinished() {
    return stage > 3;
  }

  @Override
  protected void end() {
    Robot.driveSys.setMotors(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}

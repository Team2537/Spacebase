/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.lib.units.Distances;
import frc.lib.units.Times;
import frc.lib.units.Units;
import frc.robot.Ports;
import frc.robot.Specs;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class DriveSubsystem extends Subsystem {
  private static final MotorType MOTOR_TYPE = MotorType.kBrushless;
  private static final int[] MOTOR_PORTS_LEFT = {
    Ports.DRIVE_MOTOR_LEFT_FRONT,
    Ports.DRIVE_MOTOR_LEFT_TOP,
    Ports.DRIVE_MOTOR_LEFT_BACK
  };
  private static final int[] MOTOR_PORTS_RIGHT = {
    Ports.DRIVE_MOTOR_RIGHT_FRONT,
    Ports.DRIVE_MOTOR_RIGHT_TOP,
    Ports.DRIVE_MOTOR_RIGHT_BACK
  };

  private CANSparkMax[] motorsLeft, motorsRight;
  private CANEncoder[] encodersLeft, encodersRight;
  private double[] encoderVelWindowLeft, encoderVelWindowRight;
  private double accelLeft, accelRight;
  
  @SuppressWarnings("unused") private Subsystem accelUpdater;

  private static final int NUM_VELS_TO_SAMPLE = 4;

  public DriveSubsystem(){
    motorsLeft = new CANSparkMax[MOTOR_PORTS_LEFT.length];
    encodersLeft = new CANEncoder[MOTOR_PORTS_LEFT.length];
    for(int i = 0; i < MOTOR_PORTS_LEFT.length; i++){
      motorsLeft[i] = new CANSparkMax(MOTOR_PORTS_LEFT[i],  MOTOR_TYPE);
      encodersLeft[i] = motorsLeft[i].getEncoder();
    }

    motorsRight = new CANSparkMax[MOTOR_PORTS_RIGHT.length];
    encodersRight = new CANEncoder[MOTOR_PORTS_RIGHT.length];
    for(int i = 0; i < MOTOR_PORTS_RIGHT.length; i++){
      motorsRight[i] = new CANSparkMax(MOTOR_PORTS_RIGHT[i],  MOTOR_TYPE);
      encodersRight[i] = motorsRight[i].getEncoder();
    }

    encoderVelWindowLeft = new double[NUM_VELS_TO_SAMPLE];
    encoderVelWindowRight = new double[NUM_VELS_TO_SAMPLE];

    accelUpdater = new Subsystem(){
      protected void initDefaultCommand() {setDefaultCommand(new Command(){
          protected void execute() { updateAccels(); }
          protected boolean isFinished() { return false; }
        });
      }
    };
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveCommand());
  }

  private void setMotorsSide(double percentOutput, CANSparkMax[] motors){
    for(CANSparkMax motor : motors){
      motor.set(percentOutput);
    }
  }
  
  public void setMotorsLeft(double percentOutput){
    setMotorsSide(-percentOutput, motorsLeft);
  }

  public void setMotorsRight(double percentOutput){
    setMotorsSide(percentOutput, motorsRight);
  }

  public void setMotors(double percentOutputLeft, double percentOutputRight){
    setMotorsLeft(percentOutputLeft);
    setMotorsRight(percentOutputRight);
  }

  public String encoderStatus(){
    String out = "";
    for(int i = 0; i < encodersLeft.length; i++){
      out += "Left  Encoder "+i+": " + encodersLeft[i].getPosition() + "\n";
    }
    for(int i = 0; i < encodersRight.length; i++){
      out += "Right Encoder "+i+": " + encodersRight[i].getPosition() + "\n";
    }
    return out;
  }

  private static double averageWithoutZeroes(double[] samples){
    double total = 0;
    for(double val : samples){
      if(val != 0) {
        total += val;
      }
    }

    if(total == 0) {
      return 0;
    } else {
      return total / samples.length;
    }
  }
  
  private double getEncoderPos(CANEncoder[] encoders){
    double[] positions = new double[encoders.length];
    for(int i = 0; i < encoders.length; i++){
      positions[i] = encoders[i].getPosition();
    }
    double avg = averageWithoutZeroes(positions);
    return Units.convertDistance(avg, Distances.REVOLUTIONS, Distances.INCHES);
  }

  public double getEncoderPosRight(){
    return getEncoderPos(encodersRight);
  }

  public double getEncoderPosLeft(){
    return getEncoderPos(encodersLeft);
  }

  private double getEncoderVel(CANEncoder[] encoders){
    double[] vels = new double[encoders.length];
    for(int i = 0; i < encoders.length; i++){
      vels[i] = encoders[i].getVelocity();
    }
    double avg = averageWithoutZeroes(vels);
    return Units.convertSpeed(avg, Distances.REVOLUTIONS, Times.MINUTES, Distances.INCHES, Times.SECONDS);
  }

  public double getEncoderVelRight(){
    return getEncoderVel(encodersRight);
  }

  public double getEncoderVelLeft(){
    return getEncoderVel(encodersLeft);
  }

  protected void updateAccels(){
    final double velLeft = getEncoderVelLeft(), velRight = getEncoderVelRight();
    for(int i = 0; i < NUM_VELS_TO_SAMPLE - 1; i++){
      encoderVelWindowLeft[i+1] = encoderVelWindowLeft[i];
      encoderVelWindowRight[i+1] = encoderVelWindowRight[i];
    }
    encoderVelWindowLeft[0] = velLeft;
    encoderVelWindowRight[0] = velRight;
    
    final double dt = 4*Units.convertTime(Specs.WPILIB_CYCLE_TIME_MS, Times.MILLISECONDS, Times.SECONDS);
    accelLeft = (velLeft- encoderVelWindowLeft[NUM_VELS_TO_SAMPLE-1])/dt;
    accelRight = (velRight - encoderVelWindowRight[NUM_VELS_TO_SAMPLE-1])/dt;
  }

  public double getEncoderAccRight(){
    return accelRight;
  }

  public double getEncoderAccLeft(){
    return accelLeft;
  }

}


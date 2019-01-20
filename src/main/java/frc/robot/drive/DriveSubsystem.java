/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;

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
  
  private double getEncoderPos(CANEncoder[] encoders){
    double total = 0;
    double position;
    for(CANEncoder encoder : encoders){
      position = encoder.getPosition();
      if(position != 0) {
        total += position;
      }
    }

    if(total == 0) {
      return 0;
    } else {
      return total / encoders.length;
    }
  }

  public double getEncoderPosRight(){
    return getEncoderPos(encodersRight);
  }

  public double getEncoderPosLeft(){
    return getEncoderPos(encodersLeft);
  }

  
}


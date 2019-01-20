/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Ports;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
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
  private SpeedControllerGroup motorGroupLeft, motorGroupRight;
  private CANEncoder[] encodersLeft, encodersRight;
  private DifferentialDrive differentialDrive;
  private static Joystick leftJoystick, rightJoystick;

  public DriveSubsystem(){
    motorsLeft = new CANSparkMax[MOTOR_PORTS_LEFT.length];
    for(int i = 0; i < MOTOR_PORTS_LEFT.length; i++){
      motorsLeft[i] = new CANSparkMax(MOTOR_PORTS_LEFT[i],  MOTOR_TYPE);
      encodersLeft[i] = new CANEncoder(motorsLeft[i]);
    }
    motorGroupLeft = new SpeedControllerGroup(null, motorsLeft);

    motorsRight = new CANSparkMax[MOTOR_PORTS_RIGHT.length];
    for(int i = 0; i < MOTOR_PORTS_RIGHT.length; i++){
      motorsRight[i] = new CANSparkMax(MOTOR_PORTS_RIGHT[i],  MOTOR_TYPE);
      encodersRight[i] = new CANEncoder(motorsRight[i]);
    }
    motorGroupRight = new SpeedControllerGroup(null, motorsRight);

    differentialDrive = new DifferentialDrive(motorGroupLeft, motorGroupRight);

    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveCommand());
  }


  public void setMotors(double percentOutputLeft, double percentOutputRight){
    differentialDrive.tankDrive(percentOutputLeft, percentOutputRight);
  }

  public void setMotorsCurvature(double percentOutput, double curvature, boolean turningInPlace){
    differentialDrive.curvatureDrive(percentOutput, curvature, turningInPlace);
  }

  public double getLeftJoystick(){
    if (Math.abs(leftJoystick.getRawAxis(1)) >= 0.05){
      return leftJoystick.getRawAxis(1);
    }  else {
      return 0;
    }
    
  }

  public double getRightJoystick(){
    if (Math.abs(rightJoystick.getRawAxis(1)) >= 0.05) {
      return rightJoystick.getRawAxis(1);
    } else {
      return 0;
    }
    
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
    int numEncoders = 0;
    double position;
    for(CANEncoder encoder : encoders){
      position = encoder.getPosition();
      total += position;
      if(position != 0) {
        numEncoders++;
      }
    }

    if(numEncoders == 0) {
      return 0;
    } else {
      return total / numEncoders;
    }
  }

  public double getEncoderPosRight(){
    return getEncoderPos(encodersRight);
  }

  public double getEncoderPosLeft(){
    return getEncoderPos(encodersLeft);
  }

  
}


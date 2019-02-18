package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.Ports;

public class ArmSubsystem extends Subsystem {
    public static final IdleMode DEFAULT_MODE = IdleMode.kBrake;

    private int armLevel;
    private static final int ARM_LEVEL_MIN = 0, ARM_LEVEL_MAX = 6;
    private static final double[] armLevelSetpoints = 
        {0,33,50,82,97,145,164};
    private static final double[] wristLevelSetpoints =
        {0,-8,-35,-67,-82,-130,-35}; //TODO: fix the hell out of this

    private TalonSRX wristMotor;
    private CANSparkMax armMotor;
    private Encoder wristEncoder;
    private CANEncoder armEncoder;
    private Potentiometer armPot, wristPot;

    public ArmSubsystem() {
        armMotor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        setArmMode(DEFAULT_MODE);

        armEncoder = new CANEncoder(armMotor);
        wristMotor = new TalonSRX(Ports.WRIST_MOTOR);
        wristEncoder = new Encoder(1,0);
        wristPot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 1080, 0); //TODO: determine offset
        armPot = new AnalogPotentiometer(Ports.ARM_POTENTIOMETER, 1080, 0);
        armLevel = 0;
    }

    public void increaseArmLevel(){
        if(armLevel < ARM_LEVEL_MAX){
            armLevel++;
        }
    }

    public void setArmLevel(int level){
        if(armLevel < ARM_LEVEL_MAX && armLevel > ARM_LEVEL_MIN){
            armLevel = level;
        } else {
            System.out.println("error in Arm Subsystem method setArmLevel()");
            armLevel = 0;
        }
        
    }
    public void decreaseArmLevel(){
        if(armLevel > ARM_LEVEL_MIN){
            armLevel--;
        }
    }

    public double getArmSetpoint(){
        return armLevelSetpoints[armLevel];
    }

    public double getWristSetpoint(){
        return wristLevelSetpoints[armLevel];
    }

    public void setArmMotor(double percentOutput){
        armMotor.set(percentOutput);
    }

    public void setWristMotor(double percentOutput){
        wristMotor.set(ControlMode.PercentOutput, percentOutput);
    }

    public double getArmEncoder(){
        return armEncoder.getPosition();

    }

    // public double getWristEncoder() {
    //     System.out.println(wristEncoder.get());
    //     return wristEncoder.get();
    // }

    public double getWristPotentiometer(){
        return wristPot.get();
    }

    public double getArmPotentiometer(){
        return armPot.get();
    }

    public void setArmMode(CANSparkMax.IdleMode mode){
        armMotor.setIdleMode(mode);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand());
    }


}
package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.HumanInput;
import frc.robot.Ports;

public class ArmSubsystem extends Subsystem {

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
    private Potentiometer pot;

    public ArmSubsystem() {
        armMotor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        armEncoder = new CANEncoder(armMotor);
        wristMotor = new TalonSRX(Ports.WRIST_MOTOR);
        wristEncoder = new Encoder(1,0);
        pot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 3600, 0); //TODO: determine offset
        armLevel = 0;
        
    }

    public void increaseArmLevel(){
        if(armLevel < ARM_LEVEL_MAX){
            armLevel++;
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

    public double getPotentiometer(){
        return pot.get();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand());
    }


}
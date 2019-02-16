package frc.robot.arm;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.HumanInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;



public class ArmSubsystem extends Subsystem {

    private int armLevel;
    private static final int ARM_LEVEL_MIN = 0, ARM_LEVEL_MAX = 6;
    private static final double[] armLevelSetpoints = 
        {0,.09,.14,.23,.26,.4,.46};
    private static final double[] wristLevelSetpoints =
        {0,-8,-35,-67,-82,-130,-35};

    private CANSparkMax armMotor;
    private CANEncoder armEncoder;
    private TalonSRX wristMotor;
    private AnalogPotentiometer wristEncoder;

    public ArmSubsystem() {
        //TODO proper CAN port also potentiometer port
        armMotor = new CANSparkMax(6, MotorType.kBrushless);
        wristMotor = new TalonSRX(0);
        armEncoder = new CANEncoder(armMotor);
        wristEncoder = new AnalogPotentiometer(0,3600);
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
        wristMotor.set(ControlMode.Current, percentOutput);
    }

    public double getArmEncoder(){
        return armEncoder.getPosition();
    }

    public double getWristEncoder() {
        System.out.println(wristEncoder.get());
        return wristEncoder.get();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand());
    }

    public void registerButtons(){
        HumanInput.whenPressedCommand(HumanInput.increasearmbutton, new IncreaseArmCommand());
        HumanInput.whenPressedCommand(HumanInput.decreasearmbutton, new DecreaseArmCommand());
    }

}
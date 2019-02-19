package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.Ports;

public class ArmSubsystem extends Subsystem {
    public static final IdleMode DEFAULT_ARM_MODE = IdleMode.kBrake;
    public static final NeutralMode DEFAULT_WRIST_MODE = NeutralMode.Brake;

    private int armLevel;
    private static final int ARM_LEVEL_MIN = 0, ARM_LEVEL_MAX = 7;
    private static final double[] armLevelSetpoints = 
        {509, 487, 427, 462, 433, 419, 382, 367};
        //frame perimeter, low hatch level, ship cargo, cargo rocket 1, hatch 2, cargo rocket 2, hatch 3, cargo rocket 3
    private static final double[] wristLevelSetpoints =
        {735, 701, 592, 667, 633, 617, 569, 573}; //TODO: fix the hell out of this

    // intake setpoint: arm: 476, wrist: 488

    private TalonSRX wristMotor;
    private CANSparkMax armMotor;
    private Potentiometer armPot, wristPot;

    public ArmSubsystem() {
        armMotor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        setArmMode(DEFAULT_ARM_MODE);

        wristMotor = new TalonSRX(Ports.WRIST_MOTOR);
        setWristMode(DEFAULT_WRIST_MODE);

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
        wristMotor.set(ControlMode.PercentOutput, -percentOutput);
    }

    public double getWristPotentiometer(){
        return wristPot.get();
    }

    public double getArmPotentiometer(){
        return armPot.get();
    }

    public void setArmMode(IdleMode mode){
        armMotor.setIdleMode(mode);
    }

    public void setWristMode(NeutralMode mode){
        wristMotor.setNeutralMode(mode);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand());
    }


}
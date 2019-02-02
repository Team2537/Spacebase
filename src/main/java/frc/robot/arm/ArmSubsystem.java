package frc.robot.arm;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.HumanInput;

public class ArmSubsystem extends Subsystem {

    private int armLevel;
    private static final int ARM_LEVEL_MIN = 0, ARM_LEVEL_MAX = 6;
    private static final double[] armLevelSetpoints = 
        {0,33,50,82,97,145,164};
    private static final double[] wristLevelSetpoints =
        {0,-8,-35,-67,-82,-130,-35};

    private Talon armMotor, wristMotor;
    private Encoder armEncoder, wristEncoder;

    public ArmSubsystem() {
        armMotor = new Talon(2);
        wristMotor = new Talon(1);
        armEncoder = new Encoder(3,2);
        wristEncoder = new Encoder(1,0);
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
        wristMotor.set(percentOutput);
    }

    public double getArmEncoder(){
        return armEncoder.get();
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
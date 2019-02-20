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
import frc.robot.Robot;
import frc.robot.Specs;

public class ArmSubsystem extends Subsystem {
    public static final IdleMode DEFAULT_ARM_MODE = IdleMode.kBrake;
    public static final NeutralMode DEFAULT_WRIST_MODE = NeutralMode.Brake;

    private int armLevel;
    
    public static final ArmSetpoint 
        SETPOINT_INTAKE = new ArmSetpoint(469, 478),
        SETPOINT_DEFAULT = new ArmSetpoint(509, 735),
        SETPOINT_SHIP_HATCH = new ArmSetpoint(487, 701),
        SETPOINT_SHIP_CARGO = new ArmSetpoint(427, 592),
        SETPOINT_ROCKET_CARGO_1 = new ArmSetpoint(462, 667),
        SETPOINT_ROCKET_HATCH_2 = new ArmSetpoint(433, 633),
        SETPOINT_ROCKET_CARGO_2 = new ArmSetpoint(419, 617),
        SETPOINT_ROCKET_HATCH_3 = new ArmSetpoint(382, 569),
        SETPOINT_ROCKET_CARGO_3 = new ArmSetpoint(367, 573)
    ;

    public static final ArmSetpoint[] SETPOINTS_LEVELS = {
        SETPOINT_DEFAULT,            // frame perimeter
        SETPOINT_SHIP_HATCH,
        SETPOINT_SHIP_CARGO,
        SETPOINT_ROCKET_CARGO_1,
        SETPOINT_ROCKET_HATCH_2,
        SETPOINT_ROCKET_CARGO_2,
        SETPOINT_ROCKET_HATCH_3,
        SETPOINT_ROCKET_CARGO_3
    };
    private ArmSetpoint currentSetpoint;

    private TalonSRX wristMotor;
    private CANSparkMax armMotor;
    private Potentiometer armPot, wristPot;

    public ArmSubsystem() {
        armMotor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        setArmMode(DEFAULT_ARM_MODE);

        wristMotor = new TalonSRX(Ports.WRIST_MOTOR);
        setWristMode(DEFAULT_WRIST_MODE);

        wristPot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 1080, 0);
        armPot = new AnalogPotentiometer(Ports.ARM_POTENTIOMETER, 1080, 0);
        armLevel = 0;
        currentSetpoint = null;
    }

    public void setArmSetpoint(ArmSetpoint setpoint){
        // Safety feature: make sure nothing is inside intake before we go back to default position
        if(setpoint != SETPOINT_DEFAULT 
            || Robot.driveSys.getUltrasonic() >= Specs.FRONT_ULTRASONIC_TO_BALL){
            
            this.currentSetpoint = setpoint;
        }
    }

    public void setArmLevel(int level){
        if(level > SETPOINTS_LEVELS.length-1){
            level = SETPOINTS_LEVELS.length-1;
        }
        if(level < 0){
            level = 0;
        }
        armLevel = level;
        setArmSetpoint(SETPOINTS_LEVELS[level]);
    }

    public void increaseArmLevel(){
        setArmLevel(armLevel + 1);
    }

    public void decreaseArmLevel(){
        setArmLevel(armLevel - 1);
    }

    public ArmSetpoint getSetpoint(){
        return currentSetpoint;
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

    public void safetyNet(double min){
        if(getArmPotentiometer() < min){
            Robot.armSys.setArmMotor(-0.2);
        }
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand());
    }

    public static class ArmSetpoint {
        public final double arm, wrist;
        public ArmSetpoint(double arm, double wrist) {
            this.arm = arm;
            this.wrist = wrist;
        }
    }


}
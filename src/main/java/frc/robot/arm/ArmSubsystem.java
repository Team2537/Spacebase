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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.util.Util;
import frc.robot.Ports;
import frc.robot.Robot;
import frc.robot.Specs;

public class ArmSubsystem extends Subsystem {
    public static final IdleMode DEFAULT_ARM_MODE = IdleMode.kBrake;
    public static final NeutralMode DEFAULT_WRIST_MODE = NeutralMode.Brake;

    private boolean enableManual = true;


    private int armLevel;

    public static final double
        OFFSET_ARM = -54,
        OFFSET_WRIST = 559
    ;
    
    public static final ArmSetpoint 
        SETPOINT_INTAKE = new ArmSetpoint(469, 478, "INTAKE"),
        SETPOINT_DEFAULT = new ArmSetpoint(512, 735, "FRAME PERIMETER"),
        SETPOINT_SHIP_HATCH = new ArmSetpoint(487, 701, "SHIP HATCH"),
        SETPOINT_SHIP_CARGO = new ArmSetpoint(427, 592, "SHIP CARGO"),
        SETPOINT_ROCKET_CARGO_1 = new ArmSetpoint(462, 667, "LOW ROCKET CARGO"),
        SETPOINT_ROCKET_HATCH_2 = new ArmSetpoint(433, 633, "MID ROCKET HATCH"),
        SETPOINT_ROCKET_CARGO_2 = new ArmSetpoint(419, 617, "MID ROCKET CARGO"),
        SETPOINT_ROCKET_HATCH_3 = new ArmSetpoint(382, 569, "HIGH ROCKET HATCH"),
        SETPOINT_ROCKET_CARGO_3 = new ArmSetpoint(367, 573, "HIGH ROCKET CARGO")
    ;

    public static final ArmSetpoint[] HATCH_SETPOINTS_LEVELS = {
        SETPOINT_DEFAULT,            // frame perimeter
        SETPOINT_SHIP_HATCH,
        SETPOINT_ROCKET_HATCH_2,
        SETPOINT_ROCKET_HATCH_3,
    };

    public static final ArmSetpoint[] CARGO_SETPOINTS_LEVELS = {
        SETPOINT_INTAKE,
        SETPOINT_SHIP_CARGO,
        SETPOINT_ROCKET_CARGO_1,
        SETPOINT_ROCKET_CARGO_2,
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

        wristPot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 1080, OFFSET_WRIST);
        armPot = new AnalogPotentiometer(Ports.ARM_POTENTIOMETER, 1080, OFFSET_ARM);
        armLevel = 0;
        currentSetpoint = null;
    }

    public ArmSetpoint[] getArmArray(){
        if(Robot.manipSys.getArmConfiguration() == 0){
            return HATCH_SETPOINTS_LEVELS;
        } else {
            return CARGO_SETPOINTS_LEVELS;
        }
    }


    public void setArmSetpoint(ArmSetpoint setpoint){
        // Safety feature: make sure nothing is inside intake before we go back to default position
        //if(setpoint != SETPOINT_DEFAULT 
        //    || Robot.driveSys.getUltrasonic() >= Specs.FRONT_ULTRASONIC_TO_BALL){
            
            this.currentSetpoint = setpoint;
        //}
    }

    public void setArmLevel(int level){
        ArmSetpoint[] setpoints = getArmArray();

        if(level > setpoints.length-1){
            level = setpoints.length-1;
        }
        if(level < 0){
            level = 0;
        }
        armLevel = level;
        setArmSetpoint(setpoints[level]);
    }

    public void increaseArmLevel(){
        setArmLevel(armLevel + 1);
    }

    public void updateSmartDash(){
        final String name = currentSetpoint == null ? "NONE" : currentSetpoint.name;
        SmartDashboard.putString("Arm Level", name);
    }

    public void decreaseArmLevel(){
        setArmLevel(armLevel - 1);
    }

    public ArmSetpoint getSetpoint(){
        return currentSetpoint;
    }

    public void setArmMotor(double percentOutput){
        armMotor.set(percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-4)){
            setArmMode(IdleMode.kBrake);
        } else {
            setArmMode(IdleMode.kCoast);
        }
    }

    public void setWristMotor(double percentOutput){
        wristMotor.set(ControlMode.PercentOutput, -percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-4)){
            setWristMode(NeutralMode.Brake);
        } else {
            setWristMode(NeutralMode.Coast);
        }
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

    public boolean getManualEnabled(){
        return enableManual;
    }

    public void setManualEnabled(){
        enableManual = !enableManual;
        SmartDashboard.putBoolean("armEnabled", !enableManual);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmManualCommand());
    }

    public static class ArmSetpoint {
        public final double arm, wrist;
        public final String name;
        public ArmSetpoint(double arm, double wrist, String name) {
            this.arm = arm;
            this.wrist = wrist;
            this.name = name;
        }
    }


}
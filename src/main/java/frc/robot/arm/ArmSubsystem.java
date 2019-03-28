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
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class ArmSubsystem extends Subsystem {

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmCommand()); // TODO: Revert before comp
    }

    public static final IdleMode DEFAULT_ARM_IDLE_MODE = IdleMode.kBrake;
    public static final NeutralMode DEFAULT_WRIST_IDLE_MODE = NeutralMode.Brake;

    private boolean enableManual = true;


    private int armLevel;

    public static final double
        OFFSET_ARM = -50,
        OFFSET_WRIST = -112
    ;
    
    public static final ArmSetpoint 
        SETPOINT_DEFAULT = new ArmSetpoint(515, 743, "FRAME PERIMETER"),

        //SETPOINT_INTAKE = new ArmSetpoint(486, 503, "INTAKE"),

        SETPOINT_SHIP_HATCH = new ArmSetpoint(487, 701, "SHIP HATCH"),
        SETPOINT_ROCKET_HATCH_2 = new ArmSetpoint(442, 643, "MID ROCKET HATCH"),
        SETPOINT_ROCKET_HATCH_3 = new ArmSetpoint(390, 573, "HIGH ROCKET HATCH"),

        SETPOINT_SHIP_CARGO = new ArmSetpoint(435, 588, "SHIP CARGO"),
        SETPOINT_ROCKET_CARGO_1 = new ArmSetpoint(476, 694, "LOW ROCKET CARGO"),
        SETPOINT_ROCKET_CARGO_2 = new ArmSetpoint(430, 633, "MID ROCKET CARGO"),
        SETPOINT_ROCKET_CARGO_3 = new ArmSetpoint(376, 570, "HIGH ROCKET CARGO")
    ;

    public static final ArmSetpoint[] HATCH_SETPOINTS_LEVELS = {
        SETPOINT_DEFAULT,            // frame perimeter
        SETPOINT_SHIP_HATCH,
        SETPOINT_ROCKET_HATCH_2,
        SETPOINT_ROCKET_HATCH_3,
    };

    public static final ArmSetpoint[] CARGO_SETPOINTS_LEVELS = {
        //SETPOINT_INTAKE,
        SETPOINT_ROCKET_CARGO_1,
        SETPOINT_SHIP_CARGO,
        SETPOINT_ROCKET_CARGO_2,
        SETPOINT_ROCKET_CARGO_3
    };

    private ArmSetpoint currentSetpoint;

    private TalonSRX wristMotor;
    private CANSparkMax armMotor;
    private Potentiometer armPot, wristPot;

    public ArmSubsystem() {
        armMotor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        setArmIdleMode(DEFAULT_ARM_IDLE_MODE);

        wristMotor = new TalonSRX(Ports.WRIST_MOTOR);
        setWristIdleMode(DEFAULT_WRIST_IDLE_MODE);

        wristPot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 1080, OFFSET_WRIST);
        armPot = new AnalogPotentiometer(Ports.ARM_POTENTIOMETER, 1080, OFFSET_ARM);
        armLevel = 0;
        currentSetpoint = null;
    }

    public ArmSetpoint[] getCurrentSetpointArray(){
        if(Robot.manipSys.getPlacementMode() == PlacementMode.HATCH){
            return HATCH_SETPOINTS_LEVELS;
        } else {
            return CARGO_SETPOINTS_LEVELS;
        }
    }


    public void setSetpoint(ArmSetpoint setpoint){
        // Safety feature: make sure nothing is inside intake before we go back to default position
        
        //if(setpoint != SETPOINT_DEFAULT 
        //    || Robot.driveSys.getUltrasonic() >= Specs.FRONT_ULTRASONIC_TO_BALL){
            
            this.currentSetpoint = setpoint;
        //}
    }

    public void setLevel(int level){
        ArmSetpoint[] setpoints = getCurrentSetpointArray();

        if(level > setpoints.length-1){
            level = setpoints.length-1;
        }
        if(level < 0){
            level = 0;
        }
        armLevel = level;
        setSetpoint(setpoints[level]);
    }

    public void increaseLevel(){
        setLevel(armLevel + 1);
    }

    public String updateSmartDash(){
        return currentSetpoint == null ? "NONE" : currentSetpoint.name;
        
    }

    public void decreaseLevel(){
        setLevel(armLevel - 1);
    }

    public ArmSetpoint getSetpoint(){
        return currentSetpoint;
    }

    public void setArmMotor(double percentOutput){
        // Safety feature: don't let the arm go below the lowest potentiometer value
        if(getArmPotentiometer() >= SETPOINT_DEFAULT.arm) {
            // TODO: Revert this before comp
            //percentOutput = Math.max(percentOutput, 0);
        }

        armMotor.set(percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-2)){
            setArmIdleMode(IdleMode.kBrake);
        } else {
            setArmIdleMode(IdleMode.kCoast);
        }
    }

    public void setWristMotor(double percentOutput){
        wristMotor.set(ControlMode.PercentOutput, -percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-2)){
            setWristIdleMode(NeutralMode.Brake);
        } else {
            setWristIdleMode(NeutralMode.Coast);
        }
    }

    public double getWristPotentiometer(){
        return wristPot.get();
    }

    public double getWristAmperage(){
        return wristMotor.getOutputCurrent();
    }

    public double getArmPotentiometer(){
        return armPot.get();
    }

    public double getArmAmperage(){
        return armMotor.getOutputCurrent();
    }

    public void setArmIdleMode(IdleMode mode){
        armMotor.setIdleMode(mode);
    }

    public void setWristIdleMode(NeutralMode mode){
        wristMotor.setNeutralMode(mode);
    }

    public boolean getManualEnabled(){
        return enableManual;
    }

    public void setManualEnabled(){
        enableManual = !enableManual;
        SmartDashboard.putBoolean("armEnabled", !enableManual);
    }

    public static class ArmSetpoint {
        public final double arm, wrist;
        public final String name;
        public ArmSetpoint(double arm, double wrist, String name) {
            this.arm = arm;
            this.wrist = wrist;
            this.name = name;
        }
        public String toString(){
            return "Name: "+ name + ", Arm: "+arm+", Wrist: "+wrist;
        }
    }


}
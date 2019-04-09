package frc.robot.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.lib.util.Util;
import frc.robot.Ports;
import frc.robot.Specs;

public class ArmSubsystem extends PIDSubsystem {

    public static final double
        MIN = 0, MAX = 165,
        OFFSET = 565,
        kP = 0.01, kI = 0.00004, kD = 0.010,
        TOLERANCE = 1.5
    ;
    public static final IdleMode DEFAULT_IDLE_MODE = IdleMode.kBrake;

    private CANSparkMax motor;
    private Potentiometer pot;
    private boolean hasSetpoint;
    private boolean enabled;

    public ArmSubsystem(){
        super(kP, kI, kD, Specs.ROBOT_PERIOD_SECONDS);
        setOutputRange(-1.0, 1.0);
        setAbsoluteTolerance(TOLERANCE);

        pot = new AnalogPotentiometer(Ports.ARM_POTENTIOMETER, -1080, OFFSET);
        motor = new CANSparkMax(Ports.ARM_MOTOR, MotorType.kBrushless);
        setIdleMode(DEFAULT_IDLE_MODE);

        hasSetpoint = false;
    }

    public double getPotentiometer(){
        return pot.get();
    }

    public double getAmperage(){
        return motor.getOutputCurrent();
    }

    public void setMotor(double percentOutput){
        // Safety feature: don't let the arm go below the lowest potentiometer value
        // or above the highest potentiometer value
        if(getPotentiometer() <= MIN) {
            percentOutput = Math.max(percentOutput, 0);
        }
        if(getPotentiometer() >= MAX) {
            percentOutput = Math.min(percentOutput, 0);
        }

        motor.set(percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-2)){
            setIdleMode(IdleMode.kBrake);
        } else {
            setIdleMode(IdleMode.kCoast);
        }
    }

    public void setIdleMode(IdleMode mode){
        motor.setIdleMode(mode);
    }

    public String getIdleMode(){
        return motor.getIdleMode() == IdleMode.kBrake ? "Brake" : "Coast";
    }

    @Override
    public void enable(){
        if(hasSetpoint){
            getPIDController().reset();
            super.enable();
            enabled = true;
        }
    }

    @Override
    public void disable(){
        super.disable();
        enabled = false;
        setMotor(0);
    }

    public boolean enabled(){
        return enabled;
    }

    @Override
    public void setSetpoint(double setpoint){
        // Safety feature: make sure nothing is inside intake before we go back to default position
        //if(setpoint > ArmWristSetpoints.SETPOINT_DEFAULT.arm 
        //    || Robot.driveSys.getUltrasonic() >= Specs.FRONT_ULTRASONIC_TO_BALL){
            
            super.setSetpoint(Util.clamp(setpoint, MIN, MAX));
            hasSetpoint = true;
        //}
    }

    @Override
    protected double returnPIDInput() {
        return getPotentiometer();
    }

    @Override
    protected void usePIDOutput(double output) {
        if(onTarget()){
            output = 0;
        }
        setMotor(output);
    }

    @Override
    protected void initDefaultCommand() {

    }

}
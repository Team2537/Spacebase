package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.lib.util.Util;
import frc.robot.Ports;
import frc.robot.Specs;

public class WristSubsystem extends PIDSubsystem {

    public static final double
        MIN = 0, MAX = 258,
        OFFSET = -597,
        kP = 0.015, kI = 0.0004, kD = 0.002,
        TOLERANCE = 0.5
    ;
    public static final NeutralMode DEFAULT_IDLE_MODE = NeutralMode.Brake;

    private TalonSRX motor;
    private Potentiometer pot;
    private boolean hasSetpoint;
    private boolean enabled;

    public WristSubsystem(){
        super(kP, kI, kD, Specs.ROBOT_PERIOD_SECONDS);
        setOutputRange(-1.0, 1.0);
        setAbsoluteTolerance(TOLERANCE);

        pot = new AnalogPotentiometer(Ports.WRIST_POTENTIOMETER, 1080, OFFSET);
        motor = new TalonSRX(Ports.WRIST_MOTOR);
        setIdleMode(DEFAULT_IDLE_MODE);

        hasSetpoint = false;
        enabled = false;
    }

    public double getPotentiometer(){
        return pot.get();
    }

    public double getAmperage(){
        return motor.getOutputCurrent();
    }

    public void setMotor(double percentOutput){
        // Safety feature: don't let the wrist go below the lowest potentiometer value
        // or above the highest potentiometer value
        if(getPotentiometer() <= MIN) {
            percentOutput = Math.max(percentOutput, 0);
        }
        if(getPotentiometer() >= MAX) {
            percentOutput = Math.min(percentOutput, 0);
        }

        motor.set(ControlMode.PercentOutput, -percentOutput);
        if(Util.epsilonEquals(percentOutput, 0, 1e-2)){
            setIdleMode(NeutralMode.Brake);
        } else {
            setIdleMode(NeutralMode.Coast);
        }
    }

    public void setIdleMode(NeutralMode mode){
        motor.setNeutralMode(mode);
    }

    public void reset() {
        getPIDController().reset();
        enable();
    }

    @Override
    public void enable(){
        if(hasSetpoint){
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
        super.setSetpoint(Util.clamp(setpoint, MIN, MAX));
        hasSetpoint = true;
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
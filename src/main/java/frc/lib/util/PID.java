package frc.lib.util;

public class PID {
    private double error, errorSum, actualPrev;
    private double setpoint, output;
    private double kP, kI, kD;
    private Double tolerance;

    public PID(double kP, double kI, double kD, Double tolerance){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.tolerance = tolerance == null ? null : Math.abs(tolerance);
    }

    public PID(double kP, double kI, double kD){
        this(kP, kI, kD, null);
    }

    public void setSetpoint(double setpoint){
        if(this.setpoint != setpoint){
            this.setpoint = setpoint;
            errorSum = 0;
        }
    }

    public void update(double actual){
        error = setpoint - actual;
        final double actualChange = actual - actualPrev;
        actualPrev = actual;

        if(tolerance == null || !withinTolerance()){
            errorSum += error;
            output = kP*error + kI*errorSum - kD*actualChange;
        } else {
            errorSum = 0;
            output = 0;
        }

    }

    public void update(double actual, double feedForward){
        update(actual);
        output += feedForward;
    }

    public double getOutput(){
        return output;
    }

    public boolean withinTolerance(){
        return Math.abs(error) < tolerance;
    }
}
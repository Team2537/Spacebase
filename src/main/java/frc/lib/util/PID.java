package frc.lib.util;

public class PID {
    private double error, errorSum, actualPrev;
    private double setpoint, output;
    private double kP, kI, kD;

    public PID(double kP, double kI, double kD){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setSetpoint(double setpoint){
        if(this.setpoint != setpoint){
            this.setpoint = setpoint;
            errorSum = 0;
        }
    }

    public void update(double actual){
        error = setpoint - actual;
        errorSum += error;
        final double actualChange = actual - actualPrev;
        actualPrev = actual;

        output = kP*error + kI*errorSum - kD*actualChange;
    }

    public void update(double actual, double feedForward){
        update(actual);
        output += feedForward;
    }

    public double getOutput(){
        return output;
    }

    public boolean withinTolerance(double tolerance){
        return Math.abs(error) < tolerance;
    }
}
package frc.lib.pathing;

public class RobotConstraints {
    public final double maxWheelVel, maxWheelAcc, length, dt;
    public RobotConstraints(double maxWheelVel, double maxWheelAcc, double length, double dt){
        this.maxWheelVel = maxWheelVel;
        this.maxWheelAcc = maxWheelAcc;
        this.length = length;
        this.dt = dt;
    }
}
package frc.lib.motion;

public class RobotConstraints {
    public final double maxWheelVel, maxWheelAcc, length;
    public RobotConstraints(double maxWheelVel, double maxWheelAcc, double length){
        this.maxWheelVel = maxWheelVel;
        this.maxWheelAcc = maxWheelAcc;
        this.length = length;
    }
}
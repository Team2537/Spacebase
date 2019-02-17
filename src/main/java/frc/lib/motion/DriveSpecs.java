package frc.lib.motion;

public class DriveSpecs {
    public final double wheelAxleLength;
    public final double wheelDiameter;
    public final double voltage_kV, voltage_kA, voltage_min;
    
    public DriveSpecs(double wheelAxleLength, double wheelDiameter, double voltage_kA, double voltage_kV, double voltage_min){
        this.wheelAxleLength = wheelAxleLength;
        this.wheelDiameter = wheelDiameter;
        this.voltage_kA = voltage_kA;
        this.voltage_kV = voltage_kV;
        this.voltage_min = voltage_min;
    }

    public DriveSpecs(double wheelAxleLength, double wheelDiameter){
        this(wheelAxleLength, wheelDiameter, 0,0,0);
    }

    public ChassisState toChassis(WheelState wheels){
        return new ChassisState(
            (wheels.right + wheels.left)/2,
            (wheels.right - wheels.left)/wheelAxleLength
        );
    }

    public WheelState toWheels(ChassisState chassis){
        return new WheelState(
            (chassis.linear - chassis.angular*wheelAxleLength/2),
            (chassis.linear + chassis.angular*wheelAxleLength/2)
        );
    }

    public double[] encode(){
        return new double[]{
            wheelAxleLength, wheelDiameter,
            voltage_kA, voltage_kV, voltage_min
        };
    }

    public static DriveSpecs decode(double[] p){
        return new DriveSpecs(
            p[0], p[1],
            p[2], p[3], p[4]
        );
    }
}
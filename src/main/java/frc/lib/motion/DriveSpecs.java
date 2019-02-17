package frc.lib.motion;

public class DriveSpecs {
    public final double wheelAxleLength;
    public final double wheelDiameter;
    public final VoltageParameters voltageParams;
    
    public DriveSpecs(double wheelAxleLength, double wheelDiameter, VoltageParameters voltageParams){
        this.wheelAxleLength = wheelAxleLength;
        this.wheelDiameter = wheelDiameter;
        this.voltageParams = voltageParams;
    }

    public DriveSpecs(double wheelAxleLength, double wheelDiameter){
        this(wheelAxleLength, wheelDiameter, new VoltageParameters(0,0,0));
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
            voltageParams.kV, voltageParams.kA, voltageParams.min
        };
    }

    public static DriveSpecs decode(double[] p){
        return new DriveSpecs(
            p[0], p[1],
            new VoltageParameters(p[2], p[3], p[4])
        );
    }

    public static class VoltageParameters {
        public final double kV, kA, min;
        public VoltageParameters(double kV, double kA, double min){
            this.kV = kV;
            this.kA = kA;
            this.min = min;
        }
    }
}
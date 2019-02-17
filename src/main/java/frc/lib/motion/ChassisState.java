package frc.lib.motion;

import java.text.DecimalFormat;

// Can refer to velocity or acceleration depending on context.
public class ChassisState {
    public final double linear;
    public final double angular;

    public ChassisState(double linear, double angular) {
        this.linear = linear;
        this.angular = angular;
    }

    public ChassisState() {
        this.linear = 0;
        this.angular = 0;
    }

    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat("#0.000");
        return fmt.format(linear) + ", " + fmt.format(angular);
    }
}
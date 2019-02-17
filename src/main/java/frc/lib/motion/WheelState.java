package frc.lib.motion;

import java.text.DecimalFormat;

// Can refer to velocity, acceleration, voltage, etc., depending on context.
public class WheelState {

    public final double left, right;

    public WheelState(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public WheelState() {
        this.left = 0;
        this.right = 0;
    }

    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat("#0.000");
        return fmt.format(left) + ", " + fmt.format(right);
    }
}
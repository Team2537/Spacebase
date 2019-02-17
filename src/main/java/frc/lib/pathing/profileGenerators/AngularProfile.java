package frc.lib.pathing.profileGenerators;

import frc.lib.motion.MotionProfile;
import frc.lib.motion.WheelState;

public class AngularProfile {

    public static void generate(MotionProfile profile, double dTheta, double robotLength, double accMax, double velMax){
        final double o = Math.signum(dTheta);
        final double dist = Math.abs(dTheta);
        final double accMaxLinear = accMax;
        accMax *= 2/robotLength;
        velMax *= 2/robotLength;

        // What is the maximum velocity we can reach (Vmax)? This is the intersection of two curves: one accelerating
        // towards the goal from profile.finalState(), the other coming from the goal at max vel (in reverse). If Vmax
        // is greater than constraints.max_abs_vel, we will clamp and cruise.
        // Solve the following three equations to find Vmax (by substitution):
        // Vmax^2 = Vstart^2 + 2*a*d_accel
        // Vgoal^2 = Vmax^2 - 2*a*d_decel
        // delta_pos = d_accel + d_decel
        final double v_max = Math.min(velMax, Math.sqrt(dist*accMax));
        double v_now = 0, pos_now = 0;

        // Accelerate to v_max
        final double accel_time = (v_max - v_now)/accMax;
        profile.appendControlWheels(new WheelState(-o*accMaxLinear, o*accMaxLinear), accel_time);
        pos_now += 0.5*accMax*accel_time*accel_time + v_now*accel_time;
        v_now += accMax*accel_time;

        // Figure out how much distance will be covered during deceleration.
        final double distance_decel = Math.max(0, v_now*v_now / (2*accMax));
        final double distance_cruise = Math.max(0, dist - pos_now - distance_decel);
        // Cruise at constant velocity.
        if (distance_cruise > 0) {
            final double cruise_time = distance_cruise / v_now;
            profile.appendControlWheels(new WheelState(), cruise_time);
        }
        // Decelerate to goal velocity.
        if (distance_decel > 0) {
            final double decel_time = v_now / accMax;
            profile.appendControlWheels(new WheelState(o*accMaxLinear, -o*accMaxLinear), decel_time);
        }
    }

}
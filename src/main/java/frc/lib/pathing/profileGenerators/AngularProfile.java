package frc.lib.pathing.profileGenerators;

import frc.lib.motion.MotionProfile;
import frc.lib.pathing.Clothoid;

public class AngularProfile {

    public static void generate(MotionProfile profile, Clothoid segment, double robotLength, double accMax, double velMax){
        final double o = Math.signum(segment.dTheta);
        final double dist = Math.abs(segment.dTheta);
        final double accMaxLinear = accMax;
        accMax /= robotLength;
        velMax /= robotLength;

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
        profile.appendControlWheels(accel_time, -o*accMaxLinear, o*accMaxLinear);
        pos_now += 0.5*accMax*accel_time*accel_time + v_now*accel_time;
        v_now += accMax*accel_time;

        // Figure out how much distance will be covered during deceleration.
        final double distance_decel = Math.max(0, v_now*v_now / (2*accMax));
        final double distance_cruise = Math.max(0, dist - pos_now - distance_decel);
        // Cruise at constant velocity.
        if (distance_cruise > 0) {
            final double cruise_time = distance_cruise / v_now;
            profile.appendControlWheels(cruise_time, 0, 0);
        }
        // Decelerate to goal velocity.
        if (distance_decel > 0) {
            final double decel_time = v_now / accMax;
            profile.appendControlWheels(decel_time, o*accMaxLinear, -o*accMaxLinear);
        }
    }

}
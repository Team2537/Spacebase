package frc.lib.pathing.profileGenerators;

import frc.lib.motion.MotionProfile;

public class LinearProfile {

    public static void generate(MotionProfile profile, double dist, double accMax, double velMax, double vel0, double velF){

        // What is the maximum velocity we can reach (Vmax)? This is the intersection of two curves: one accelerating
        // towards the goal from profile.finalState(), the other coming from the goal at max vel (in reverse). If Vmax
        // is greater than constraints.max_abs_vel, we will clamp and cruise.
        // Solve the following three equations to find Vmax (by substitution):
        // Vmax^2 = Vstart^2 + 2*a*d_accel
        // Vgoal^2 = Vmax^2 - 2*a*d_decel
        // delta_pos = d_accel + d_decel
        final double v_max = Math.min(velMax,
                Math.sqrt((vel0*vel0 + velF*velF) / 2.0 + dist*accMax));
        double v_now = vel0, pos_now = 0;

        // Accelerate to v_max
        if (v_max > vel0) {
            final double accel_time = (v_max - v_now)/accMax;
            profile.appendControlWheels(accel_time, accMax, accMax);
            pos_now += 0.5*accMax*accel_time*accel_time + v_now*accel_time;
            v_now += accMax*accel_time;
        }
        // Figure out how much distance will be covered during deceleration.
        final double distance_decel = Math.max(0, (v_now*v_now - velF*velF) / (2*accMax));
        final double distance_cruise = Math.max(0, dist - pos_now - distance_decel);
        // Cruise at constant velocity.
        if (distance_cruise > 0) {
            final double cruise_time = distance_cruise / v_now;
            profile.appendControlWheels(cruise_time, 0, 0);
        }
        // Decelerate to goal velocity.
        if (distance_decel > 0) {
            final double decel_time = (v_now - velF) / accMax;
            profile.appendControlWheels(decel_time, -accMax, -accMax);
        }
    }

}
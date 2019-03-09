package frc.lib.pathing;

import java.util.ArrayList;
import java.util.List;

import frc.lib.motion.ChassisState;
import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.Pose2d;
import frc.lib.motion.Pose2dCurved;
import frc.lib.util.Vec2;

public class MotionProfileGenerator {
    //public static Vec2[] ptstemp;
    public static void generate(MotionProfile profile, Waypoint[] waypoints, double diststep){
        Path path = new Path(waypoints);
        List<Pose2dCurved> states = path.split(diststep);

        final double kEpsilon = 1e-6;

        double accMax = 10;
        double velMax = 40;
        DriveSpecs constraint = profile.getDriveSpecs();
        List<ConstrainedState> constraint_states = new ArrayList<>(states.size());

        // Forward pass. We look at pairs of consecutive states, where the start state has already been velocity
        // parameterized (though we may adjust the velocity downwards during the backwards pass). We wish to find an
        // acceleration that is admissible at both the start and end state, as well as an admissible end velocity. If
        // there is no admissible end velocity or acceleration, we set the end velocity to the state's maximum allowed
        // velocity and will repair the acceleration during the backward pass (by slowing down the predecessor).
        ConstrainedState predecessor = new ConstrainedState();
        predecessor.state = states.get(0);
        predecessor.distance = 0.0;
        predecessor.max_velocity = 0;
        predecessor.min_acceleration = -accMax;
        predecessor.max_acceleration = accMax;
        for (int i = 0; i < states.size(); ++i) {
            // Add the new state.
            constraint_states.add(new ConstrainedState());
            ConstrainedState constraint_state = constraint_states.get(i);
            constraint_state.state = states.get(i);
            final double ds = constraint_state.state.vec.diff(predecessor.state.vec).mag();
            constraint_state.distance = ds + predecessor.distance;

            // We may need to iterate to find the maximum end velocity and common acceleration, since acceleration
            // limits may be a function of velocity.
            while (true) {
                // Enforce global max velocity and max reachable velocity by global acceleration limit.
                // vf = sqrt(vi^2 + 2*a*d)
                constraint_state.max_velocity = Math.min(velMax,
                        Math.sqrt(predecessor.max_velocity * predecessor.max_velocity
                                + 2.0 * predecessor.max_acceleration * ds));
                if (Double.isNaN(constraint_state.max_velocity)) {
                    throw new RuntimeException();
                }
                // Enforce global max absolute acceleration.
                constraint_state.min_acceleration = -accMax;
                constraint_state.max_acceleration = accMax;

                // At this point, the state is full constructed, but no constraints have been applied aside from
                // predecessor
                // state max accel.

                // Enforce all velocity constraints.
                constraint_state.max_velocity = Math.min(constraint_state.max_velocity, 
                    constraint.getMaxVelocity(constraint_state.state));

                if (constraint_state.max_velocity < 0.0) {
                    // This should never happen if constraints are well-behaved.
                    throw new RuntimeException();
                }

                // Now enforce all acceleration constraints.
                    final DriveSpecs.MinMaxAcceleration min_max_accel = constraint.getMinMaxAcceleration(
                            constraint_state.state, constraint_state.max_velocity);
                    if (!min_max_accel.valid()) {
                        // This should never happen if constraints are well-behaved.
                        throw new RuntimeException();
                    }
                    constraint_state.min_acceleration = Math.max(constraint_state.min_acceleration,
                        min_max_accel.min);
                    constraint_state.max_acceleration = Math.min(constraint_state.max_acceleration,
                        min_max_accel.max);
                
                if (constraint_state.min_acceleration > constraint_state.max_acceleration) {
                    // This should never happen if constraints are well-behaved.
                    throw new RuntimeException();
                }

                if (ds < kEpsilon) {
                    break;
                }
                // If the max acceleration for this constraint state is more conservative than what we had applied, we
                // need to reduce the max accel at the predecessor state and try again.
                // Simply using the new max acceleration is guaranteed to be valid, but may be too conservative.
                // Doing a search would be better.
                final double actual_acceleration = (constraint_state.max_velocity * constraint_state.max_velocity
                        - predecessor.max_velocity * predecessor.max_velocity) / (2.0 * ds);
                if (constraint_state.max_acceleration < actual_acceleration - kEpsilon) {
                    predecessor.max_acceleration = constraint_state.max_acceleration;
                } else {
                    if (actual_acceleration > predecessor.min_acceleration + kEpsilon) {
                        predecessor.max_acceleration = actual_acceleration;
                    }
                    // If actual acceleration is less than predecessor min accel, we will repair during the backward
                    // pass.
                    break;
                }
                // System.out.println("(intermediate) i: " + i + ", " + constraint_state.toString());
            }
            // System.out.println("i: " + i + ", " + constraint_state.toString());
            predecessor = constraint_state;
        }

        // Backward pass.
        ConstrainedState successor = new ConstrainedState();
        successor.state = states.get(states.size() - 1);
        successor.distance = constraint_states.get(states.size() - 1).distance;
        successor.max_velocity = 0;
        successor.min_acceleration = -accMax;
        successor.max_acceleration = accMax;
        for (int i = states.size() - 1; i >= 0; --i) {
            ConstrainedState constraint_state = constraint_states.get(i);
            final double ds = constraint_state.distance - successor.distance; // will be negative.

            while (true) {
                // Enforce reverse max reachable velocity limit.
                // vf = sqrt(vi^2 + 2*a*d), where vi = successor.
                final double new_max_velocity = Math.sqrt(successor.max_velocity * successor.max_velocity
                        + 2.0 * successor.min_acceleration * ds);
                if (new_max_velocity >= constraint_state.max_velocity) {
                    // No new limits to impose.
                    break;
                }
                constraint_state.max_velocity = new_max_velocity;
                if (Double.isNaN(constraint_state.max_velocity)) {
                    throw new RuntimeException();
                }

                final DriveSpecs.MinMaxAcceleration min_max_accel = constraint.getMinMaxAcceleration(
                        constraint_state.state, constraint_state.max_velocity);
                if (!min_max_accel.valid()) {
                    throw new RuntimeException();
                }
                constraint_state.min_acceleration = Math.max(constraint_state.min_acceleration,
                        min_max_accel.min);
                constraint_state.max_acceleration = Math.min(constraint_state.max_acceleration,
                        min_max_accel.max);

                if (constraint_state.min_acceleration > constraint_state.max_acceleration) {
                    throw new RuntimeException();
                }

                if (ds > kEpsilon) {
                    break;
                }
                // If the min acceleration for this constraint state is more conservative than what we have applied, we
                // need to reduce the min accel and try again.
                // Simply using the new min acceleration is guaranteed to be valid, but may be too conservative.
                // Doing a search would be better.
                final double actual_acceleration = (constraint_state.max_velocity * constraint_state.max_velocity
                        - successor.max_velocity * successor.max_velocity) / (2.0 * ds);
                if (constraint_state.min_acceleration > actual_acceleration + kEpsilon) {
                    successor.min_acceleration = constraint_state.min_acceleration;
                } else {
                    successor.min_acceleration = actual_acceleration;
                    break;
                }
            }
            successor = constraint_state;
        }

        /*
        ptstemp = new Vec2[states.size()];
        for(int i = 0; i < states.size(); i++){
            ptstemp[i] = states.get(i).vec;
        }*/

        // Integrate the constrained states forward in time to obtain the TimedStates.
        double t = 0.0, s = 0.0, v = 0.0, K = 0.0;
        for (int i = 1; i < states.size(); ++i) {
            final ConstrainedState constrained_state = constraint_states.get(i);
            // Advance t.
            final double ds = constrained_state.distance - s;
            final double accel = (constrained_state.max_velocity * constrained_state.max_velocity - v * v) / (2.0 * ds);
            double dt = 0.0;
            if (Math.abs(accel) > kEpsilon) {
                dt = (constrained_state.max_velocity - v) / accel;
            } else if (Math.abs(v) > kEpsilon) {
                dt = ds / v;
            } else {
                throw new RuntimeException();
            }

            if(dt == 0){
                continue;
            }
            final double angAccel = (constrained_state.max_velocity*constrained_state.state.curvature - v*K)/dt; 


            profile.appendControlChassis(new ChassisState(accel, angAccel), dt);
            t += dt;
            if (Double.isNaN(t) || Double.isInfinite(t)) {
                throw new RuntimeException();
            }

            v = constrained_state.max_velocity;
            s = constrained_state.distance;
            K = constrained_state.state.curvature;
        }
    }

    public static MotionProfile generate(DriveSpecs drive, Pose2d start,
            Waypoint[] waypoints, double diststep){

        MotionProfile profile = new MotionProfile(drive, start);
        generate(profile, waypoints, diststep);
        return profile;
    }

    private static class ConstrainedState {
        public Pose2dCurved state;
        public double distance;
        public double max_velocity;
        public double min_acceleration;
        public double max_acceleration;

        @Override
        public String toString() {
            return state.toString() + ", distance: " + distance + ", max_velocity: " + max_velocity + ", " +
                    "min_acceleration: " + min_acceleration + ", max_acceleration: " + max_acceleration;
        }
    }

}
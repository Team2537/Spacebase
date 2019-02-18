package frc.lib.pathing;

import frc.lib.motion.ChassisState;
import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.MotionState;
import frc.lib.motion.Pose2d;
import frc.lib.motion.RobotConstraints;
import frc.lib.motion.WheelState;
import frc.lib.pathing.MotionProfileGenerator.ProfileConstraints;
import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class PathFollower {
    private final double dt, timestep;
    private final Pose2d start;
    private final Waypoint[] waypoints;
    private final MotionProfile profile;
    private final DriveSpecs drive;
    
    private double timeElapsed;

    public PathFollower(Pose2d start, DriveSpecs drive, double dt, Waypoint[] waypoints, double profileTimestep){
        this.dt = dt;
        this.timestep = profileTimestep;
        this.waypoints = waypoints;
        this.drive = drive;
        this.start = start;

        // Find the optimal accMax and velMax        
        final double TOLERANCE = 1e-5;
        final double accMax = findOptimalAccMax(TOLERANCE);
        final double velMax = accMaxToVelMax(accMax);

        final ProfileConstraints constraints = new ProfileConstraints(accMax, velMax);
        this.profile = MotionProfileGenerator.generate(start, drive, waypoints, constraints, timestep);

        timeElapsed = 0;
    }

    public double accMaxToVelMax(double accMax){
        return (12 - drive.voltage_min - drive.voltage_kA*accMax)/drive.voltage_kV;
    }
    public double velMaxToAccMax(double velMax){
        return (12 - drive.voltage_min - drive.voltage_kV*velMax)/drive.voltage_kA;
    }

    private double calculateProfileTime(double accMax){
        double velMax = accMaxToVelMax(accMax);
        ProfileConstraints constraints = new ProfileConstraints(accMax, velMax);
        MotionProfile profile = MotionProfileGenerator.generate(start, drive, waypoints, constraints, timestep);
        return profile.dt();
    }

    private double findOptimalAccMax(double tol){
        final double invphi = (Math.sqrt(5) - 1) / 2; // 1/phi
        final double invphi2 = (3 - Math.sqrt(5)) / 2; // 1/phi^2

        double a = 0, b = velMaxToAccMax(0);
        double h = b - a;

        if(h <= tol) return (a+b)/2;

        // required steps to achieve tolerance
        final double n = (int)(Math.ceil(Math.log(tol/h)/Math.log(invphi)));

        double c = a + invphi2 * h;
        double d = a + invphi * h;
        double yc = calculateProfileTime(c);
        double yd = calculateProfileTime(d);

        for(int k = 0; k < n-1; k++){
            if (yc < yd) {
                b = d;
                d = c;
                yd = yc;
                h = invphi*h;
                c = a + invphi2 * h;
                yc = calculateProfileTime(c);
            } else {
                a = c;
                c = d;
                yc = yd;
                h = invphi*h;
                d = a + invphi * h;
                yd = calculateProfileTime(d);
            }
        }

        if (yc < yd) return (a+d)/2;
        else return (c+b)/2;
    }

    public void update(MotionState stateCurrent){
        MotionState stateSetpoint = profile.getState(timeElapsed);
        WheelState vels = stateCurrent.velWheels;
        WheelState accs = getNonlinearFeedback(stateSetpoint, stateCurrent);

        timeElapsed += dt;
    }

    private WheelState getNonlinearFeedback(MotionState state, MotionState stateCurrent) {
        // Based on Team 254's 2018 code
        // Implements eqn. 5.12 from https://www.dis.uniroma1.it/~labrob/pub/papers/Ramsete01.pdf
        final double kBeta = 0.00129;   // >0.
        final double kZeta = 0.7;       // Damping coefficient, [0, 1].

        // Compute gain parameter.
        final double k = 2.0 * kZeta * Math.sqrt(kBeta * state.vel.linear * state.vel.linear
                + state.vel.angular * state.vel.angular);

        // Compute error components.
        Pose2d error = state.pose.delta(stateCurrent.pose);
        final double sin_x_over_x = Util.epsilonEquals(error.ang, 0.0, 1E-2) ?
                1.0 : Math.sin(error.ang) / error.ang;

        final Vec2 dPos = error.vec.rotateBy(stateCurrent.pose.ang);

        final ChassisState adjustedVel = new ChassisState(
                state.vel.linear * Math.cos(error.ang) + k * dPos.x,
                state.vel.angular + k * error.ang + state.vel.linear * kBeta * sin_x_over_x * dPos.y
        );

        // Compute adjusted left and right wheel velocities.
        final WheelState adjustedVelWheels = drive.toWheels(adjustedVel);
        final WheelState adjustedAccWheels = new WheelState(
            (adjustedVelWheels.left - state.velWheels.left)/dt,
            (adjustedVelWheels.right - state.velWheels.right)/dt
        );
        return adjustedAccWheels;
    }
}
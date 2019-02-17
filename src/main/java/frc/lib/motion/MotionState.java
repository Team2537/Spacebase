package frc.lib.motion;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

import frc.lib.util.FresnelMath;

/** A MotionState represents the linear motion, angular motion, wheel states,
 * and curvature of the robot's drive train at a specific point in time.
 */
public class MotionState {    
    public final Pose2d pose;
    public final ChassisState vel, acc;
    public final WheelState velWheels, accWheels;
    public final double t;

    public MotionState( 
                    double t, 
                    Pose2d pose,
                    ChassisState vel, ChassisState acc,
                    WheelState velWheels, WheelState accWheels
                ){
        this.t = t;
        this.pose = pose;
        this.vel = vel;
        this.acc = acc;
        this.velWheels = velWheels;
        this.accWheels = accWheels;
    }

    public MotionState(Pose2d pose){
        this(0, pose, new ChassisState(), new ChassisState(), new WheelState(), new WheelState());
    }

    public MotionState(){
        this(new Pose2d());
    }

    /** @return a new MotionState, inferring wheel states and curvature
     * from known angular motion.
     */
    public static MotionState fromChassis(DriveSpecs drive,
                    double t, Pose2d pose, ChassisState vel, ChassisState acc){
        return new MotionState(t, pose, vel, acc, drive.toWheels(vel), drive.toWheels(acc));
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the linear and angular acceleration parameters.
     */
    public static MotionState controlChassis(MotionState old, DriveSpecs drive, ChassisState acc){
        return fromChassis(drive, old.t, old.pose, old.vel, acc);
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the linear and angular acceleration parameters.
     */
    public MotionState controlChassis(DriveSpecs drive, ChassisState acc){
        return controlChassis(this, drive, acc);
    }

    /** @return a new MotionState, inferring angular motion and curvature
     * from known wheel states.
     */
    public static MotionState fromWheels(DriveSpecs drive,
                double t, Pose2d pose, WheelState velWheels, WheelState accWheels){
        return new MotionState(t, pose, drive.toChassis(velWheels), drive.toChassis(accWheels), velWheels, accWheels);
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the wheel accelerations parameters.
     */
    public static MotionState controlWheels(MotionState old, DriveSpecs drive, WheelState accWheels){
        return fromWheels(drive, old.t, old.pose, old.velWheels, accWheels);
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the wheel accelerations parameters.
     */
    public MotionState controlWheels(DriveSpecs drive, WheelState accWheels){
        return controlWheels(this, drive, accWheels);
    }

    
    /** Calculates and returns the new MotionState of the robot after driving for some time, 
     * given its previous state and the change in time.
     * @param start The previous MotionState.
     * @param dt Amount of time elapsed between the old and new MotionStates.
     * @return The new MotionState.
     */
    public static MotionState forwardKinematics(MotionState start, DriveSpecs drive, double dt){
        if(Util.epsilonEquals(dt, 0)) return start;

        final ChassisState vel = new ChassisState(
            start.acc.linear*dt + start.vel.linear,
            start.acc.angular*dt + start.vel.angular
        );

        final double angle = 0.5*start.acc.angular*dt*dt + start.vel.angular*dt + start.pose.ang;

        /* To calculate the change in x and y based on the given wheel accelerations and
         * the previous state, we must integrate over the x- and y-velocity functions:
         * v_x(t) = v(t)*cos(angle(t))  and  v_y(t) = v(t)*sin(angle(t)) .
         */
        double dx, dy;
        if(Util.epsilonEquals(start.acc.linear, 0) && Util.epsilonEquals(start.vel.linear, 0)){
            // Turning in place
            dx = 0;
            dy = 0;
        } else {
            if(Util.epsilonEquals(start.acc.angular, 0)){
                if(Util.epsilonEquals(start.vel.angular, 0)){
                    // Integrating: (a*t + v_0)*cos(angle_0)
                    final double ds = 0.5*start.acc.linear*dt*dt + start.vel.linear*dt;
                    dx = ds*Math.cos(start.pose.ang);
                    dy = ds*Math.sin(start.pose.ang);
                } else {
                    // Integrating: (a*t + v_0)*cos(angVel_0*t + angle_0)
                    final double inv_av = 1/start.vel.angular;
                    final double one = inv_av*(start.acc.linear*dt + start.vel.linear), two = start.acc.linear*inv_av*inv_av;
                    final double sin = Math.sin(angle) - Math.sin(start.pose.ang);
                    final double cos = Math.cos(angle) - Math.cos(start.pose.ang);
                    dx = one*sin + two*cos;
                    dy = two*sin - one*cos;
                }
            } else {
                // Integrating: (a*t + v_0)*cos(0.5*angAcc*t^2 + angVel_0*t + angle_0)
                final double scalar = start.vel.linear - start.vel.angular*start.acc.linear/start.acc.angular;
                dx = (Math.sin(angle) - Math.sin(start.pose.ang))*start.acc.linear/start.acc.angular
                    + scalar*FresnelMath.integrateC(start.acc.angular, start.vel.angular, start.pose.ang, 0, dt);
                dy = -(Math.cos(angle) - Math.cos(start.pose.ang))*start.acc.linear/start.acc.angular
                    + scalar*FresnelMath.integrateS(start.acc.angular, start.vel.angular, start.pose.ang, 0, dt);
            }
        }

        final Vec2 vec = new Vec2(dx,dy).add(start.pose.vec);

        return MotionState.fromChassis(
            drive,
            start.t + dt,
            new Pose2d(vec,angle),
            vel, start.acc
        );
    }

    /** Calculates and returns the new MotionState of the robot after driving for some time, 
     * given its previous state and the change in time.
     * @param dt Amount of time elapsed between the old and new MotionStates.
     * @return The new MotionState.
     */
    public MotionState forwardKinematics(DriveSpecs drive, double dt){
        return MotionState.forwardKinematics(this, drive, dt);
    }

    public double[] encode(){
        return new double[]{ 
            t,
            pose.x, pose.y, pose.ang,
            vel.linear, vel.angular,
            acc.linear, acc.angular,
            velWheels.left, velWheels.right,
            accWheels.left, accWheels.right
        };
    }

    public static MotionState decode(double[] p){
        return new MotionState(
            p[0],
            new Pose2d(p[1],p[2],p[3]),
            new ChassisState(p[4],p[5]),
            new ChassisState(p[6],p[7]),
            new WheelState(p[8],p[9]),
            new WheelState(p[10],p[11])
        );
    }

}
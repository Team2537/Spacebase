package frc.lib.motion;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

import frc.lib.util.FresnelMath;

/** A MotionState represents the linear motion, angular motion, wheel states,
 * and curvature of the robot's drive train at a specific point in time.
 */
public class MotionState {    
    public final RobotConstraints constraints;
    public final Vec2 pos;
    public final double t, angle, angVel, angAcc, vel, acc, velL, velR, accL, accR, curvature, length;
    public final boolean fitsConstraints;

    public MotionState(RobotConstraints constraints, 
                    double t, 
                    Vec2 pos, double vel, double acc, 
                    double angle, double angVel, double angAcc,
                    double velL, double velR, double accL, double accR,
                    double curvature){
        this.constraints = constraints;
        this.length = constraints.length;
        this.t = t;
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
        this.angle = angle;
        this.angVel = angVel;
        this.angAcc = angAcc;
        this.velL = velL;
        this.velR = velR;
        this.accL = accL;
        this.accR = accR;
        this.curvature = curvature;
        fitsConstraints = (velL <= constraints.maxWheelVel) && (velR <= constraints.maxWheelVel)
                        && (accL <= constraints.maxWheelAcc) && (accR <= constraints.maxWheelAcc);
    }

    /** @return a new MotionState, inferring wheel states and curvature
     * from known angular motion.
     */
    public static MotionState fromAngular(RobotConstraints constraints,
                    double t,
                    Vec2 pos, double vel, double acc,
                    double angle, double angVel, double angAcc){
        return new MotionState(
            constraints,
            t,
            pos, vel, acc,
            angle, angVel, angAcc,
            vel - 0.5*angVel*constraints.length,    // left wheel velocity
            vel + 0.5*angVel*constraints.length,    // right wheel velocity
            acc - 0.5*angAcc*constraints.length,    // left wheel acceleration
            acc + 0.5*angAcc*constraints.length,    // right wheel acceleration
            angVel / vel                            // curvature
        );
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the linear and angular acceleration parameters.
     */
    public static MotionState controlAngular(MotionState old, double acc, double angAcc){
        return fromAngular(
            old.constraints,
            old.t,
            old.pos, old.vel, acc,
            old.angle, old.angVel, angAcc
        );
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the linear and angular acceleration parameters.
     */
    public MotionState controlAngular(double acc, double angAcc){
        return controlAngular(this, acc, angAcc);
    }

    /** @return a new MotionState, inferring angular motion and curvature
     * from known wheel states.
     */
    public static MotionState fromWheels(RobotConstraints constraints,
                        double t,
                        Vec2 pos, double angle,
                        double velL, double velR,
                        double accL, double accR){
        return new MotionState(
            constraints,
            t,
            pos, (velR+velL)/2, (accR+accL)/2,                  // linear velocity and acceleration
            angle, 
            (velR-velL)/constraints.length,                     // angular velocity
            (accR-accL)/constraints.length,                     // angular acceleration
            velL, velR, accL, accR,
            (2*(velR-velL))/(constraints.length*(velR+velL))    // curvature
        );
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the wheel accelerations parameters.
     */
    public static MotionState controlWheels(MotionState old, double accL, double accR){
        return fromWheels(
            old.constraints,
            old.t,
            old.pos, old.angle, 
            old.velL, old.velR, 
            accL, accR
        );
    }

    /** @return a new MotionState, replacing the old one's accelerations by using
     * the wheel accelerations parameters.
     */
    public MotionState controlWheels(double accL, double accR){
        return controlWheels(this, accL, accR);
    }

    
    /** Calculates and returns the new MotionState of the robot after driving for some time, 
     * given its previous state and the change in time.
     * @param start The previous MotionState.
     * @param dt Amount of time elapsed between the old and new MotionStates.
     * @return The new MotionState.
     */
    public static MotionState forwardKinematics(MotionState start, double dt){
        if(Util.epsilonEquals(dt, 0)) return start;

        final double vel = start.acc*dt + start.vel, angVel = start.angAcc*dt + start.angVel;
        final double angle = 0.5*start.angAcc*dt*dt + start.angVel*dt + start.angle;

        /* To calculate the change in x and y based on the given wheel accelerations and
         * the previous state, we must integrate over the x- and y-velocity functions:
         * v_x(t) = v(t)*cos(angle(t))  and  v_y(t) = v(t)*sin(angle(t)) .
         */
        double dx, dy;
        if(Util.epsilonEquals(start.acc, 0) && Util.epsilonEquals(start.vel, 0)){
            // Turning in place
            dx = 0;
            dy = 0;
        } else {
            if(Util.epsilonEquals(start.angAcc, 0)){
                if(Util.epsilonEquals(start.angVel, 0)){
                    // Integrating: (a*t + v_0)*cos(angle_0)
                    final double ds = 0.5*start.acc*dt*dt + start.vel*dt;
                    dx = ds*Math.cos(start.angle);
                    dy = ds*Math.sin(start.angle);
                } else {
                    // Integrating: (a*t + v_0)*cos(angVel_0*t + angle_0)
                    final double inv_av = 1/start.angVel;
                    final double one = inv_av*(start.acc*dt + start.vel), two = start.acc*inv_av*inv_av;
                    final double sin = Math.sin(angle) - Math.sin(start.angle);
                    final double cos = Math.cos(angle) - Math.cos(start.angle);
                    dx = one*sin + two*cos;
                    dy = two*sin - one*cos;
                }
            } else {
                // Integrating: (a*t + v_0)*cos(0.5*angAcc*t^2 + angVel_0*t + angle_0)
                final double scalar = start.vel - start.angVel*start.acc/start.angAcc;
                dx = (Math.sin(angle) - Math.sin(start.angle))*start.acc/start.angAcc
                    + scalar*FresnelMath.integrateC(start.angAcc, start.angVel, start.angle, 0, dt);
                dy = -(Math.cos(angle) - Math.cos(start.angle))*start.acc/start.angAcc
                    + scalar*FresnelMath.integrateS(start.angAcc, start.angVel, start.angle, 0, dt);
            }
        }

        Vec2 pos = new Vec2(dx,dy).add(start.pos);

        return MotionState.fromAngular(
            start.constraints,
            start.t + dt,
            pos, vel, start.acc,
            angle, angVel, start.angAcc
        );
    }

    /** Calculates and returns the new MotionState of the robot after driving for some time, 
     * given its previous state and the change in time.
     * @param dt Amount of time elapsed between the old and new MotionStates.
     * @return The new MotionState.
     */
    public MotionState forwardKinematics(double dt){
        return MotionState.forwardKinematics(this, dt);
    }

    public double[] encode(){
        return new double[]{
            constraints.maxWheelVel, constraints.maxWheelAcc, constraints.length, 
            t, pos.x, pos.y, angle, velL, velR, accL, accR
        };
    }

    public static MotionState decode(double[] vals){
        return MotionState.fromWheels(new RobotConstraints(vals[0], vals[1], vals[2]),
            vals[3], new Vec2(vals[4], vals[5]), vals[6], vals[7], vals[8], vals[9], vals[10]);
    }

}
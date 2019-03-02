package frc.lib.pathing;

import frc.lib.motion.Pose2d;
import frc.lib.util.FresnelMath;
import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class Clothoid {
    public final double length, Kp;
    public final Pose2d delta;
    public final boolean flipped;

    /**
    * @param startAngle Initial angle of the robot
    * @param turningRadius Distance away from the path node for this turn (essentially turning radius)
    * @param nodeAngle Angle between the current path segment and the next path segment
    */
    public Clothoid(double turningRadius, double nodeAngle){
        // generate clothoid
        // based on https://arxiv.org/pdf/1209.0910.pdf
        final double dir = Math.signum(nodeAngle);
        nodeAngle = Math.abs(nodeAngle)/2;
        final double dTheta_abs = Math.PI/2 - nodeAngle;
        final double dPhi = FresnelMath.integrate(2*dTheta_abs, 0, 0, 0,1).angle();
        final double r = turningRadius*Math.sin(nodeAngle)/Math.sin(nodeAngle + dPhi);
        length = r/FresnelMath.integrateC(-2*dTheta_abs, 0, dPhi, 0,1);

        // calculate change in curvature with respect to distance traveled along the path
        final double dTheta = dir*dTheta_abs;
        Kp = (2*dTheta)/(length*length);
        flipped = false;
        delta = new Pose2d(FresnelMath.integrate(Kp, 0, 0, 0, length), dTheta);
    }

    private Clothoid(double length, double Kp, Pose2d delta, boolean flipped){
        this.length = length;
        this.Kp = Kp;
        this.delta = delta;
        this.flipped = flipped;
    }  

    public Clothoid flip(){
        return new Clothoid(length, Kp, delta, true);
    }

    public static Clothoid fromAngle(double dTheta){
        return new Clothoid(0, Double.POSITIVE_INFINITY, new Pose2d(new Vec2(0,0), Util.normalizeHeadingRadians(dTheta)), false);
    }

    public static Clothoid fromLength(double length){
        return new Clothoid(length, 0, new Pose2d(new Vec2(length, 0), 0), false);
    }

    public double getMaxStartVelocity(double accMax, double robotLength){
        return Math.sqrt((2*accMax)/(Math.abs(Kp)*robotLength));
    }

    public String toString(){
        return String.format("Length: %f\nKp: %f\ndTheta: %f\nFlipped: %s\n",
            length, Kp, delta, flipped ? "yes" : "no");
    }

    public Pose2d getPose(Pose2d start, double s){
        if(s < 0 || s > length) return null;

        final Vec2 pos = FresnelMath.integrate(Kp, 0, 0, 0, s);
        final double ang = Math.tan(0.5*Kp*s*s);
        Pose2d deltaPartial = new Pose2d(pos,ang);
        if(flipped){
            deltaPartial = delta.delta(deltaPartial);
        }

        return start.addDelta(deltaPartial);
    }
}
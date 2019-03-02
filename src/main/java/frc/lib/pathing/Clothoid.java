package frc.lib.pathing;

import frc.lib.motion.Pose2d;
import frc.lib.motion.Pose2dCurved;
import frc.lib.util.FresnelMath;
import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class Clothoid {
    public final double length, Kp, K0, dTheta;

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
        dTheta = dir*dTheta_abs;
        Kp = (2*dTheta)/(length*length);
        K0 = 0;
    }

    private Clothoid(double length, double dTheta, double K0, double Kp){
        this.length = length;
        this.dTheta = dTheta;
        this.K0 = K0;
        this.Kp = Kp;
    }  

    public Clothoid flip(){
        return new Clothoid(
            length,
            dTheta, // TODO: correct this even though it's unused
            Kp*length, 
            -Kp
        );
    }

    public static Clothoid fromAngle(double dTheta){
        return new Clothoid(0, 0, Double.POSITIVE_INFINITY, Util.normalizeHeadingRadians(dTheta));
    }

    public static Clothoid fromLength(double length){
        return new Clothoid(length, 0, 0, 0);
    }

    public double getMaxStartVelocity(double accMax, double robotLength){
        return Math.sqrt((2*accMax)/(Math.abs(Kp)*robotLength));
    }

    public String toString(){
        return String.format("Length: %f\nKp: %f\ndTheta: %f\nFlipped: %s\n",
            length, Kp);
    }

    public Pose2dCurved getPose(Pose2d start, double s){
        if(s < 0 || s > length) return null;

        final double curvature = Kp*s + K0;
        final Vec2 pos = FresnelMath.integrate(Kp, K0, 0, 0, s);
        final double ang = 0.5*Kp*s*s + K0*s;
        final Pose2d deltaPartial = new Pose2d(pos,ang);

        return new Pose2dCurved(start.addDelta(deltaPartial), curvature);
    }
}
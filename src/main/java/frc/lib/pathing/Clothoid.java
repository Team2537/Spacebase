package frc.lib.pathing;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class Clothoid {
    public final double length, Kp, dTheta;
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
        dTheta = dir*dTheta_abs;
        Kp = (2*dTheta)/(length*length);
        flipped = false;
    }

    private Clothoid(double length, double Kp, double dTheta, boolean flipped){
        this.length = length;
        this.Kp = Kp;
        this.dTheta = dTheta;
        this.flipped = flipped;
    }  

    public Clothoid flip(){
        return new Clothoid(length, Kp, dTheta, true);
    }

    public static Clothoid fromAngle(double dTheta){
        return new Clothoid(0, Double.POSITIVE_INFINITY, Util.normalizeHeadingRadians(dTheta), false);
    }

    public static Clothoid fromLength(double length){
        return new Clothoid(length, 0, 0, false);
    }

    public double getMaxStartVelocity(double accMax, double robotLength){
        return Math.sqrt((2*accMax)/(Math.abs(Kp)*robotLength));
    }

    public String toString(){
        return String.format("Length: %f\nKp: %f\ndTheta: %f\nFlipped: %s\n",
            length, Kp, dTheta, flipped ? "yes" : "no");
    }
}
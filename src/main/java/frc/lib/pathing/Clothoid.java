package frc.lib.pathing;

import frc.lib.util.Vec2;

public class Clothoid {
    public final double length, Kp;

    /**
    * @param startAngle Initial angle of the robot
    * @param nodeDistance Distance away from the path node for this turn
    * @param nodeAngle Angle between the current path segment and the next path segment
    */
    public Clothoid(double startAngle, double nodeDistance, double nodeAngle){
        // generate clothoid
        // based on https://arxiv.org/pdf/1209.0910.pdf
        nodeAngle /= 2;
        final double dTheta = (Math.PI/2 - Math.abs(nodeAngle))*Math.signum(nodeAngle);
        final double dPhi = Vec2.angleBetween(
            new Vec2(startAngle),
            FresnelMath.integrate(2*dTheta, 0, 0, 0,1)
        );
        final double r = nodeDistance*Math.sin(nodeAngle)/Math.sin(nodeAngle + dPhi);
        length = r/FresnelMath.integrateC(-2*dTheta, 0, dPhi, 0,1);

        // calculate change in curvature with respect to distance traveled along the path
        Kp = Math.signum(nodeAngle)*(2*dTheta)/(length*length); 
    }

    public double getMaxStartVelocity(double accMax, double robotLength){
        return Math.sqrt((2*accMax)/(Kp*robotLength));
    }
}
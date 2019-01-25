package frc.lib.pathing;

import frc.lib.util.Vec2;

public class ClothoidGenerator3 {
    private static double getNodeRadius(double distanceToNode, double nodeAngle, double dPhi){
        return distanceToNode*Math.sin(dPhi)/Math.sin(dPhi+nodeAngle);
    }

    private static double getG(double nodeAngle, double dPhi){
        return ClothoidMath.integrateS(2*nodeAngle, 0, dPhi, 0,1);
    }
    private static double getH(double nodeAngle, double dPhi){
        return ClothoidMath.integrateC(2*nodeAngle, 0, dPhi, 0,1);
    }

    public static void buildClothoid(double startAngle, double nodeDistance, double nodeAngle){
        final double dTheta = Math.PI/2 - nodeAngle;
        final double dPhi = Vec2.angleBetween(
            new Vec2(startAngle),
            new Vec2(getH(dTheta,0), getG(dTheta,0))
        );

        final double r = nodeDistance*Math.sin(nodeAngle)/Math.sin(nodeAngle + dPhi);
        final double length = r/getH(-dTheta, dPhi);
        final double K0 = 0;
        final double Kp = (2*dTheta)/(length*length);
        System.out.println("Length: "+length);
        System.out.println("Curvature: "+K0);
        System.out.println("Change in curvature: "+Kp);
        System.out.println("dPhi: "+dPhi);
    }

    public static void main(String[] args){
        //System.out.println(getG(-0.5,3,5));
        buildClothoid(0, 4, Math.PI/12);
    }
}
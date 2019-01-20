package frc.lib.pathing;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class ClothoidGenerator {
    private static final double SQRT_HALF_PI = Math.sqrt(Math.PI/2);
    private static double getG(double A, double dTheta, double dPhi){
        if(Util.epsilonEquals(A, 0)){
            if(Util.epsilonEquals(dTheta, 0)){
                return (1 - Math.cos(dPhi))/dPhi;
            } else {
                return (Math.cos(dTheta) - Math.cos(dTheta + dPhi))/dTheta;
            }
        } else {
            final double sqrtA = Math.sqrt(Math.abs(A));
            final double o = Math.signum(A);
            final double trigInner = dPhi - Math.pow(dTheta - A, 2)/(4*A);
            final double fresUpper = (sqrtA + o*(dTheta - A)/(2*sqrtA))/SQRT_HALF_PI;
            final double fresLower = o*(dTheta - A)/(2*sqrtA*SQRT_HALF_PI);
            return 
            (
                Math.sin(trigInner)*(Util.fresnelC(fresUpper) - Util.fresnelC(fresLower))
            + o*Math.cos(trigInner)*(Util.fresnelS(fresUpper) - Util.fresnelS(fresLower))
            ) * SQRT_HALF_PI / sqrtA;        
        }
    }

    private static final double dA = 0.001;
    private static double findA(double Aguess, double dTheta, double dPhi, double tolerance){
        double A = Aguess;
        double G = getG(A, dTheta, dPhi);
        double GDerivative = (getG(A+dA, dTheta, dPhi) - getG(A-dA, dTheta, dPhi))/(2*dA);
        while (Math.abs(G) > tolerance){
            A -= G/GDerivative;
            G = getG(A, dTheta, dPhi);
            GDerivative = (getG(A+dA, dTheta, dPhi) - getG(A-dA, dTheta, dPhi))/(2*dA);
        }
        return A;
    }

    private static double normalizeAngle(double angle){
        while(angle > Math.PI) angle -= 2*Math.PI;
        while(angle < Math.PI) angle += 2*Math.PI;
        return angle;
    }

    public static final double TOLERANCE = 0.01;
    public static void buildClothoid(Vec2 start, double startAngle, Vec2 end, double endAngle){
        final Vec2 diff = end.diff(start);
        final double r = Math.hypot(diff.y, diff.x);
        final double phi = Math.atan2(diff.y, diff.x);
        final double dPhi = normalizeAngle(startAngle - phi);
        final double dTheta = normalizeAngle(endAngle - startAngle);
        final double A = findA(2.4674*dTheta + 5.2478*dPhi, dTheta, dPhi, TOLERANCE);
        System.out.println(getG(A, dTheta, dPhi + Math.PI/2)); 
        final double length = r/getG(A, dTheta, dPhi + Math.PI/2);
        final double curvature = (dTheta - A)/length;
        final double curvatureDerivative = (2*A)/(length*length);
        System.out.println("Length: "+length);
        System.out.println("Curvature: "+curvature);
        System.out.println("Change in curvature: "+curvatureDerivative);
    }

    public static void main(String[] args){
        //System.out.println(getG(-0.5,3,5));
        buildClothoid(new Vec2(0,0), 0, new Vec2(4,2), -0.3);
    }
}
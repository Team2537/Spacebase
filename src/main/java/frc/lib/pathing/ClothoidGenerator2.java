package frc.lib.pathing;

import frc.lib.util.Vec2;

public class ClothoidGenerator2 {
    private static double getG(double dTheta, double dPhi){
        return ClothoidMath.integrateS(2*dTheta, 0, dPhi, 0,1);
    }

    private static final double dA = 0.00001;
    private static double findA(double Aguess, double dPhi, double tolerance){
        double A = Aguess;
        double G = getG(A, dPhi);
        double GDerivative = (getG(A+dA, dPhi) - getG(A-dA, dPhi))/(2*dA);
        while (Math.abs(G) > tolerance){
            A -= G/GDerivative;
            G = getG(A, dPhi);
            GDerivative = (getG(A+dA, dPhi) - getG(A-dA, dPhi))/(2*dA);
        }
        System.out.println(A);
        return A;
    }

    public static final double TOLERANCE = 0.02;
    public static final int SEARCH_ITERATIONS = 1024;
    public static final double SEARCH_RANGE = Math.PI;
    public static void buildClothoid(Vec2 start, double startAngle, Vec2 end){
        final Vec2 diff = end.diff(start);
        final double r = Math.hypot(diff.y, diff.x);
        final double phi = Math.atan2(diff.y, diff.x);
        final double dPhi = (startAngle - phi);

        double A, length;
        double[] Aguess = new double[SEARCH_ITERATIONS+1];
        for(int i = 0; i <= SEARCH_ITERATIONS; i++){
            Aguess[i] = SEARCH_RANGE*i/SEARCH_ITERATIONS - SEARCH_RANGE/2;
        }

        A = 0;
        length = Double.POSITIVE_INFINITY;
        double Atemp, Ltemp;
        for(int i = 1; i <= SEARCH_ITERATIONS; i++){
            //if(getG(Aguess[i],dPhi)*getG(Aguess[i-1],dPhi) <= 0){
                Atemp = findA((Aguess[i] + Aguess[i-1])/2,dPhi,TOLERANCE);
                System.out.println(Math.toDegrees(Atemp));
                Ltemp = r/getG(Atemp,dPhi + Math.PI/2);

                if(Ltemp > 0 && Ltemp < length){
                    length = Ltemp;
                    A = Atemp;
                }
            //}
        }

        final double K0 = 0;
        final double Kp = (2*A)/(length*length);
        System.out.println("Length: "+length);
        System.out.println("Curvature: "+K0);
        System.out.println("Change in curvature: "+Kp);
        final double err = new Vec2(ClothoidMath.integrateC(Kp,K0,startAngle,0,length),
                                        ClothoidMath.integrateS(Kp,K0,startAngle,0,length))
                                    .diff(end).mag();
        System.out.println("Error: "+err);
        System.out.println(A);
    }

    public static void main(String[] args){
        //System.out.println(getG(-0.5,3,5));
        buildClothoid(new Vec2(0,0), 0, new Vec2(3,2));
    }
}
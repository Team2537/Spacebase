package frc.lib.pathing;

import frc.lib.util.Vec2;

public class ClothoidGenerator {
    private static double getG(double A, double dTheta, double dPhi){
        return ClothoidMath.integrateS(2*A, dTheta - A, dPhi, 0,1);
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

    public static final double TOLERANCE = 0.001;
    public static final int SEARCH_ITERATIONS = 1024;
    public static final double SEARCH_RANGE = 40;
    public static void buildClothoid(Vec2 start, double startAngle, Vec2 end, double endAngle){
        final Vec2 diff = end.diff(start);
        final double r = Math.hypot(diff.y, diff.x);
        final double phi = Math.atan2(diff.y, diff.x);
        final double dPhi = (startAngle - phi);
        final double dTheta = (endAngle - startAngle);

        double A, length;

        if(Math.abs(startAngle - phi) > Math.PI/2){ 
            A = findA(2.4674*dTheta + 5.2478*dPhi, dTheta, dPhi, TOLERANCE);
            length = r/getG(A, dTheta, dPhi + Math.PI/2);
        
        } else {

            double[] Aguess = new double[SEARCH_ITERATIONS+1];
            for(int i = 0; i <= SEARCH_ITERATIONS; i++){
                Aguess[i] = SEARCH_RANGE*i/SEARCH_ITERATIONS - SEARCH_RANGE/2;
            }

            A = 0;
            length = Double.POSITIVE_INFINITY;
            double Atemp, Ltemp;
            for(int i = 1; i <= SEARCH_ITERATIONS; i++){
                if(getG(Aguess[i],dTheta,dPhi)*getG(Aguess[i-1],dTheta,dPhi) <= 0){
                    Atemp = findA((Aguess[i] + Aguess[i-1])/2,dTheta,dPhi,TOLERANCE);
                    Ltemp = r/getG(Atemp,dTheta,dPhi + Math.PI/2);

                    if(Ltemp > 0 && Ltemp < length){
                        length = Ltemp;
                        A = Atemp;
                    }
                }
            }
        }

        final double curvature = (dTheta - A)/length;
        final double curvatureDerivative = (2*A)/(length*length);
        System.out.println("Length: "+length);
        System.out.println("Curvature: "+curvature);
        System.out.println("Change in curvature: "+curvatureDerivative);
    }

    public static void main(String[] args){
        //System.out.println(getG(-0.5,3,5));
        buildClothoid(new Vec2(0,0), 0, new Vec2(3,2), Math.PI/4);
    }
}
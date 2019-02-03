package frc.lib.pathing;

import frc.lib.util.Vec2;

public class ClothoidProfile {

    public final double pathLength,Kp,l,vel0,velF,acc,rampUpTime,cruiseTime;
    
    
    private static interface ProfileError { double get(double t); };
    private static final double dT = 0.0001, TOLERANCE = 0.0001;

    /**
     * @param startAngle Initial angle of the robot
     * @param nodeDistance Distance away from the path node for this turn
     * @param nodeAngle Angle between the current path segment and the next path segment
     * @param l Length between the wheels of the robot
     * @param accMax Maximum acceleration of the robot
     * @param velMax Maximum velocity of the robot
     * @param vel0 Initial linear velocity of the robot (should be less than velF)
     * @param velF Final velocity of the faster side of the robot (right side if turning left, left side if turning right)
    */
    public ClothoidProfile(double startAngle, double nodeDistance, double nodeAngle,
        double l, double accMax, double vel0, double velF){            
        
        if(vel0 < 0){
            throw new IllegalArgumentException("vel0 should be positive");
        } else if(velF < 0) {
            throw new IllegalArgumentException("velF should be positive");
        } else if(velF < vel0) {
            throw new IllegalArgumentException("velF should be greater than vel0");
        }

        
        // generate clothoid
        // based on https://arxiv.org/pdf/1209.0910.pdf
        nodeAngle /= 2;
        final double dTheta = Math.PI/2 - Math.abs(nodeAngle);
        final double dPhi = Vec2.angleBetween(
            new Vec2(startAngle),
            ClothoidMath.integrate(2*dTheta, 0, 0, 0,1)
        );
        final double r = nodeDistance*Math.sin(nodeAngle)/Math.sin(nodeAngle + dPhi);
        final double pathLength = r/ClothoidMath.integrateC(-2*dTheta, 0, dPhi, 0,1);

        // calculate change in curvature with respect to distance traveled along the path
        final double Kp = Math.signum(nodeAngle)*(2*dTheta)/(pathLength*pathLength); 
        System.out.println("Length: "+pathLength);
        System.out.println("Change in curvature: "+Kp);
        System.out.println("dPhi: "+dPhi);

        
        // find profile segment times
        double rampUpTime = (velF - vel0)/accMax;
        double cruiseTime;
        
        if(profileDist(Kp,0,l,accMax,vel0,rampUpTime) > pathLength){
            System.out.println(profileDist(Kp,0,l,accMax,vel0,rampUpTime));
            // we can't speed up fast enough to get our velocity to velF
            cruiseTime = 0;
            rampUpTime = (Math.sqrt(vel0*vel0 + accMax*pathLength*(0.5*l*Kp*pathLength + 2)) - vel0)/accMax;
            System.out.println(profileDist(Kp,0,l,accMax,vel0,rampUpTime));
            velF = rampUpTime*accMax + vel0;

        } else {
            // use Newton-Raphson method to find correct cruise time
            final double rampUpTemp = rampUpTime, velFTemp = velF;
            ProfileError f = (t) -> profileError(pathLength,Kp,l,accMax,vel0,velFTemp,rampUpTemp,t);
            cruiseTime = 0;
            double error = f.get(0);
            while(Math.abs(error) > TOLERANCE){
                double derivative = (f.get(cruiseTime + dT) - f.get(cruiseTime - dT))/(2*dT);
                cruiseTime -= f.get(cruiseTime)/derivative;
                error = f.get(cruiseTime);
            }

            if(cruiseTime < 0){
                rampUpTime += cruiseTime;
                velF = rampUpTime*accMax + vel0;
                cruiseTime = 0;
            }
        }

        this.pathLength = pathLength;
        this.Kp = Kp;
        this.l = l;
        this.acc = accMax;
        this.vel0 = vel0;
        this.velF = velF;
        this.rampUpTime = rampUpTime;
        this.cruiseTime = cruiseTime;
    }

    
    private static double getNodeRadius(double distanceToNode, double nodeAngle, double dPhi){
        return distanceToNode*Math.sin(dPhi)/Math.sin(dPhi+nodeAngle);
    }

    private static double profileDist(double Kp, double K, double l, double acc, double vel0, double t){
        return (Math.sqrt((0.5*acc*t*t + vel0*t)*l*Kp + Math.pow(1 + l*K/2, 2)) - (1 + l*K/2))*2/(l*Kp);
    }

    private static double profileError(double pathLength, double Kp, double l, 
        double accMax, double vel0, double velF,
        double rampUpTime, double cruiseTime){
        if(cruiseTime >= 0){
            double rampUpDist = profileDist(Kp, 0, l, accMax, vel0, rampUpTime);
            double cruiseDist = profileDist(Kp, Kp*rampUpDist, l, 0, velF, cruiseTime);
            double error = rampUpDist + cruiseDist - pathLength;
            if(Double.isNaN(error)){
                return pathLength;
            } else {
                return error;
            }
        } else if(-cruiseTime <= rampUpTime) {
            rampUpTime += cruiseTime;
            velF = rampUpTime*accMax + vel0;
            return profileError(pathLength,Kp,l,accMax,vel0,velF,rampUpTime,0);
        } else {
            return -pathLength;
        }
    }

    public static void main(String[] args){
        //System.out.println(getG(-0.5,3,5));
        //buildClothoid(0, 5, Math.PI/6);
        ClothoidProfile profile = new ClothoidProfile(0, 10, 2*Math.PI/3, 1, 0.5,1,4);
        System.out.println(profile.rampUpTime);
        System.out.println(profile.cruiseTime);
        System.out.println(profile.velF);
    }
}
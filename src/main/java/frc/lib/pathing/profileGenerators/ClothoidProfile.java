package frc.lib.pathing.profileGenerators;

import frc.lib.motion.MotionProfile;
import frc.lib.motion.WheelState;
import frc.lib.pathing.Clothoid;

public class ClothoidProfile {
    
    private static interface ProfileError { double get(double t); };
    private static final double dT = 0.001, TOLERANCE = 0.001;

    /**
     * @param robotLength Length between the wheels of the robot
     * @param accMax Maximum acceleration of the robot
     * @param velMax Maximum velocity of the robot
     * @param vel0 Initial linear velocity of the robot (should be less than velF)
     * @param velF Final velocity of the faster side of the robot (right side if turning left, left side if turning right)
    */
    public static void generate(MotionProfile profile, double dt,
            Clothoid clothoid, double robotLength, double accMax, double vel0, double velF){            
        final double Kp = Math.abs(clothoid.Kp);

        if(vel0 < 0){
            throw new IllegalArgumentException("vel0 should be positive");
        } else if(velF < 0) {
            throw new IllegalArgumentException("velF should be positive");
        } else if(velF < vel0) {
            //System.out.println(vel0);
            //System.out.println(velF);
            throw new IllegalArgumentException("velF should be greater than vel0");
        }
        
        // find profile segment times
        double rampUpTime = (velF - vel0)/accMax;
        double cruiseTime;
        
        if(profileDist(Kp,0,robotLength,accMax,vel0,rampUpTime) > clothoid.length){
            // we can't speed up fast enough to get our velocity to velF
            cruiseTime = 0;
            rampUpTime = (Math.sqrt(vel0*vel0 + accMax*clothoid.length*(0.5*robotLength*Kp*clothoid.length + 2)) - vel0)/accMax;
            velF = rampUpTime*accMax + vel0; 

        } else {
            // use Newton-Raphson method to find correct cruise time
            final double rampUpTemp = rampUpTime, velFTemp = velF;
            ProfileError f = (t) -> profileError(clothoid.length,Kp,robotLength,accMax,vel0,velFTemp,rampUpTemp,t);
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


        // step ahead by dt and get accelerations
        double[] accsLowRampUp = new double[(int)(rampUpTime/dt)];
        double[] accsLowCruise = new double[(int)(cruiseTime/dt)];

        // ramp up accelerations
        for(int i = 0; i < accsLowRampUp.length; i++){
            final double t = i*dt;
            final double inner = Kp*robotLength*(0.5*accMax*t*t + vel0*t) + 1;
            accsLowRampUp[i] = accMax*(2/Math.sqrt(inner) - 1) - Kp*robotLength*Math.pow(accMax*t+vel0,2)/Math.pow(inner, 1.5);
        }

        // cruise accelerations
        final double K = Kp*profileDist(Kp, 0, robotLength, accMax, vel0, rampUpTime);
        for(int i = 0; i < accsLowCruise.length; i++){
            final double t = i*dt;
            final double inner = Kp*robotLength*velF*t + Math.pow((1+K*robotLength/2), 2);
            accsLowCruise[i] = -Kp*robotLength*velF*velF/Math.pow(inner, 1.5);
        }

        // append accelerations
        for(double accLow : accsLowRampUp){
            appendAccs(profile, dt, clothoid.Kp, accLow, accMax);
        }
        for(double accLow : accsLowCruise) {
            appendAccs(profile, dt, clothoid.Kp, accLow, 0);
        }
        for(int i = accsLowCruise.length - 1; i >= 0; i--) {
            appendAccs(profile, dt, clothoid.Kp, -accsLowCruise[i], 0);
        }
        for(int i = accsLowRampUp.length - 1; i >= 0; i--) {
            appendAccs(profile, dt, clothoid.Kp, -accsLowRampUp[i], -accMax);
        }
    }

    private static void appendAccs(MotionProfile profile, double dt, double Kp, double accLow, double accHigh){
        if(Kp > 0){
            profile.appendControlWheels(new WheelState(accLow, accHigh), dt);
        } else {
            profile.appendControlWheels(new WheelState(accHigh, accLow), dt);
        }
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
}
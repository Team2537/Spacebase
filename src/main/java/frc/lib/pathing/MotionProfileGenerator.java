package frc.lib.pathing;

import frc.lib.pathing.profileGenerators.AngularProfile;
import frc.lib.pathing.profileGenerators.ClothoidProfile;
import frc.lib.pathing.profileGenerators.LinearProfile;

public class MotionProfileGenerator {
    public static final double dt = 0.003;
    public static void generate(MotionProfile profile, RobotConstraints constraints, Path path){
        Clothoid[] clothoids = path.getClothoids();

        final double accMax = constraints.maxWheelAcc, velMax = constraints.maxWheelVel, l = constraints.length;
        Clothoid[] linearSegs = new Clothoid[clothoids.length/2 + 1];
        Clothoid[] turnSegs = new Clothoid[clothoids.length/2];
        double[] linearVels = new double[clothoids.length/2 + 2];
        double[] turnVels = new double[clothoids.length/2];

        for(int i = 0; i < clothoids.length; i++){
            (i % 2 == 0 ? linearSegs : turnSegs)[i/2] = clothoids[i];
        }

        double velPrev = 0;
        for(int i = 0; i < linearSegs.length - 1; i++){
            velPrev = Math.min(velMax, Math.sqrt(velPrev*velPrev + 2*accMax*linearSegs[i].length));
            velPrev = Math.min(velPrev, turnSegs[i].getMaxStartVelocity(accMax, l));
            linearVels[i+1] = velPrev;
        }

        velPrev = 0;
        for(int i = linearSegs.length - 1; i > 0; i--){
            velPrev = Math.min(velMax, Math.sqrt(velPrev*velPrev + 2*accMax*linearSegs[i].length));
            velPrev = Math.min(velPrev, turnSegs[i-1].getMaxStartVelocity(accMax, l));
            linearVels[i+1] = Math.min(linearVels[i+1], velPrev);
        }
        
        for(int i = 0; i < turnSegs.length; i++){
            Clothoid c = turnSegs[i];
            final double vel0 = linearVels[i+1];
            turnVels[i] = Math.min(velMax, Math.sqrt(vel0*vel0 + accMax*c.length*(2 + 0.5*Math.abs(c.Kp)*l*c.length)));
        }


        for(int i = 0; i < linearSegs.length; i++){
            final double vel0 = linearVels[i], velF = linearVels[i+1];
            LinearProfile.generate(profile, linearSegs[i], accMax, velMax, vel0, velF);

            if(i < turnSegs.length){
                Clothoid c = turnSegs[i];
                if(Double.isInfinite(c.Kp)){
                    AngularProfile.generate(profile, c, accMax, velMax);
                } else {
                    ClothoidProfile.generate(profile, dt, c, l, accMax, velF, turnVels[i]);
                }
            }
        }
    }

    public static MotionProfile generate(RobotConstraints constraints, Path path){
        MotionState startState = MotionState.fromWheels(constraints, 0, path.start(), path.startAngle(), 0, 0, 0, 0);
        MotionProfile profile = new MotionProfile(startState);
        generate(profile, constraints, path);
        return profile;
    }

}
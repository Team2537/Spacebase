package frc.lib.pathing;

import java.util.Arrays;
import java.util.List;

import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.Pose2d;
import frc.lib.pathing.profileGenerators.AngularProfile;
import frc.lib.pathing.profileGenerators.ClothoidProfile;
import frc.lib.pathing.profileGenerators.LinearProfile;
import frc.lib.util.Util;

public class MotionProfileGenerator {
    public static void generate(MotionProfile profile, Waypoint[] waypoints, ProfileConstraints constraints, double timestep){
        final double accMax = constraints.accMax, velMax = constraints.velMax;
        final double l = profile.getDriveSpecs().wheelAxleLength;

        final Pose2d endPose = profile.endState().pose;

        List<Waypoint> waypointsList = Arrays.asList(waypoints);
        
        if(!waypoints[0].point.equals(endPose.vec)){
            waypointsList.add(0, new Waypoint(endPose.vec,0));
        }
        
        Path path = new Path(waypoints);
        Clothoid[] clothoids = path.getClothoids();

        if(path.startAngle() != endPose.ang){
            AngularProfile.generate(profile, Util.normalizeHeadingRadians(path.startAngle() - endPose.ang), l, accMax, velMax);
        }

        
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
            LinearProfile.generate(profile, linearSegs[i].length, accMax, velMax, vel0, velF);

            if(i < turnSegs.length){
                Clothoid c = turnSegs[i];
                if(Double.isInfinite(c.Kp)){
                    AngularProfile.generate(profile, c.dTheta*2, l, accMax, velMax);
                } else {
                    ClothoidProfile.generate(profile, timestep, c, l, accMax, velF, turnVels[i]);
                }
            }
        }
    }

    public static MotionProfile generate(Pose2d start, DriveSpecs drive, 
            Waypoint[] waypoints, ProfileConstraints constraints, double timestep){

        MotionProfile profile = new MotionProfile(drive, start);
        generate(profile, waypoints, constraints, timestep);
        return profile;
    }

    public static MotionProfile generate(DriveSpecs drive, 
            Waypoint[] waypoints, ProfileConstraints constraints, double timestep){

        return generate(new Pose2d(), drive, waypoints, constraints, timestep);
    }

    public static class ProfileConstraints{
        public final double accMax, velMax;
        public ProfileConstraints(double accMax, double velMax){
            this.accMax = accMax;
            this.velMax = velMax;
        }
    }

}
package frc.lib.pathing;

import java.awt.Color;

import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.Pose2d;
import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

public class Test {
    private static Waypoint[] waypoints;
    private static DriveSpecs drive = new DriveSpecs(12, 6, 0.83, 0.1, 1.8, 9.0);
    public static void main(String[] args) {
        final double dt = 0.001;
        Vec2[] points = new Vec2[]{
            new Vec2(0.0, 0.0),
            new Vec2(20.0, 20.0),
            new Vec2(10.0, 50.0),
            new Vec2(30.0, 60.0),
            new Vec2(40.0, 55.0),
            new Vec2(40.0, 35.0),
            new Vec2(60.0, 20.0),
            new Vec2(80.0, 35.0),
            new Vec2(80.0, 55.0)
        };
        
        waypoints = new Waypoint[points.length];
        for(int i = 0; i < points.length; i++) waypoints[i] = new Waypoint(points[i], 10);

        MotionProfile profile = MotionProfileGenerator.generate(drive, new Pose2d(0,0,Math.PI/4), waypoints, 0.1);
        
        Vec2[] newPoints = new Vec2[(int)(profile.dt()/dt)];
        for(int i = 0; i < newPoints.length; i++){
            newPoints[i] = profile.getState(i*dt).pose.vec;
        }

        Turtle turtle = new Turtle(800,800,9,new Vec2(40,30));
        turtle.animateProfile(profile);
        turtle.addPoints(points, new Color(255, 140, 0));
        turtle.addPoints(newPoints, Color.blue);
        //turtle.addPoints(MotionProfileGenerator.ptstemp, Color.green);

        System.out.println(profile.endState().pose.vec);
        System.out.println("Time: "+profile.endTime());
        System.out.println("Error: "+points[points.length-1].diff(profile.endState().pose.vec).mag());
    }
}
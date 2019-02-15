package frc.lib.pathing;

import java.awt.Color;

import frc.lib.motion.MotionProfile;
import frc.lib.motion.RobotConstraints;
import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

public class Test {
    public static void main(String[] args) {
        final double dt = 0.001;
        Vec2[] points = new Vec2[]{
            // new Vec2(0.0, 0.0),
            // new Vec2(20.0, 20.0),
            // new Vec2(10.0, 50.0),
            // new Vec2(30.0, 60.0),
            // new Vec2(40.0, 55.0),
            // new Vec2(40.0, 35.0),
            // new Vec2(60.0, 20.0),
            // new Vec2(80.0, 35.0),
            // new Vec2(80.0, 55.0)
            new Vec2(0,0),
            new Vec2(80,80),
            new Vec2(80,0),
            new Vec2(0,80),
            new Vec2(40,57),
            new Vec2(40,0)
        };
        
        Waypoint[] waypoints = new Waypoint[points.length];
        for(int i = 0; i < points.length; i++) waypoints[i] = new Waypoint(points[i], 10);
        
        RobotConstraints constraints = new RobotConstraints(500, 50, 12);
        MotionProfile profile = MotionProfileGenerator.generate(constraints, 0.003, waypoints);
        
        Vec2[] newPoints = new Vec2[(int)(profile.dt()/dt)];
        for(int i = 0; i < newPoints.length; i++){
            newPoints[i] = profile.getState(i*dt).pos;
        }

        Turtle turtle = new Turtle(800,800,9,new Vec2(40,30));
        turtle.animateProfile(profile);
        turtle.addPoints(points, new Color(255, 140, 0));
        turtle.addPoints(newPoints, Color.blue);

        System.out.println(profile.endState().pos);
        System.out.println("Time: "+profile.endTime());
        System.out.println("Error: "+points[points.length-1].diff(profile.endState().pos).mag());
    }
}
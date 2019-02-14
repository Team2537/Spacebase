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
            new Vec2(0.0, 0.0),
            new Vec2(10.0, 10.0),
            new Vec2(5.0, 25.0),
            new Vec2(15.0, 30.0),
            new Vec2(20.0, 27.5),
            new Vec2(20.0, 17.5),
            new Vec2(30.0, 17.5),
        };
        
        Path.Waypoint[] waypoints = new Path.Waypoint[points.length];
        for(int i = 0; i < points.length; i++) waypoints[i] = new Path.Waypoint(points[i], 4);

        Path path = new Path(waypoints);

        RobotConstraints constraints = new RobotConstraints(30, 15, 12);
        MotionProfile profile = MotionProfileGenerator.generate(constraints, 0.003, path);
        
        Vec2[] newPoints = new Vec2[(int)(profile.dt()/dt)];
        for(int i = 0; i < newPoints.length; i++){
            newPoints[i] = profile.getState(i*dt).pos;
        }

        Turtle turtle = new Turtle(800,800,18,new Vec2(10,15));
        turtle.addPoints(points, Color.red);
        turtle.addPoints(newPoints, Color.blue);

        System.out.println(profile.endState().pos);
        System.out.println("Time: "+profile.endTime());
        System.out.println("Error: "+points[points.length-1].diff(profile.endState().pos).mag());
    }
}
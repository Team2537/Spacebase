package frc.lib.pathing;

import java.awt.Color;

import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

public class Test {
    public static void main(String[] args){
        RobotConstraints constraints = new RobotConstraints(2, 1, 0.8, 0.005);
        Vec2[] points = new Vec2[]{
            new Vec2(0,0),
            new Vec2(1,1),
            new Vec2(0.5, 2.5),
            new Vec2(1.5, 3),
            new Vec2(2, 2.75),
            new Vec2(2, 1.5),
            new Vec2(-0.5, 1),
            new Vec2(-0.5,0.5),
            new Vec2(2.5,0.5)
        };
        PathWaypoint[] waypoints = PathWaypoint.convert(points);
        PathProfile profile = PathProfileGenerator.generateProfile(constraints, waypoints); 
        System.out.println("error: "+points[points.length-1].diff(profile.endState().pos).mag());
        System.out.println("total time: "+profile.dt());
        System.out.println("fits constraints: "+profile.fitsConstraints());
        System.out.println(profile.getState(0.355).pos);

        Vec2[] newPoints = new Vec2[(int)(profile.dt()/constraints.dt)];
        for(int i = 0; i < newPoints.length; i++){
            newPoints[i] = profile.getState(i*constraints.dt).pos;
        }

        
        Turtle t = new Turtle(800,800,240,new Vec2(1,1.5));
        t.addPoints(points, Color.red);
        t.addPoints(newPoints, Color.blue);
        
    }
}
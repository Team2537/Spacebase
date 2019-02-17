package frc.lib.pathing;

import java.awt.Color;

import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.pathing.MotionProfileGenerator.ProfileConstraints;
import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

public class Test {
    private static Waypoint[] waypoints;
    private static final double V_kv = 0.05, V_ka = 0.2, V_min = 1;
    private static DriveSpecs drive = new DriveSpecs(12, 6);
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

        final double accMax = gss(0, (12-V_min)/V_ka, 1e-5);
        final double velMax = (12 - V_min - V_ka*accMax)/V_kv;
        System.out.println("Acc Max: "+accMax+"\nVel Max: "+velMax);
        ProfileConstraints constraints = new ProfileConstraints(velMax, accMax);
        MotionProfile profile = MotionProfileGenerator.generate(drive, waypoints, constraints, 0.003);
        
        Vec2[] newPoints = new Vec2[(int)(profile.dt()/dt)];
        for(int i = 0; i < newPoints.length; i++){
            newPoints[i] = profile.getState(i*dt).pose.vec;
        }

        Turtle turtle = new Turtle(800,800,9,new Vec2(40,30));
        turtle.animateProfile(profile);
        turtle.addPoints(points, new Color(255, 140, 0));
        turtle.addPoints(newPoints, Color.blue);

        System.out.println(profile.endState().pose.vec);
        System.out.println("Time: "+profile.endTime());
        System.out.println("Error: "+points[points.length-1].diff(profile.endState().pose.vec).mag());
    }

    private static double f(double accMax){
        double velMax = (12 - V_min - V_ka*accMax)/V_kv;
        ProfileConstraints constraints = new ProfileConstraints(accMax, velMax);
        MotionProfile profile = MotionProfileGenerator.generate(drive, waypoints, constraints, 0.003);
        return profile.dt();
    }

    private static final double invphi = (Math.sqrt(5) - 1) / 2; // 1/phi
    private static final double invphi2 = (3 - Math.sqrt(5)) / 2; // 1/phi^2
    private static double gss(double a, double b, double tol){
        a = Math.min(a,b);
        b = Math.max(a,b);
        double h = b - a;
        if(h <= tol) return (a+b)/2;

        // required steps to achieve tolerance
        final double n = (int)(Math.ceil(Math.log(tol/h)/Math.log(invphi)));
        System.out.println(n);

        double c = a + invphi2 * h;
        double d = a + invphi * h;
        double yc = f(c);
        double yd = f(d);

        for(int k = 0; k < n-1; k++){
            if (yc < yd) {
                b = d;
                d = c;
                yd = yc;
                h = invphi*h;
                c = a + invphi2 * h;
                yc = f(c);
            } else {
                a = c;
                c = d;
                yc = yd;
                h = invphi*h;
                d = a + invphi * h;
                yd = f(d);
            }
        }

        if (yc < yd) return (a+d)/2;
        else return (c+b)/2;
    }
}
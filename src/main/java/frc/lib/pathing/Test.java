package frc.lib.pathing;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.Pose2d;
import frc.lib.util.Turtle;
import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class Test {
    private static DriveSpecs drive = new DriveSpecs(12, 6, 0.83, 0.1, 1.8, 9.0);
    private static final double dt = 0.001;
    public static void main(String[] args) {
        Vec2[] points = new Vec2[]{
            new Vec2(0.0, 0.0),
            new Vec2(20.0, 20.0),
            new Vec2(10.0, 50.0) ,
            new Vec2(30.0, 60.0),
            new Vec2(40.0, 55.0),
            new Vec2(40.0, 35.0),
            new Vec2(60.0, 20.0),
            //new Vec2(80.0, 35.0),
            //new Vec2(80.0, 55.0)
        };
        
        Turtle turtle = new Turtle(800,800,9,new Vec2(40,30));

        MouseAdapter adapter = new MouseAdapter() { 
            private int selectedIndex = -1;

            public void mousePressed(MouseEvent me) {
                final double rMax = 10;

                Vec2 posMouse = new Vec2(me.getX(), me.getY());
                double closestDist = -1;

                for(int i = 0; i < points.length; i++){
                    Vec2 posPoint = turtle.toPanelSpace(points[i]);
                    Vec2 diff = posPoint.diff(posMouse);
                    if(diff.mag() <= rMax && (selectedIndex < 0 || diff.mag() < closestDist)) {
                        selectedIndex = i;
                        closestDist = diff.mag();
                    }
                }
            }
            

            public void mouseDragged(MouseEvent me){
                if(selectedIndex >= 0){
                    Vec2 newPoint = turtle.toWorldSpace(new Vec2(me.getX(), me.getY()));
                    Vec2 oldPoint = points[selectedIndex];
                    points[selectedIndex] = newPoint;

                    boolean anyAcute = false;
                    for(int i = 1; i < points.length-1; i++){
                        double phi = Math.abs(Vec2.angleBetween(
                            points[i-1].diff(points[i]), 
                            points[i+1].diff(points[i])
                        ));
                        if(phi < Math.PI/2 || Util.epsilonEquals(phi, Math.PI, 0.01)) {
                            anyAcute = true;
                            break;
                        }
                    }

                    if(anyAcute){
                        points[selectedIndex] = oldPoint;
                    }


                    List<Vec2> ghostPts = new ArrayList<>();

                    if(selectedIndex > 0){
                        ghostPts.add(points[selectedIndex-1]);
                    }

                    ghostPts.add(points[selectedIndex]);

                    if(selectedIndex < points.length-1){
                        ghostPts.add(points[selectedIndex+1]);
                    }

                    turtle.clearPoints(Color.lightGray);
                    turtle.addPoints(ghostPts, Color.lightGray);
                }
            }

            public void mouseReleased(MouseEvent me){
                if(selectedIndex >= 0){
                    drawProfileFromPoints(turtle, points);
                }
                selectedIndex = -1;
            }
        };
        turtle.addMouseAdapter(adapter);

        drawProfileFromPoints(turtle, points);

        //turtle.addPoints(MotionProfileGenerator.ptstemp, Color.green);

        // System.out.println(profile.endState().pose.vec);
        // System.out.println("Time: "+profile.endTime());
        // System.out.println("Error: "+points[points.length-1].diff(profile.endState().pose.vec).mag());
    }

    private static void drawProfileFromPoints(Turtle turtle, Vec2[] points){
        Waypoint[] waypoints = new Waypoint[points.length];
        for(int i = 0; i < points.length; i++) waypoints[i] = new Waypoint(points[i], 10);

        turtle.clearPoints();
        turtle.addPoints(points, new Color(255, 140, 0));

        Pose2d startPose = new Pose2d(points[0], points[1].diff(points[0]).angle());
        MotionProfile profile = null;
        try {
            profile = MotionProfileGenerator.generate(drive, startPose, waypoints, 0.1);

            Vec2[] newPoints = new Vec2[(int)(profile.dt()/dt)];
            for(int i = 0; i < newPoints.length; i++){
                newPoints[i] = profile.getState(i*dt).pose.vec;
            }

            turtle.animateProfile(profile);
            //turtle.addPoints(MotionProfileGenerator.ptstemp, Color.green);
            turtle.addPoints(newPoints, Color.blue);
            
        } catch(RuntimeException e){
        }
    }
}
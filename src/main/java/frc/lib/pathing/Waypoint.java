package frc.lib.pathing;

import frc.lib.util.Vec2;

public class Waypoint {
    public final Vec2 point;
    private double radius;
    public Waypoint(Vec2 point, double turningRadius){
        this.point = point;
        setRadius(turningRadius);
    }
    public Waypoint(double x, double y, double turningRadius){
        this(new Vec2(x,y), turningRadius);
    }
    public double radius(){
        return radius;
    }
    public void setRadius(double radius){
        this.radius = Math.abs(radius);
    }
}
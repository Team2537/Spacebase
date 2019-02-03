package frc.lib.pathing;

import frc.lib.util.Vec2;

public class PathWaypoint {
    public final Vec2 point;
    public final double turningRadius;
    public PathWaypoint(Vec2 point, double turningRadius){
        this.point = point;
        this.turningRadius = Math.abs(turningRadius);
    }
    public PathWaypoint(double x, double y, double turningRadius){
        this(new Vec2(x,y), turningRadius);
    }
    public PathWaypoint(Vec2 point){
        this.point = point;
        this.turningRadius = -1;
    }
    public PathWaypoint(double x, double y){
        this(new Vec2(x,y));
    }
    public static PathWaypoint[] convert(Vec2[] points){
        PathWaypoint[] out = new PathWaypoint[points.length];
        for(int i = 0; i < points.length; i++){
            out[i] = new PathWaypoint(points[i]);
        }
        return out;
    }
}
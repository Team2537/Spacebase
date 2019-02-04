package frc.lib.pathing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc.lib.util.Vec2;

public class Path {
    private Vec2 start, end = null;
    private List<Waypoint> waypoints;

    public Path(Vec2 start){
        waypoints = new ArrayList<>();
        this.start = start;
    }

    public Path(Vec2 start, List<Waypoint> waypoints, Vec2 end){
        this(start);
        for(Waypoint waypoint : waypoints) appendWaypoint(waypoint);
        completePath(end);
    }

    public Path(Vec2 start, Waypoint[] waypoints, Vec2 end){
        this(start, Arrays.asList(waypoints), end);
    }

    public Vec2 getStart(){
        return start;
    }

    public Vec2 getEnd(){
        return end;
    }

    public boolean isComplete(){
        return end != null;
    }

    /** @return true if the Waypoint was appended successfully.
     * If the path has been completed already, the Waypoint won't be appended.
     */
    public boolean appendWaypoint(Waypoint waypoint){
        if(isComplete()) return false;

        Vec2 point = waypoint.point, lastPoint;
        double radius = waypoint.turningRadius, lastRadius;
        if(waypoints.size() > 0){
            Waypoint lastWaypoint = waypoints.get(waypoints.size() - 1);
            lastPoint = lastWaypoint.point;
            lastRadius = lastWaypoint.turningRadius;
        } else {
            lastPoint = start;
            lastRadius = 0;
        }
        double dist = point.diff(lastPoint).mag();
        
        if(lastRadius > dist){
            lastRadius = dist;
            waypoints.set(waypoints.size() - 1, new Waypoint(lastPoint, lastRadius));
        }

        if(lastRadius + radius > dist){
            radius = dist - lastRadius;
            waypoint = new Waypoint(point, radius);
        }

        waypoints.add(waypoint);
        return true;
    }

    /** @return true if the Waypoint was appended successfully.
     * If the path has been completed already, the Waypoint won't be appended.
     */
    public boolean appendWaypoint(Vec2 point, double turningRadius){
        return appendWaypoint(new Waypoint(point, turningRadius));
    }

    /** @return true if the Waypoint was appended successfully.
     * If the path has been completed already, the Waypoint won't be appended.
     */
    public boolean appendWaypoint(double x, double y, double turningRadius){
        return appendWaypoint(new Vec2(x,y), turningRadius);
    }

    /** @return true if the Path was completed successfully.
     * If the path has been completed already, it can't be completed again.
     */
    public boolean completePath(Vec2 end){
        if(isComplete()) return false;
        this.end = end;
        return true;
    }

    public static class Waypoint {
        public final Vec2 point;
        public final double turningRadius;
        public Waypoint(Vec2 point, double turningRadius){
            this.point = point;
            this.turningRadius = Math.abs(turningRadius);
        }
        public Waypoint(double x, double y, double turningRadius){
            this(new Vec2(x,y), turningRadius);
        }
    }
}
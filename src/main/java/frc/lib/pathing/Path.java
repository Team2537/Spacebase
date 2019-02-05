package frc.lib.pathing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

public class Path {
    private Vec2 start, end = null;
    private double startAngle, endAngle;
    private List<Waypoint> waypoints;
    private List<Segment> segments;
    private List<Clothoid> clothoids;

    public Path(Vec2 start){
        waypoints = new ArrayList<>();
        segments = new ArrayList<>();
        clothoids = new ArrayList<>();
        this.start = start;
        waypoints.add(new Waypoint(start,0));
    }

    public Path(List<Waypoint> waypoints){
        this(waypoints.get(0).point);
        if(waypoints.size() < 2) {
            throw new IllegalArgumentException("At least 2 Waypoints are needed to create a path");
        }

        for(int i = 1; i < waypoints.size() - 1; i++) {
            appendWaypoint(waypoints.get(i));
        }
        completePath(waypoints.get(waypoints.size()-1).point);
    }

    public Path(Waypoint[] waypoints){
        this(Arrays.asList(waypoints));
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
        waypoints.add(new Waypoint(end,0));

        // create Segments
        for(int i = 0; i < waypoints.size() - 1; i++){
            segments.add(new Segment(waypoints.get(i), waypoints.get(i+1)));
        }
        startAngle = segments.get(0).diff.angle();
        endAngle = segments.get(segments.size()-1).diff.angle();

        // make sure turning radii of adjacent segments don't overlap
        for(int i = 0; i < segments.size(); i++){
            Segment seg = segments.get(i);
            if(seg.end.radius() > seg.dist){
                seg.end.setRadius(seg.dist);
            }
            final double overlap = (seg.start.radius() + seg.end.radius() - seg.dist)/2;
            if(overlap > 0){
                seg.start.setRadius(seg.start.radius() - overlap);
                seg.end.setRadius(seg.end.radius() - overlap);
            }
        }

        /* generate clothoids */
        for(int i = 0; i < segments.size(); i++){
            Segment seg = segments.get(i);
            // linear segment
            final double dist = seg.dist - seg.start.radius() - seg.end.radius();
            if(dist > 0){
                clothoids.add(Clothoid.fromLength(dist));
            }

            // turn segments
            if(i < segments.size() - 1){
                Segment segNext = segments.get(i+1);
                final double nodeAngle = segNext.diff.angleBetween(seg.diff.inverse());
                Clothoid turn = new Clothoid(seg.diff.angle(), seg.end.radius(), nodeAngle);
                clothoids.add(turn);
                clothoids.add(turn.flip());
            }
        }

        return true;
    }

    public Clothoid[] getClothoids(){
        if(!isComplete()) return null;

        Clothoid[] arr = new Clothoid[clothoids.size()];
        clothoids.toArray(arr);
        return arr;
    }

    public Vec2 start(){
        return start;
    }

    public Vec2 end(){
        return end;
    }

    public double startAngle(){
        if(!isComplete()) return Double.NaN;
        return startAngle;
    }
    
    public double endAngle(){
        if(!isComplete()) return Double.NaN;
        return endAngle;
    }

    public static class Waypoint {
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

    public static class Segment {
        public final Waypoint start, end;
        public final Vec2 diff;
        public final double dist;
        public Segment(Waypoint start, Waypoint end){
            this.start = start;
            this.end = end;
            this.diff = end.point.diff(start.point);
            this.dist = diff.mag();
        }
    }
    
    public static void main(String[] args){

        Vec2[] points = new Vec2[]{
            new Vec2(0.0, 0.0),
            new Vec2(1.0, 1.0),
            new Vec2(0.5, 2.5),
            new Vec2(1.5, 3.0),
            new Vec2(2.0, 2.75),
            new Vec2(2,1.5)
        };
        Waypoint[] waypoints = new Waypoint[points.length];
        for(int i = 0; i < points.length; i++) waypoints[i] = new Waypoint(points[i], 0.5);

        Path path = new Path(waypoints);
        Clothoid[] clothoids = path.getClothoids();
        for(Clothoid c : clothoids){
            System.out.println(c);
        }

        Turtle t = new Turtle(800,800,240,new Vec2(1,1.5));
        t.addPoints(points, Color.red);
    }
}
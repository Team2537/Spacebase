package frc.lib.pathing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc.lib.motion.Pose2d;
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
            clothoids.add(Clothoid.fromLength(dist));

            // turn segments
            if(i < segments.size() - 1){
                Segment segNext = segments.get(i+1);
                final double nodeAngle = segNext.diff.angleBetween(seg.diff.inverse());
                Clothoid turn = new Clothoid(seg.end.radius(), nodeAngle);
                clothoids.add(turn);
                // clothoids.add(turn.flip());
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

    public List<Pose2d> split(double ds){
        if(!isComplete()) return null;
        
        List<Pose2d> out = new ArrayList<>();
        Pose2d currentPose = new Pose2d(start,startAngle);
        double s;
        for(Clothoid c : clothoids){
            s = 0;
            while(s < c.length){
                out.add(c.getPose(currentPose, s));
                s += ds;
            }
        }
        out.add(new Pose2d(end, endAngle));
        return out;
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
}
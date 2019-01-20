package frc.lib.pathing;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class PathProfileGenerator {

    public static PathProfile generateProfile(RobotConstraints constraints, PathWaypoint[] waypoints){
        PathSegment[] segments = waypointsToSegments(constraints, waypoints);
        if(segments.length == 0) return new PathProfile(constraints);

        double acc = constraints.maxWheelAcc;

        // Set the starting velocity of each turn such that we're guaranteed to
        // have enough distance to accelerate/deccelerate to that velocity, based
        // on our robot's constraints.
        while(true){
            double highestConstraint = 0;
            PathSegment next = null;
            for(PathSegment seg : segments){
                double vs = seg.startNode().vel(), ve = seg.endNode().vel();
                double constraint = Math.abs(ve*ve - vs*vs)/(2*acc) - seg.linearDistance();
                if(constraint > highestConstraint && !Util.epsilonEquals(constraint, highestConstraint)){
                    highestConstraint = constraint;
                    next = seg;
                }
            }
            if(next == null) break;
            correctVel(acc, next);
        }
        for(PathSegment s : segments){
            //s.endNode().setVel(0);
        }

        MotionState startState = MotionState.fromWheels(constraints, 0, 
                                    segments[0].start, segments[0].end.diff(segments[0].start).angle(), 
                                    0, 0, 0, 0);
        PathProfile profile = new PathProfile(startState);

        for(PathSegment segment : segments){
            linearProfile(constraints, profile, segment);
            clothoidProfile(constraints, profile, segment.endNode());
        }
        
        return profile;
    }

    private static void correctVel(double acc, PathSegment seg){
        final boolean reversed = seg.startNode().vel() > seg.endNode().vel();
        PathNode start = reversed ? seg.endNode() : seg.startNode();
        PathNode end = reversed ? seg.startNode() : seg.endNode();

        final double len = seg.length;
        if((start.vmult+end.vmult)*start.vel() < len){
            final double vs = start.vel(), ds = start.turnDist(), mul = end.vmult;
            final double vCorrected = acc*(Math.sqrt(mul*mul - (vs*vs)/(acc*acc) + 2*(len - ds)/acc) - mul);
            end.setVel(Double.isNaN(vCorrected) ? 0 : vCorrected);
        } else {
            final double vCorrected = seg.length/(start.vmult+end.vmult);
            start.setVel(vCorrected);
            end.setVel(vCorrected);
        }   
    }

    private static void linearProfile(RobotConstraints constraints, PathProfile profile, PathSegment segment){
        final double dist = segment.linearDistance();
        final double acc_max = constraints.maxWheelAcc;
        final double v_start = segment.startNode().vel(), v_end = segment.endNode().vel();

        // What is the maximum velocity we can reach (Vmax)? This is the intersection of two curves: one accelerating
        // towards the goal from profile.finalState(), the other coming from the goal at max vel (in reverse). If Vmax
        // is greater than constraints.max_abs_vel, we will clamp and cruise.
        // Solve the following three equations to find Vmax (by substitution):
        // Vmax^2 = Vstart^2 + 2*a*d_accel
        // Vgoal^2 = Vmax^2 - 2*a*d_decel
        // delta_pos = d_accel + d_decel
        final double v_max = Math.min(constraints.maxWheelVel,
                Math.sqrt((v_start*v_start + v_end*v_end) / 2.0 + dist*acc_max));
        double v_now = v_start, pos_now = 0;

        // Accelerate to v_max
        if (v_max > v_start) {
            final double accel_time = (v_max - v_now)/acc_max;
            profile.appendControlWheels(accel_time, acc_max, acc_max);
            pos_now += 0.5*acc_max*accel_time*accel_time + v_now*accel_time;
            v_now += acc_max*accel_time;
        }
        // Figure out how much distance will be covered during deceleration.
        final double distance_decel = Math.max(0, (v_now*v_now - v_end*v_end) / (2*acc_max));
        final double distance_cruise = Math.max(0, dist - pos_now - distance_decel);
        // Cruise at constant velocity.
        if (distance_cruise > 0) {
            final double cruise_time = distance_cruise / v_now;
            profile.appendControlWheels(cruise_time, 0, 0);
        }
        // Decelerate to goal velocity.
        if (distance_decel > 0) {
            final double decel_time = (v_now - v_end) / acc_max;
            profile.appendControlWheels(decel_time, -acc_max, -acc_max);
        }
    }

    private static void clothoidProfile(RobotConstraints constraints, PathProfile profile, PathNode node){
        if(node.theta == 0) return;
        if(node.vel() == 0){
            turnProfile(constraints, profile, node.theta, node.clockwise);
            return;
        }

        final double theta = node.theta, length = constraints.length, dt = constraints.dt;
        final double acc_max = constraints.maxWheelAcc, vel_max = node.vel();
        final double time_half = Math.sqrt((theta*length)/acc_max) + (theta*length)/(4*vel_max);
        final double scalar = acc_max/vel_max;
        final int intervals = (int)(time_half / dt);
        
        double[] accs = new double[intervals*2];
        for(int i = 0; i < intervals; i++){
            final double acc_wheel = -acc_max*Math.pow(1 + scalar*i*dt, -1.5);
            accs[i] = acc_wheel;
            accs[intervals*2 - 1 - i] = -acc_wheel;
        }
        for(int i = 0; i < intervals*2; i++){
            if(node.clockwise) profile.appendControlWheels(dt, 0, accs[i]);
            else profile.appendControlWheels(dt, accs[i], 0);
        }
    }

    public static void turnProfile(RobotConstraints constraints, PathProfile profile, double theta, boolean clockwise){
        final double acc_max = 2*constraints.maxWheelAcc/constraints.length;
        final double vel_max = 2*constraints.maxWheelVel/constraints.length;
        double time_accel, time_cruise;
        if((vel_max*vel_max)/(2*acc_max) > theta){
            time_accel = Math.sqrt(theta/acc_max);
            time_cruise = 0;
        } else {
            time_accel = vel_max/acc_max;
            time_cruise = (theta/vel_max) - (vel_max/acc_max);
        }
        final double sign = (clockwise ? -1 : 1);
        profile.appendControlAngular(time_accel, 0, sign*acc_max);
        profile.appendControlAngular(time_cruise, 0, 0);
        profile.appendControlAngular(time_accel, 0, -sign*acc_max);
    }

    private static PathSegment[] waypointsToSegments(RobotConstraints constraints, PathWaypoint[] waypoints){
        int amt = waypoints.length - 1;
        if(amt < 1) return new PathSegment[0];

        PathSegment[] out = new PathSegment[amt];
        for(int i = 0; i < amt; i++){
            out[i] = new PathSegment(waypoints[i].point, waypoints[i+1].point);
        }

        PathNode currentNode = new PathNode();
        PathSegment start, end;
        for(int i = 0; i < amt - 1; i++){
            start = out[i];
            end = out[i+1];
            PathNode newNode = new PathNode(constraints, 
                                    start.start, start.end, end.end, 
                                    waypoints[i+1].maxDistance);
            start.setNodes(currentNode, newNode);
            currentNode = newNode;
        }
        out[amt-1].setNodes(currentNode, new PathNode());

        return out;
    }


    private static class PathSegment {
        public final Vec2 start, end;
        public final double length;
        private PathNode startNode, endNode;
    
        public PathSegment(Vec2 start, Vec2 end) {
            this.start = start;
            this.end = end;
            this.length = end.diff(start).mag();
            this.startNode = null;
            this.endNode = null;
        }
    
        public PathNode startNode(){
            return startNode;
        }
        public PathNode endNode(){
            return endNode;
        }
        public boolean hasNodes(){
            return startNode != null && endNode != null;
        }
        public void setNodes(PathNode startNode, PathNode endNode){
            if(!hasNodes()){
                this.startNode = startNode;
                this.endNode = endNode;
            }
        }
    
        public double linearDistance(){
            if(!hasNodes()) return length;
            return length - startNode().turnDist() - endNode().turnDist();
        }
    }

    private static class PathNode {
        public final double theta, vmult;
        public final boolean clockwise;
        private double vel;
    
        public PathNode(RobotConstraints constraints, Vec2 start, Vec2 here, Vec2 end, double maxDistance){
            this.theta = Math.PI - Vec2.angleBetween(start.diff(here), end.diff(here));
            this.clockwise = Vec2.cross(start.diff(here), end.diff(here)) > 0;
            
            // A turn will cover a distance over its PathSegments that's proportional to 
            // the starting velocity of that turn. "vmult" is the proportionality constant.
            double inner = Math.sqrt(theta/Math.PI);
            double fC = Util.fresnelC(inner), fS = Util.fresnelS(inner);
            this.vmult = Math.sqrt(Math.PI*constraints.length/constraints.maxWheelAcc)
                             *(fC + fS*Math.tan(theta/2));
            
            if(maxDistance < 0){
                this.vel = constraints.maxWheelVel;
            } else {
                this.vel = Math.min(maxDistance/fS, constraints.maxWheelVel);
            }
        }
    
        public PathNode(){
            this.theta = 0;
            this.clockwise = true;
            this.vmult = 0;
            this.vel = 0;
        }
    
        public double vel(){
            return vel;
        }
    
        public double turnDist(){
            return vel()*vmult;
        }
    
        public void setVel(double vel){
            if(vel < Util.kEpsilon){
                this.vel = 0;
            } else {
                this.vel = vel;
            }
        }
    }
}
package frc.lib.pathing;

import frc.lib.util.Vec2;

public class MotionSegment {
    public final MotionState start, end;
    public final double startTime, endTime, dt;
    public final Vec2 startPos, endPos;
    public MotionSegment(MotionState start, double dt){
        this.dt = dt;
        this.start = start;
        this.startTime = start.t;
        this.startPos = start.pos;
        this.end = start.forwardKinematics(dt);
        this.endTime = end.t;
        this.endPos = end.pos;
    }
    public boolean fitsConstraints(){
        return start.fitsConstraints && end.fitsConstraints;
    }
}
package frc.lib.motion;

import java.util.ArrayList;
import java.util.List;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class MotionProfile {
    private List<MotionSegment> segments;
    private MotionState startState;

    public MotionProfile(MotionState startState){
        reset(startState);
    }

    public MotionProfile(RobotConstraints constraints){
        this(MotionState.fromWheels(constraints,0,new Vec2(0,0),0,0,0,0,0));
    }

    public void reset(MotionState startState){
        this.segments = new ArrayList<>();
        this.startState = startState;
    }

    public boolean isEmpty(){
        return segments.isEmpty();
    }

    public MotionState startState(){
        return startState;
    }
    public MotionState endState(){
        if(isEmpty()) return startState;
        return segments.get(segments.size()-1).end;
    }
    public double startTime(){
        return startState().t;
    }
    public double endTime(){
        return endState().t;
    }
    public double dt(){
        return endTime() - startTime();
    }

    public void appendControlWheels(double dt, double accL, double accR){
        if(dt > 0){
            segments.add(new MotionSegment(endState().controlWheels(accL, accR), dt));
        }
    }

    public void appendControlAngular(double dt, double acc, double angAcc){
        if(dt > 0){
            segments.add(new MotionSegment(endState().controlAngular(acc, angAcc), dt));
        }
    }

    public void appendControlCurvature(double dt, double acc, double curvature){
        final double k = curvature*startState.length/4;
        final double accR = (k-0.5)*(endState().velR/dt) + (k+0.5)*(2*acc + endState().velL/dt);
        final double accL = 2*acc - accR;
        appendControlWheels(dt, accL, accR);
    }

    public MotionState getState(double t){
        t = Util.clamp(t, startTime(), endTime());
        MotionState start = startState;
        for(MotionSegment s : segments){
            start = s.start;
            if(t < s.endTime) break;
        }
        return start.forwardKinematics(t - start.t);
    }

    public boolean fitsConstraints(){
        for(MotionSegment s : segments){
            if(!s.fitsConstraints()) return false;
        }
        return true;
    }

    public double arclength(double t0, double tF){
        double length = 0;
        for(MotionSegment s : segments){
            if(s.startTime >= t0 && s.endTime <= tF){
                length += s.endPos.diff(s.startPos).mag();
            }
        }
        return length;
    }

    public double arclength(double tF){
        return arclength(0,tF);
    }

}
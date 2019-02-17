package frc.lib.motion;

import java.util.ArrayList;
import java.util.List;

import frc.lib.util.Util;

public class MotionProfile {
    private DriveSpecs drive;
    private List<MotionSegment> segments;
    private MotionState startState;

    public MotionProfile(DriveSpecs drive, MotionState startState){
        this.drive = drive;
        reset(startState);
    }

    public MotionProfile(DriveSpecs drive, Pose2d start){
        this(drive, new MotionState(start));
    }

    public MotionProfile(DriveSpecs drive){
        this(drive, new MotionState());
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

    public void appendControlWheels(WheelState accWheels, double dt){
        if(dt > 0){
            segments.add(new MotionSegment(drive, endState().controlWheels(drive, accWheels), dt));
        }
    }

    public void appendControlChassis(ChassisState acc, double dt){
        if(dt > 0){
            segments.add(new MotionSegment(drive, endState().controlChassis(drive, acc), dt));
        }
    }

    public void appendDeltas(double dt, ChassisState delta){
        if(dt > 0){
            MotionState es = endState();
            segments.add(new MotionSegment(drive, MotionState.fromWheels(
                drive, es.t, 
                new Pose2d(es.pose.vec, es.pose.ang+delta.angular), 
                new WheelState(delta.linear/dt, delta.linear/dt),
                new WheelState()
            ), dt));
        }
    }

    public MotionState getState(double t){
        t = Util.clamp(t, startTime(), endTime());
        MotionState start = startState;
        for(MotionSegment s : segments){
            start = s.start;
            if(t < s.endTime) break;
        }
        return start.forwardKinematics(drive, t - start.t);
    }

    public double arclength(double t0, double tF){
        double length = 0;
        for(MotionSegment s : segments){
            if(s.startTime >= t0 && s.endTime <= tF){
                length += s.endPose.vec.diff(s.startPose.vec).mag();
            }
        }
        return length;
    }

    public double arclength(double tF){
        return arclength(0,tF);
    }

    public DriveSpecs getDriveSpecs(){
        return drive;
    }

}
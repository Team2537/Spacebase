package frc.lib.motion;

public class MotionSegment {
    public final MotionState start, end;
    public final double startTime, endTime, dt;
    public final Pose2d startPose, endPose;
    public MotionSegment(DriveSpecs drive, MotionState start, double dt){
        this.dt = dt;
        this.start = start;
        this.startTime = start.t;
        this.startPose = start.pose;
        this.end = start.forwardKinematics(drive, dt);
        this.endTime = end.t;
        this.endPose = end.pose;
    }
}
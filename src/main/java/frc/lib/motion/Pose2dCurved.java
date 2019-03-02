package frc.lib.motion;

import frc.lib.util.Vec2;

public class Pose2dCurved extends Pose2d {
    public final double curvature;

    public Pose2dCurved(Vec2 vec, double ang, double curvature){
        super(vec,ang);
        this.curvature = curvature;
    }

    public Pose2dCurved(double x, double y, double ang, double curvature){
        super(x,y, ang);
        this.curvature = curvature;
    }

    public Pose2dCurved(Pose2d pose, double curvature){
        this(pose.vec, pose.ang, curvature);
    }
}
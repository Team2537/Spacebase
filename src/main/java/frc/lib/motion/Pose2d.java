package frc.lib.motion;

import frc.lib.util.Util;
import frc.lib.util.Vec2;

public class Pose2d {
    public final Vec2 vec;
    public final double x,y,ang;

    public Pose2d(Vec2 vec, double ang){
        this.vec = vec;
        this.x = vec.x;
        this.y = vec.y;
        this.ang = Util.normalizeHeadingRadians(ang);
    }

    public Pose2d(double x, double y, double ang){
        this(new Vec2(x,y), ang);
    }

    public static Pose2d addDelta(Pose2d pose, Pose2d delta){
        return new Pose2d(pose.vec.add(delta.vec.rotateBy(pose.ang)), pose.ang+delta.ang);
    }

    public Pose2d addDelta(Pose2d delta){
        return addDelta(this, delta);
    }

    public Pose2d(){
        this(0,0,0);
    }

    public String toString(){
        return String.format("{vec: %s, ang: %f}", vec, ang);
    }
}
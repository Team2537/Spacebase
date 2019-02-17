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

    public static Pose2d delta(Pose2d one, Pose2d two){
        return new Pose2d(one.vec.diff(two.vec), one.ang - two.ang);
    }

    public Pose2d delta(Pose2d other){
        return Pose2d.delta(this, other);
    }

    public Pose2d(){
        this(0,0,0);
    }
}
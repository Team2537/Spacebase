package frc.lib.util;

public class Vec2 {
    public final double x,y;
    private double mag = -1;
    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    /** Returns the sum of two vectors. */
    public static Vec2 add(Vec2 a, Vec2 b){
        return new Vec2(a.x + b.x, a.y + b.y);
    }
    /** Returns the sum of this vector and another vector. */
    public Vec2 add(Vec2 other){
        return add(this, other);
    }
    /** Returns a copy of vector v scaled by a factor of k. */
    public static Vec2 scale(Vec2 v, double k){
        return new Vec2(v.x * k, v.y * k);
    }
    /** Returns a copy of this vector scaled by a factor of k. */
    public Vec2 scale(double k){
        return scale(this, k);
    }
    /** Returns a copy of vector v scaled by -1. */
    public static Vec2 inverse(Vec2 v){
        return v.scale(-1);
    }
    /** Returns a copy of this vector scaled by -1. */
    public Vec2 inverse(){
        return inverse(this);
    }
    /** Returns the difference of two vectors (a - b). */
    public static Vec2 diff(Vec2 a, Vec2 b){
        return add(a,b.inverse());
    }
    /** Returns the difference of this vector and another vector (this - other). */
    public Vec2 diff(Vec2 other){
        return diff(this, other);
    }
    /** Returns the magnitude (length) of the vector. */
    public static double mag(Vec2 v){
        if(v.mag == -1){
            v.mag = Math.hypot(v.x, v.y);
        }
        return v.mag;
    }
    /** Returns the magnitude (length) of this vector. */
    public double mag(){
        return mag(this);
    }
    /** Returns a normalized (length = 1) version of the vector. */
    public static Vec2 normal(Vec2 v){
        return v.scale(1/v.mag());
    }
    /** Returns a normalized (length = 1) version of this vector. */
    public Vec2 normal(){
        return normal(this);
    }
    /** Returns the dot product of the two vectors. */
    public static double dot(Vec2 a, Vec2 b){
        return a.x*b.x + a.y*b.y;
    }
    /** Returns the dot product of this vector and another vector. */
    public double dot(Vec2 other){
        return dot(this, other);
    }
    /** Returns the cross product of the two vectors. */
    public static double cross(Vec2 a, Vec2 b){
        return a.x*b.y - a.y*b.x;
    }
    /** Returns the cross product of this vector and another vector. */
    public double cross(Vec2 other){
        return cross(this, other);
    }
    /** Returns the angle between two vectors. */
    public static double angleBetween(Vec2 a, Vec2 b){
        return Math.acos(dot(a.normal(),b.normal()));
    }
    /** Returns the angle between this vector and another vector. */
    public double angleBetween(Vec2 other){
        return angleBetween(this,other);
    }
    /** Returns the angle between a vector and the horizontal. */
    public static double angle(Vec2 v){
        return Math.atan2(v.y, v.x);
    }
    /** Returns the angle between this vector and the horizontal. */
    public double angle(){
        return angle(this);
    }

    @Override
    public String toString(){
        return "("+x+", "+y+")";
    }
}
package frc.lib.units;

public class Units {
	public static double degrees_to_radians(double val){
		return val*Math.PI/180;
	}

	public static double radians_to_degrees(double val){
		return val*180/Math.PI;
	}

	public static double revolutions_to_inches(double val, double wheelDiameter){
		return val*Math.PI*wheelDiameter;
	}
	public static double revoltionsPerMinute_to_inchesPerSecond(double val, double wheelDiameter){
		return revolutions_to_inches(val, wheelDiameter)/60;
	}
}
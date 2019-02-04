package frc.lib.units;

public class Units {	
	public static double convertDistance(double value, Distances currentType, Distances desiredType){
		return value * currentType.getInches() / desiredType.getInches();
	}
	
	public static double convertTime(double value, Times currentType, Times desiredType){
		return value * currentType.getMillis() / desiredType.getMillis();
	}
	
	public static double convertSpeed(double value, Distances currentDistanceType, Times currentTimeType,
			Distances desiredDistanceType, Times desiredTimeType){
		return value * (currentDistanceType.getInches() / currentTimeType.getMillis()) 
				* (desiredTimeType.getMillis() / desiredDistanceType.getInches());
	}

	public static double convertAccel(double value, Distances currentDistanceType, Times currentTimeType,
			Distances desiredDistanceType, Times desiredTimeType){
		return convertSpeed(value, currentDistanceType, currentTimeType, desiredDistanceType, desiredTimeType)
			* (desiredTimeType.getMillis() / currentTimeType.getMillis());
	}
	
	public static double roundDigits(double value, int digits){
		return (int)(value * Math.pow(10, digits))/Math.pow(10, digits);
    }
}
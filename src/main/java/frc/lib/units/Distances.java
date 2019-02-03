package frc.lib.units;

public enum Distances {
	INCHES			(1),
	REVOLUTIONS		(Math.PI*Units.wheelDiameter),
	FEET			(12),
	CENTIMETERS		(1/2.54),
	METERS			(100/2.54);
	
	private final double toInches;
	
	Distances(double toInches){
		this.toInches = toInches;
	}
	
	public double getInches(){
		return toInches;
	}
}
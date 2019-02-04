package frc.lib.units;

import frc.robot.Specs;

public enum Distances {
	INCHES			(1),
	REVOLUTIONS		(Math.PI*Specs.DRIVE_WHEEL_DIAMETER),
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
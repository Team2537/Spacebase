package frc.lib.units;

public enum Times{
	NANOSECONDS	(1e-6),
	MILLISECONDS(1),
	CYCLES		(20),
	HUNDRED_MS	(100),
	SECONDS		(1000),
	MINUTES		(60000)
	;
	
	private final double toMillis;
	
	Times(double toMillis){
		this.toMillis = toMillis;
	}
	
	public double getMillis(){
		return toMillis;
	}
}
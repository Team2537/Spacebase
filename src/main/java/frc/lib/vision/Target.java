package frc.lib.vision;

public class Target {
	private Point[] points;
	private Point[] boundingBox;
	private Point boundingBoxCenter;
	private double boundingBoxArea;

	public Target(Point[] points) {
		this.points = points;
		this.boundingBoxArea = 0;
	}
	
	public Target(){
		this(new Point[0]);
		this.boundingBox = new Point[2];
		this.boundingBox[0] = new Point(0,0);
		this.boundingBox[1] = new Point(0,0);
	}

	public Point[] getPoints() {
		return points;
	}

	public Point[] getBoundingBox() {
		// must be run with at least 4 points in the target for no duplicates to occur
		// bottom left and top right
		if(boundingBox == null){
			boundingBox = new Point[2];
			int leftX = Integer.MAX_VALUE;
			int rightX = Integer.MIN_VALUE;
			int upY = Integer.MIN_VALUE;
			int downY = Integer.MAX_VALUE;
			for (int i = 0; i < points.length; i++) {
				if (points[i].x < leftX) {
					leftX = points[i].x;
				}
				if (points[i].x > rightX) {
					rightX = points[i].x;
				}
				if (points[i].y > upY) {
					upY = points[i].y;
				}
				if (points[i].y < downY) {
					downY = points[i].y;
				}
			}
			boundingBox[0] = new Point(leftX, upY); 
			boundingBox[1] = new Point(rightX, downY);
		}
		return boundingBox;
	}
	
	public double getBoundingBoxLength(){
		if(boundingBox == null){
			getBoundingBox();
		}
		return boundingBox[1].x - boundingBox[0].x;
	}
	
	public double getBoundingBoxHeight(){
		if(boundingBox == null){
			getBoundingBox();
		}
		return boundingBox[1].y - boundingBox[0].y;
	}
	
	public double getBoundingBoxArea(){
		if(boundingBoxArea == 0){
			boundingBoxArea = Math.abs(getBoundingBoxLength()*getBoundingBoxHeight());
		}
		return boundingBoxArea;
	}
	
	public Point getBoundingBoxCenter(){
		if(boundingBoxCenter == null){
			Point topLeft = getBoundingBox()[0];
			boundingBoxCenter = new Point((int)(topLeft.x + getBoundingBoxLength()/2),
					(int)(topLeft.y + getBoundingBoxHeight()/2));
		}
		return boundingBoxCenter;
	}
}

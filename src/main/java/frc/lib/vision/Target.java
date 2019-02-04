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

	/** @return The midpoint between the two targets in the array with the largest bounding boxes.
	 * If less than 2 targets are passed in, a new Point(0,0) is returned.
	 */
	public static Point getMidpoint(Target[] targets){
		if(targets.length < 2) return new Point(0,0);

		double[] boundingBoxAreas = new double[targets.length];

		for(int i = 0; i < boundingBoxAreas.length; i++){
			System.out.println(boundingBoxAreas[i]);
			boundingBoxAreas[i] = targets[i].getBoundingBoxArea();   
		}

		double first = -Double.MAX_VALUE, second = -Double.MAX_VALUE;
		int firstIndex = 0, secondIndex = 0;
		for (int i = 0; i < boundingBoxAreas.length; i++) {
			if (boundingBoxAreas[i] > first) {
				second = first;
				first = boundingBoxAreas[i];
				firstIndex = i;
			}
			// if current element is between first and second, update second to store value of current variable
			else if (boundingBoxAreas[i] > second && boundingBoxAreas[i] != first){
				second = boundingBoxAreas[i];
				secondIndex = i;
			}
		}

		Point box1 = targets[firstIndex].getBoundingBoxCenter();
		Point box2 = targets[secondIndex].getBoundingBoxCenter();
		return new Point((box1.x + box2.x)/2, (box1.y + box2.y)/2);
	}
}

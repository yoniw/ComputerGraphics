package RayTracing;


public class Camera {

	private Point position;
	private Point lookAt;
	private Vector upVector;
	private Vector rightVector;
	private double screenDistance;
	private double screenWidth;
	
	private Vector direction;
	
	public Camera(String[] params)
	{
		position =  new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		lookAt = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]));
		
		direction = new Vector(position, lookAt);
		direction.normalize();
		
		upVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]));
		// Fixing up vector to be perpendicular to the direction vector we calculated.
		Vector leftVector = VectorOperations.crossProduct(upVector, direction);
		leftVector.normalize();
		rightVector = VectorOperations.scalarMult(-1, leftVector);
		upVector = VectorOperations.crossProduct(rightVector, direction);
		upVector.normalize();
		
		screenDistance = Double.parseDouble(params[9]);
		screenWidth = Double.parseDouble(params[10]);
	}

	public Ray constructRayThroughPixel(int i, int j, int imageWidth, int imageHeight) {
		Ray ray = new Ray(position, direction);
		Point centerPoint = ray.getPoint(screenDistance);
		
		Point point = getWorldPointFromScreenCoordinates(i, j, imageWidth, imageHeight, centerPoint);
		ray.setV(VectorOperations.subtract(point, position));

		return ray;	
		
	}

	private Point getWorldPointFromScreenCoordinates(int i, int j, int imageWidth, int imageHeight, Point centerPoint) {
		double pixelWidth = screenWidth / imageWidth;
		double pixelHeight = (imageWidth / imageHeight) * pixelWidth;
		double upPixels =   (j - (imageHeight / 2) ) * pixelHeight * -1;
		double rightPixels = (i - (imageWidth / 2) ) * pixelWidth;

		Point point = VectorOperations.add(centerPoint, VectorOperations.scalarMult(upPixels, upVector));
		point= VectorOperations.add(point, VectorOperations.scalarMult(rightPixels, rightVector));
		return point;
	}
	
	
}

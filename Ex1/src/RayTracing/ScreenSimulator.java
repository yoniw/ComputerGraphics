package RayTracing;

public class ScreenSimulator {
	
	private Vector v;
	private Vector currentV;
	
	private Vector xDiff;
	private Vector yDiff;

	
	public ScreenSimulator(Camera camera, int imageWidth, int imageHeight) {

		Vector zAxis = camera.getDirection();
		Vector xAxis = VectorOperations.crossProduct(camera.getUpVector(), zAxis);
		Vector yAxis = VectorOperations.crossProduct(xAxis, zAxis);
		
		// Calculating start vector.
		double pixelSize = camera.getScreenWidth() / imageWidth;
		v = VectorOperations.add(camera.getPosition(), VectorOperations.scalarMult(camera.getScreenDistance(), zAxis));
		v = VectorOperations.subtract(v, VectorOperations.scalarMult(camera.getScreenWidth() / 2, xAxis));
		v = VectorOperations.subtract(v, VectorOperations.scalarMult(imageHeight * pixelSize / 2, yAxis));

		xDiff = VectorOperations.scalarMult(pixelSize, xAxis);
		yDiff = VectorOperations.scalarMult(pixelSize, yAxis);
	}
	
	
	public ScreenSimulator(Camera camera, Light light, Vector lightVector, double numShadowRays) {
		
		Vector xAxis = VectorOperations.crossProduct(camera.getUpVector(), lightVector);
		Vector yAxis = VectorOperations.crossProduct(xAxis, lightVector);
		
		// Calculating start vector.
		double lightRadius = light.getLightWidth() / 2;
		double pixelSize = light.getLightWidth() / numShadowRays;
		v = VectorOperations.subtract(light.getPosition(), VectorOperations.scalarMult(lightRadius, xAxis));
		v = VectorOperations.subtract(v, VectorOperations.scalarMult(lightRadius, yAxis));
		
		xDiff = VectorOperations.scalarMult(pixelSize, xAxis);
		yDiff = VectorOperations.scalarMult(pixelSize, yAxis);
	}
	
	
	public void initCurrentV() {
		currentV = v;
	}
	
	public Vector getCurrentV() {
		return currentV;
	}
	
	
	public void nextX() {
		currentV = VectorOperations.add(currentV, xDiff);
	}
	
	public void nextY() {
		v = VectorOperations.add(v, yDiff);
	}
	
	
	public Vector getXDiff() {
		return xDiff;
	}
	
	public Vector getYDiff() {
		return yDiff;
	}
}

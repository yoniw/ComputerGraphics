package RayTracing;

public class Point {

	private double x;
	private double y;
	private double z;
	
	public Point(double x, double y, double z) {

		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public double[] getPointAsDoubleArr()
	{
		double[] result = new double[3];
		result[0] = x;
		result[1] = y;
		result[2] = z;
		return result;
	}
}

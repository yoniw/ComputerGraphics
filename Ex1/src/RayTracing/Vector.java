package RayTracing;


public class Vector extends Point {
	
	private double length;
	
	public Vector(double x, double y, double z) {
		
		super(x, y, z);
		
		this.length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	
	public Vector(double x1, double y1, double z1, double x2, double y2, double z2) {
		this(x2 - x1, y2 - y1, z2 - z1);
	}
	
	public Vector(Point p1, Point p2) {
		this(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
	}
	

	public void normalize() {
		
		this.x /= this.length;
		this.y /= this.length;
		this.z /= this.length;
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
	
	public double getLength() {
		return length;
	}
}

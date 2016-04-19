package RayTracing;

public class Ray {

	private Point p0;
	private Vector v;

	public Ray(Point p0, Vector v) {
		this.p0 = p0;
		this.v = v;
	}
	
	
	public Point getPoint(double distance) {
		Vector vec = VectorOperations.scalarMult(distance, v);
		return VectorOperations.add(p0, vec);
	}

	
	public Point getP0() {
		return p0;
	}
	
	public Vector getV() {
		return v;
	}
	
	public void setV(Vector v) {
		this.v = v;
	}
}
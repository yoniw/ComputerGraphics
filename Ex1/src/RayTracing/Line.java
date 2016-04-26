package RayTracing;

public class Line {

	private Point start;
	private Vector direction;

	public Line(Point start, Vector direction) {
		this.start = start;
		this.direction = direction;
	}
	
	
	public Point getPoint(double distance) {
		Vector vec = VectorOperations.scalarMult(distance, direction);
		return VectorOperations.add(start, vec);
	}
}

package RayTracing;


public class Cylinder extends Surface{

	private Point centerPosition;
	private double length;
	private double radius;
	private Vector direction;
	private Point base;
	
	public Cylinder(String[] params)
	{
		super(Integer.parseInt(params[8]));
		centerPosition = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		length = Double.parseDouble(params[3]);
		radius = Double.parseDouble(params[4]);
		double xRotation = Double.parseDouble(params[5]);
		double yRotation = Double.parseDouble(params[6]);
		double zRotation = Double.parseDouble(params[7]);
		
		direction = new Vector(0, 0, 1);
		direction = MatrixRotation.rotateX(direction, xRotation);
		direction = MatrixRotation.rotateY(direction, yRotation);
		direction = MatrixRotation.rotateZ(direction, zRotation);
		
		base = VectorOperations.subtract(centerPosition, direction);
	}



	@Override
	public double getIntersection(Ray ray) {
		// Preparing arguments for quadratic equation.
		double vDP = VectorOperations.dotProduct(ray.getV(), direction);
		Vector deltaP = VectorOperations.subtract(ray.getP0(), base);
		double pDP = VectorOperations.dotProduct(deltaP, direction);
		
		Vector aV = VectorOperations.subtract(ray.getV(), VectorOperations.scalarMult(vDP, direction));
		double a = aV.getLength() * aV.getLength();
		
		Vector bV = VectorOperations.subtract(deltaP, VectorOperations.scalarMult(pDP, direction));
		double b = 2 * VectorOperations.dotProduct(aV, bV);
		
		double c = bV.getLength() * bV.getLength() - radius * radius;
		
		double[] t = solveQuadraticEquation(a, b, c);
		
		if (t == null) {
			// No intersection.
			return -1;
			
		} else if ((t[0] < 0) && (t[1] < 0)) {
			// No intersection.
			return -1;
		
		} else if ((t[0] >= 0) && (t[1] < 0)) {
			// Only first solution is valid.
			return t[0];
			
		} else if ((t[0] < 0) && (t[1] >= 0)) {
			// Only second solution is valid.
			return t[1];
		
		} else {
			// Both solutions are valid.
			return Math.min(t[0], t[1]);
		}
	}



	@Override
	public Vector getNormal(Vector lightDirection, Point point, boolean withDirection) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private double[] solveQuadraticEquation(double a, double b, double c) {
		
		double disc = b * b - 4 * a * c;
		if (disc < 0) { 
			return null;
		}
		
		double[] res = new double[2];
		res[0] = ((-1) * b + Math.sqrt(disc)) / (2 * a);
		res[0] = ((-1) * b - Math.sqrt(disc)) / (2 * a);
		
		return res;
	}
}

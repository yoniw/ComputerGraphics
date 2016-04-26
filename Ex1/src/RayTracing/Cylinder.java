package RayTracing;


public class Cylinder extends Surface{

	private Point centerPosition;
	private double length;
	private double radius;
	
	private Line cylinderLine;
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
		
		cylinderLine = new Line(centerPosition, direction);
		
		base = cylinderLine.getPoint((-1) * length / 2);
		//base = centerPosition;
	}



	@Override
	public double getIntersection(Ray ray) {
		double t1 = getCylinderIntersection(ray);
		double t2 = getCapsIntersection(ray);
		
		if ((t1 <= 0) && (t2 <= 0)) {
			return -1;
			
		} else if (t1 <= 0) {
			return t2;
			
		} else if (t2 <= 0) {
			return t1;
			
		} else {
			return Math.min(t1,  t2);
		}
	}
	
	private double getCylinderIntersection(Ray ray) {
		// Preparing arguments for quadratic equation.
		double vDP = VectorOperations.dotProduct(ray.getV(), direction);
		Vector deltaP = VectorOperations.subtract(ray.getP0(), base);
//		Vector deltaP = VectorOperations.subtract(ray.getP0(), centerPosition);
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
		
		} else if ((t[0] > 0) && (t[1] <= 0)) {
			// Only first solution is valid.
			if (hasFiniteIntersection(ray, t[0])) {
				return t[0];
			} else {
				// Intersection point exceeds cylinder length.
				return -1;
			}
			
		} else if ((t[0] <= 0) && (t[1] > 0)) {
			// Only second solution is valid.
			if (hasFiniteIntersection(ray, t[1])) {
				return t[1];
			} else {
				// Intersection point exceeds cylinder length.
				return -1;
			}
		
		} else {
			// Both solutions are valid.
			boolean t0Intersection = hasFiniteIntersection(ray, t[0]);
			boolean t1Intersection = hasFiniteIntersection(ray, t[1]);
			
			if (t0Intersection && t1Intersection) {
				return Math.min(t[0], t[1]);
				
			} else if (t0Intersection) {
				return t[0];
				
			} else if (t1Intersection) {
				return t[1];
			
			} else {
				// Intersection points exceeds cylinder length.
				return -1;
			}
		}
	}
	
	
	private double getCapsIntersection(Ray ray) {
		
		Point p1 = cylinderLine.getPoint((-1) * length / 2);
		Plane pln1 = new Plane(direction, VectorOperations.dotProduct(direction, p1), materialIndex);
		double t1 = pln1.getIntersection(ray);
		double t1Distance = -1;
		if (t1 > -1) {
			Point t1Point = ray.getPoint(t1);
			t1Distance = VectorOperations.getDistance(p1, t1Point);
		}
		
		Point p2 = cylinderLine.getPoint(length / 2);
		Plane pln2 = new Plane(direction, VectorOperations.dotProduct(direction, p2), materialIndex);
		double t2 = pln2.getIntersection(ray);
		double t2Distance = -1;
		if (t2 > -1) {
			Point t2Point = ray.getPoint(t2);
			t2Distance = VectorOperations.getDistance(p2, t2Point);
		}
		
		// Summarizing.
		if ((t1 < 0) && (t2 < 0)) {
			return -1;
			
		} else if (t1 < 0) {
			if (t2Distance <= radius) {
				return t2;
			} else {
				return -1;
			}
			
		} else if (t2 < 0) {
			if (t1Distance <= radius) {
				return t1;
			} else {
				return -1;
			}
			
		} else {
			if ((t1Distance <= radius) && (t2Distance <= radius)) {
				return Math.min(t1, t2);
				
			} else if (t1Distance <= radius) {
				return t1;
				
			} else if (t2Distance <= radius) {
				return t2;
				
			} else {
				return -1;
			}
		}
	}
	
	
	private boolean hasFiniteIntersection(Ray ray, double t) {
		
		Point tP = ray.getPoint(t);
		Vector vP = VectorOperations.subtract(tP, base);
		
		double distance = VectorOperations.dotProduct(direction, vP);
		
		if (distance > length) {
			// Intersection point exceeds cylinder length.
			return false;
		
		} else if (distance < 0) {
			// Intersection point exceeds cylinder length.
			return false;
			
		} else {
			return true;
		}
	}



	@Override
	public Vector getNormal(Vector lightDirection, Point point, boolean withDirection) {
		Vector normal = new Vector(centerPosition, point);
		normal.normalize();
		if (!withDirection)
		{
			return normal;
		}
		double cosAngel = VectorOperations.dotProduct(normal, lightDirection);
		if (Math.acos(cosAngel) > 0.5*Math.PI)
		{
			return VectorOperations.invert(normal);
		}
		else
		{
			return normal;
		}
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

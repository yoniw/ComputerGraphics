package RayTracing;

public class Plane implements Surface{

	private Vector normal;
	private double offset;
	private int materialIndex;
	
	public Plane(String[] params)
	{
		normal = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		offset = Double.parseDouble(params[3]);
		materialIndex = Integer.parseInt(params[4]);
	}
	
	@Override
	public double getIntersection(Ray ray) {
		
		double vd = VectorOperations.dotProduct(normal, ray.getV());
		if (vd == 0) {
			// Plane is parallel - no intersection.
			return -1;
		}
		
		double v0 = (-1) * VectorOperations.dotProduct(normal, VectorOperations.add(ray.getP0(), new Vector(offset, offset, offset)));
		double t = v0 / vd;
		if (t < 0) {
			return -1;
		}
		
		return t;
	}
}

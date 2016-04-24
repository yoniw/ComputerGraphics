package RayTracing;

public class Plane extends Surface{

	private Vector normal;
	private double offset;
	
	// an abstract plane. no need for a real material
	public Plane(Vector normal, double offset)
	{
		super(0); 
		this.normal = normal;
		this.offset = offset;
	}
	
	public Plane(String[] params)
	{
		super(Integer.parseInt(params[4]));
		normal = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		offset = Double.parseDouble(params[3]);
		
	}
	
	@Override
	public double getIntersection(Ray ray) {
		
		double vd = VectorOperations.dotProduct(normal, ray.getV());
		if (vd == 0) {
			// Plane is parallel - no intersection.
			return -1;
		}
		
		//double v0 = (-1) * VectorOperations.dotProduct(normal, VectorOperations.add(ray.getP0(), new Vector(offset, offset, offset)));
		double v0 = VectorOperations.dotProduct(normal, ray.getP0());
		double t = (-1 * v0 + offset) / vd;
		if (t < 0) {
			return -1;
		}
		
		return t;
	}



	@Override
	//TODO does withDirection flag has a meaning here? 
	public Vector getNormal(Vector lightDirection, Point point, boolean withDirection) {
		return VectorOperations.normalize(normal);
	}
}

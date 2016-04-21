package RayTracing;

public class Sphere extends Surface{

	private Point sphereCenter;
	private double radius;

	
	public Sphere(String[] params)
	{
		super(Integer.parseInt(params[4]));
		sphereCenter = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		radius = Double.parseDouble(params[3]);
	}
	
	@Override
	public double getIntersection(Ray ray) {
		
		Vector l = VectorOperations.subtract(sphereCenter, ray.getP0());
		
		double t_ca = VectorOperations.dotProduct(l, ray.getV());
		if (t_ca < 0) { 
			// No intersection.
			return -1;
		}
		
		double d2 = VectorOperations.dotProduct(l, l) + (t_ca * t_ca);
		if (d2 > (radius * radius)) {
			// No intersection.
			return -1;
		}
		
		double t_hc = Math.sqrt((radius * radius) - d2);
		return t_ca - t_hc;
	}

	@Override
	public Vector getNormal(Vector lightDirection, Point point, boolean withDirection) {
		Vector normal = new Vector(sphereCenter, point);
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




}

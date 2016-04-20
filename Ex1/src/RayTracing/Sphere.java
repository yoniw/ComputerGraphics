package RayTracing;

public class Sphere implements Surface{

	private Point sphereCenter;
	private double radius;
	private int materialIndex;
	
	public Sphere(String[] params)
	{
		sphereCenter = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		radius = Double.parseDouble(params[3]);
		materialIndex = Integer.parseInt(params[4]);
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
}

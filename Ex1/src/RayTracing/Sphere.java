package RayTracing;

public class Sphere {

	private double[] sphereCenter;
	private double[] radius;
	private double[] materialIndex;
	
	public Sphere(String[] params)
	{
		sphereCenter = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		radius = new double[]{Double.parseDouble(params[3])};
		materialIndex = new double[]{Double.parseDouble(params[4])};
	}
}

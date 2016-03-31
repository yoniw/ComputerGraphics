package RayTracing;

public class Plane {

	private double[] normal;
	private double[] offset;
	private double[] materialIndex;
	
	public Plane(String[] params)
	{
		normal = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		offset = new double[]{Double.parseDouble(params[3])};
		materialIndex = new double[]{Double.parseDouble(params[4])};
	}
}

package RayTracing;

public class Cylinder {

	private double[] centerPosition;
	private double[] length;
	private double[] radius;
	private double[] rotation;
	private double[] materialIndex;
	
	public Cylinder(String[] params)
	{
		centerPosition = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		length = new double[]{Double.parseDouble(params[3])};
		radius = new double[]{Double.parseDouble(params[4])};
		rotation = new double[]{Double.parseDouble(params[5]), Double.parseDouble(params[6]), Double.parseDouble(params[7])};
		materialIndex = new double[]{Double.parseDouble(params[8])};
	}
}

package RayTracing;

public class Settings {

	private double[] backgroundColorRGB;
	private double[] rootNumberOfShadowRays;
	private double[] maxNumberOfRecursions;
	
	public Settings(String[] params)
	{
		backgroundColorRGB = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		rootNumberOfShadowRays = new double[]{Double.parseDouble(params[3])};
		maxNumberOfRecursions = new double[]{Double.parseDouble(params[4])};
	}
	
	
}

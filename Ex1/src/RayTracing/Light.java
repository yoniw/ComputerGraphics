package RayTracing;

public class Light {

	private double[] position;
	private double[] colorRGB;
	private double[] specularIntensity;
	private double[] shadowIntensity;
	private double[] lightWidth;
	
	public Light(String[] params)
	{
		position = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		colorRGB = new double[]{Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5])};
		specularIntensity = new double[]{Double.parseDouble(params[6])};
		shadowIntensity = new double[]{Double.parseDouble(params[7])};
		lightWidth = new double[]{Double.parseDouble(params[8])};
	}
}

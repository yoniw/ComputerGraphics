package RayTracing;

public class Light {

	private Point position;
	private RGB colorRGB;
	private double specularIntensity;
	private double shadowIntensity;
	private double lightWidth;
	
	public Light(String[] params)
	{
		position = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		colorRGB = new RGB(Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]));
		specularIntensity = Double.parseDouble(params[6]);
		shadowIntensity = Double.parseDouble(params[7]);
		lightWidth = Double.parseDouble(params[8]);
	}
}

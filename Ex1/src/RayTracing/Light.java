package RayTracing;

public class Light {

	private Point position;
	private RGB colorRGB;
	private double specularIntensity;
	private double shadowIntensity;
	private double lightWidth;
	
	public Light(String[] params)
	{
		position = new Point(Float.parseFloat(params[0]),Float.parseFloat(params[1]), Float.parseFloat(params[2]));
		colorRGB = new RGB(Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
		specularIntensity = Double.parseDouble(params[6]);
		shadowIntensity = Double.parseDouble(params[7]);
		lightWidth = Double.parseDouble(params[8]);
	}

	public Point getPosition() {
		return position;
	}

	public RGB getColor() {
		return colorRGB;
	}

	public double getSpecularIntensity() {
		return specularIntensity;
	}
	
	public double getShadowIntensity() {
		return shadowIntensity;
	}
	
	public double getLightWidth() {
		return lightWidth;
	}
}

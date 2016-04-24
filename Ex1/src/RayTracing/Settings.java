package RayTracing;

public class Settings {

	private RGB backgroundColorRGB;
	private int rootNumberOfShadowRays;
	private int maxNumberOfRecursions;
	
	public Settings(String[] params)
	{
		backgroundColorRGB = new RGB(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
		rootNumberOfShadowRays = Integer.parseInt(params[3]);
		maxNumberOfRecursions = Integer.parseInt(params[4]);
	}
	
	public RGB getBackgroundColor()
	{
		return backgroundColorRGB;
	}

	public int getMaxNumberOfRecursions() {
		return maxNumberOfRecursions;
	}

	public int getNumberOfShadowRays() {
		return rootNumberOfShadowRays;
	}
}

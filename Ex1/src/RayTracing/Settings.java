package RayTracing;

public class Settings {

	private RGB backgroundColorRGB;
	private int rootNumberOfShadowRays;
	private int maxNumberOfRecursions;
	
	public Settings(String[] params)
	{
		backgroundColorRGB = new RGB(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
		rootNumberOfShadowRays = Integer.parseInt(params[3]);
		maxNumberOfRecursions = Integer.parseInt(params[4]);
	}
	
	public RGB getBackgroundColor()
	{
		return backgroundColorRGB;
	}
}

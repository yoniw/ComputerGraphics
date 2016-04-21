package RayTracing;

public class Settings {

	private RGB backgroundColorRGB;
	private int rootNumberOfShadowRays;
	private int maxNumberOfRecursions;
	
	public Settings(String[] params)
	{
		backgroundColorRGB = new RGB(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
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

	public void minusOneReduction() {
		maxNumberOfRecursions--;
	}
}

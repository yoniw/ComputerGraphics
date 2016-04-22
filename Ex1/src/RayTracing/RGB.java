package RayTracing;

public class RGB {

	private double red;
	private double green;
	private double blue;
	

	public RGB(double red, double green, double blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
//	public RGB(byte red, byte green, byte blue) {
//		this.red = red;
//		this.green = green;
//		this.blue = blue;
//	}
	
//	public RGB(int red, int green, int blue) {
//		this((byte)red, (byte)green, (byte)blue);
//	}
	
//	public RGB(int[] rgbColors)
//	{
//		this(rgbColors[0], rgbColors[1], rgbColors[2]);
//	}
	
	public double getRed()
	{
		return red;
	}
	public double getGreen()
	{
		return green;
	}
	public double getBlue()
	{
		return blue;
	}


}

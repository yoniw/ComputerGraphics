package RayTracing;

public class RGB {

	private double red;
	private double green;
	private double blue;
	

	public RGB(double red, double green, double blue)
	{
		// calculated colors might "overflow" 255
		if (red > 1)
		{
			this.red = 1;
		}
		else
		{
			this.red = red;
		}
		if (green > 1)
		{
			this.green = 1;
		}
		else
		{
			this.green = green;

		}
		if (blue > 1)
		{
			this.blue = 1;
		}
		else
		{
			this.blue = blue;
		}
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

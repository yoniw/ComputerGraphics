package RayTracing;

public class RGB {

	private byte red;
	private byte green;
	private byte blue;
	

	public RGB(byte red, byte green, byte blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public RGB(int red, int green, int blue) {
		this((byte)red, (byte)green, (byte)blue);
	}
	
	public RGB(int[] rgbColors)
	{
		this(rgbColors[0], rgbColors[1], rgbColors[2]);
	}
	
	public byte getRed()
	{
		return red;
	}
	public byte getGreen()
	{
		return green;
	}
	public byte getBlue()
	{
		return blue;
	}

	public double[] getRGBAsDoubleArr() {
		double[] result = new double[3];
		result[0] = red;
		result[1] = green;
		result[2] = blue;
		return result;
	}
}

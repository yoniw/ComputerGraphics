package RayTracing;

public class RGB {

	private byte red;
	private byte green;
	private byte blue;
	

	public RGB(double red, double green, double blue)
	{
		//TODO multiply by 255 and convert to byte
	}
	
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


}

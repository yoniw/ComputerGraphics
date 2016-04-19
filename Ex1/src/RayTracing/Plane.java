package RayTracing;

public class Plane implements Surface{

	private Point normal;
	private double offset;
	private int materialIndex;
	
	public Plane(String[] params)
	{
		normal = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		offset = Double.parseDouble(params[3]);
		materialIndex = Integer.parseInt(params[4]);
	}
}

package RayTracing;

public class Sphere implements Surface{

	private Point sphereCenter;
	private double radius;
	private int materialIndex;
	
	public Sphere(String[] params)
	{
		sphereCenter = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		radius = Double.parseDouble(params[3]);
		materialIndex = Integer.parseInt(params[4]);
	}
}

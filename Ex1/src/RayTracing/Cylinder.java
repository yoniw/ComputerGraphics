package RayTracing;

public class Cylinder {

	private Point centerPosition;
	private double length;
	private double radius;
	private Point rotation;
	private int materialIndex;
	
	public Cylinder(String[] params)
	{
		centerPosition = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		length = Double.parseDouble(params[3]);
		radius = Double.parseDouble(params[4]);
		rotation = new Point(Double.parseDouble(params[5]), Double.parseDouble(params[6]), Double.parseDouble(params[7]));
		materialIndex = Integer.parseInt(params[8]);
	}
}

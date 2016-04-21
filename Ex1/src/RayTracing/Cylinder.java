package RayTracing;

public class Cylinder extends Surface{

	private Point centerPosition;
	private double length;
	private double radius;
	private Point rotation;
	
	public Cylinder(String[] params)
	{
		super(Integer.parseInt(params[8]));
		centerPosition = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		length = Double.parseDouble(params[3]);
		radius = Double.parseDouble(params[4]);
		rotation = new Point(Double.parseDouble(params[5]), Double.parseDouble(params[6]), Double.parseDouble(params[7]));
		
	}



	@Override
	public double getIntersection(Ray ray) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public Vector getNormal(Vector lightDirection, Point point, boolean withDirection) {
		// TODO Auto-generated method stub
		return null;
	}
}

package RayTracing;

import java.util.Arrays;

public class Camera {

	private Point position;
	private Point lookAt;
	private Point upVector;
	private double screenDistance;
	private double screenWidth;
	
	private Vector direction;
	
	public Camera(String[] params)
	{
		position =  new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
		lookAt = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]));
		
		direction = new Vector(position, lookAt);
		
		upVector = new Point(Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]));
		screenDistance = Double.parseDouble(params[9]);
		screenWidth = Double.parseDouble(params[10]);
	}
	
	
}

package RayTracing;

import java.util.Arrays;

public class Camera {

	private double[] position;
	private double[] lookAt;
	private double[] upVector;
	private double[] screenDistance;
	private double[] screenWidth;
	
	public Camera(String[] params)
	{
		position =  new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		lookAt = new double[]{Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5])};
		upVector = new double[]{Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8])};
		screenDistance = new double[]{Double.parseDouble(params[9])};
		screenWidth = new double[]{Double.parseDouble(params[10])};
	}
	
	
}

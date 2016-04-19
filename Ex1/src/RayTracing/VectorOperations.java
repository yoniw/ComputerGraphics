package RayTracing;

public class VectorOperations {

	public static double getLength(double[] vector)
	{
		double length = 0;
		for (int i = 0; i < vector.length; i++)
		{
			length += vector[i]*vector[i];
		}
		return Math.sqrt(length);
	}
	
	public static double[] normalize(double[] vector)
	{
		double[] normalizedVector = vector;
		for (int i = 0; i < normalizedVector.length; i++)
		{
			normalizedVector[i] /= getLength(vector);
		}
		return normalizedVector;
	}
	
	public static double[] add(double[] vector1, double[] vector2)
	{
		double[] result = new double[vector1.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = vector1[i] + vector2[i];
		}
		return result;
	}
	
	public static Vector add(Point p1, Point p2) {
		return new Vector(
				p1.getX() + p2.getX(),
				p1.getY() + p2.getY(),
				p1.getZ() + p2.getZ()
				);
	}
	
	/**
	 * @return vector1 - vector2
	 */
	public static double[] subtract(double[] vector1, double[] vector2)
	{
		double[] result = new double[vector1.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = vector1[i] - vector2[i];
		}
		return result;
	}
	
	public static Vector subtract(Point p1, Point p2) {
		return new Vector(
				p1.getX() - p2.getX(),
				p1.getY() - p2.getY(),
				p1.getZ() - p2.getZ()
				);
	}
	
	
	public static double[] multiply(double[] vector1, double[] vector2)
	{
		double[] result = new double[vector1.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = vector1[i]*vector2[i];
		}
		return result;
	}
	
	
	public static double[] scalarMult(double scalar, double[] vector)
	{
		double[] result = vector;
		for (int i = 0; i < result.length; i++)
		{
			result[i] *= scalar;
		}
		return result;
	}

	public static Vector scalarMult(double scalar, Vector vector) {
		return new Vector(vector.getX() * scalar, vector.getY() * scalar, vector.getZ() * scalar);
	}
	
	public static double[] invert(double[] vector)
	{
		return scalarMult(-1, vector);
	}
	
	public static double dotProduct(double[] vector1, double[] vector2)
	{
		double result = 0;
		for (int i = 0; i < vector1.length; i++)
		{
			result += vector1[i]*vector2[i];
		}
		return result;
	}
	
	/**
	 * @pre vector1.length == vector2.length == 3
	 */
	public static double[] crossProduct(double[] vector1, double[] vector2)
	{
		double[] result = new double[3];
		result[0]=(vector1[1]*vector2[2]) - (vector1[2]*vector2[1])  ;
		result[1]=(vector1[2]*vector2[0]) - (vector1[0]*vector2[2])  ;
		result[2]=(vector1[0]*vector2[1]) - (vector1[1]*vector2[0])  ;
		return result;
	}

	public static Vector crossProduct(Vector v1, Vector v2) {
		return new Vector(
				(v1.getY() * v2.getZ()) - (v1.getZ() * v2.getY()),
				(v1.getZ() * v2.getX()) - (v1.getX() * v2.getZ()),
				(v1.getX() * v2.getY()) - (v1.getY() * v2.getX())
				);
	}
	
}

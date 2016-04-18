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
	
	public static double[] scalarMult(double scalar, double[] vector)
	{
		double[] result = vector;
		for (int i = 0; i < result.length; i++)
		{
			result[i] *= scalar;
		}
		return result;
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
	
}


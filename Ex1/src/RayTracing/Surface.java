package RayTracing;

public interface Surface {

	public Material getMaterial();
	public double[] getNormal(double[] vector);
	public double getIntersection(Ray ray);
}

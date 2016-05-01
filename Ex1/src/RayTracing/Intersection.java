package RayTracing;

import java.util.Map;
import java.util.TreeMap;

public class Intersection {

	private Ray ray;
	private TreeMap<Double, Surface> intersections;
	
	
	public Intersection(Ray ray) {
		this.ray = ray;
		intersections = new TreeMap<>();
	}
	
	
	public Ray getRay() {
		return ray;
	}
	
	public void addIntersection(double t, Surface s) {
		intersections.put(t, s);
	}
	
	public Map<Double, Surface> getIntersections() {
		return intersections;
	}


	private Surface getClosestIntersectedSurface() {
		return intersections.firstEntry().getValue();
	}


	private Double getClosestDistance() {
		return intersections.firstKey();
	}


	public Surface getNthIntersectedSurface(int currRecursionDepth, int maxRecursionDepth) {
		int counter = 0;
		for (Map.Entry<Double, Surface> entry: intersections.entrySet())
		{
			if (counter == maxRecursionDepth - currRecursionDepth)
			{
				return entry.getValue();
			}
			counter++;
		}
		return getClosestIntersectedSurface();
	}
	
	public Double getNthDistance(int currRecursionDepth, int maxRecursionDepth) {
		int counter = 0;
		for (Map.Entry<Double, Surface> entry: intersections.entrySet())
		{
			if (counter == maxRecursionDepth - currRecursionDepth)
			{
				return entry.getKey();
			}
			counter++;
		}
		return getClosestDistance();
	}
}

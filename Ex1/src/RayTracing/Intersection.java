package RayTracing;

import java.util.Map;
import java.util.TreeMap;

public class Intersection {

	private Ray ray;
	private Map<Double, Surface> intersections;
	
	
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
}
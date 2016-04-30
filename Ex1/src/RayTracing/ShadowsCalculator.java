package RayTracing;

import java.util.Map.Entry;
import java.util.Random;

public class ShadowsCalculator {
	
	private final double EPSILON = 0.00001;

	private Scene scene;
	private Random random;
	int numShadowRays;
	
	public ShadowsCalculator(Scene scene) {

		this.scene = scene;
		this.random = new Random();
		this.numShadowRays = scene.getSettings().getNumberOfShadowRays();
	}
	
	
	public double getIntensity(Intersection intersection, Light light, int currRecursionDepth) {

		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Surface intersectedSurface = intersection.getNthIntersectedSurface(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Point intersectionPoint = intersection.getRay().getPoint(closestDistance);
		Vector lightVector = VectorOperations.subtract(light.getPosition(), intersectionPoint);
		lightVector = VectorOperations.scalarMult(-1, lightVector);
		lightVector.normalize();
		
		
		ScreenSimulator screen = new ScreenSimulator(scene.getCamera(), light, lightVector, numShadowRays);
		
		double totalNumHits = 0;
		
		for (int i = 0; i < numShadowRays; i++) {
			screen.initCurrentV();
			
			for (int j = 0; j < numShadowRays; j++) {
				
				double randIScalar = (i + random.nextDouble()) / numShadowRays;
				double randJScalar = (j + random.nextDouble()) / numShadowRays;
				
				Vector shadow = screen.getCurrentV();
				shadow = VectorOperations.add(shadow, VectorOperations.scalarMult(randIScalar, screen.getXDiff()));
				shadow = VectorOperations.add(shadow, VectorOperations.scalarMult(randJScalar, screen.getYDiff()));
				
				totalNumHits += getTotalShadows(intersectedSurface, intersectionPoint, shadow);
				
				screen.nextX();
			}
			
			screen.nextY();
		}
		
		double hitPercent = totalNumHits / (numShadowRays * numShadowRays);
		
		return 1 - ((1 - hitPercent) * light.getShadowIntensity());
	}
		

	private double getTotalShadows(Surface intersectedSurface, Point intersectionPoint, Point shadowPoint) {

		
		Ray shadowRay = new Ray(shadowPoint, new Vector(intersectionPoint));
		
		Intersection hit = shadowRay.findIntersection(scene);
		if (hit.getIntersections().isEmpty()) {
			return 1;
		}
		
		Vector shadowHitVector = VectorOperations.subtract(shadowRay.getPoint(hit.getNthDistance(scene.getSettings().getMaxNumberOfRecursions(), scene.getSettings().getMaxNumberOfRecursions())), shadowPoint);
		Vector intersectionHitVector = VectorOperations.subtract(intersectionPoint, shadowPoint);
		
		if (shadowHitVector.getLength() > intersectionHitVector.getLength()) {
			// Between light and intersected object, so it does not affect.
			return 1;
			
		} else if (Math.abs(shadowHitVector.getLength() - intersectionHitVector.getLength()) < EPSILON) {
			// Intersected object is same object. Skipping.
			return 1;
		}
		
		
		double totalShadows = 1;
		
		for (Surface surface : hit.getIntersections().values()) {
			if (surface == intersectedSurface) {
				// Intersected with current surface, all other surfaces are hidden.
				break;
			}
			
			totalShadows = totalShadows * surface.getMaterial(scene).getTransparency();
		}
		
		return totalShadows;
	}
}

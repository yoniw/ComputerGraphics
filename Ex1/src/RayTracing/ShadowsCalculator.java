package RayTracing;

import java.util.List;
import java.util.Random;

public class ShadowsCalculator {

	private static Random random = new Random();
	
	public static double getIntensity(Scene scene, Intersection intersection, Light light, int currRecursionDepth) {
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		int numShadowRays = scene.getSettings().getNumberOfShadowRays();
		
		Plane perpendicularPlane = getPerpendicularPlane(scene, intersection, light, currRecursionDepth);
		
		//TODO  define a rectangle on perpendicularPlane centered at the light source ??
		
		double totalNumHits = 0;
		
		for (int i = 0; i < numShadowRays; i++)
		{
			for (int j = 0; j < numShadowRays; j++)
			{
				double randIScalar = (i+random.nextDouble())/numShadowRays;
				double randJScalar = (j+random.nextDouble())/numShadowRays;
				
				Vector v = null; //TODO calculate v using perpendicularPlane, randIScalar and randJScalar
				Point p0 = intersection.getRay().getPoint(closestDistance);
				
				Ray shadowRay = new Ray(p0, v);
				
				double totalShadows = 0;			
				List<Surface> intersectedSurfaces = null; //TODO use shadowRay to intersect surfaces
				for (Surface surface : intersectedSurfaces)
				{
					//TODO
				}
				totalNumHits += totalShadows;
			}
		}
		
		// TODO 
		return 1;
		
	}

	
	// TODO
	private static Plane getPerpendicularPlane(Scene scene, Intersection intersection, Light light, int currRecursionDepth) {
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		
		Vector lightIntersectionVector = VectorOperations.subtract(intersection.getRay().getPoint(closestDistance), light.getPosition());
		double offset = VectorOperations.dotProduct(lightIntersectionVector, light.getPosition());
		
		
		//TODO 
		
		
		return new Plane(null,offset);
	}

}

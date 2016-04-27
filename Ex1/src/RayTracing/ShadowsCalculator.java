package RayTracing;

import java.util.Random;

public class ShadowsCalculator {
	
	private final double EPSILON = 0.0000000000001;

	private Scene scene;
	private Random random;
	
	
	public ShadowsCalculator(Scene scene) {

		this.scene = scene;
		this.random = new Random();
	}
	
	
	public double getIntensity(Intersection intersection, Light light, int currRecursionDepth) {

		int numShadowRays = scene.getSettings().getNumberOfShadowRays();

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
	

	public double getShadowCoeff(Light light, Intersection intersection, int currRecursionDepth) {

		int numShadowRays = scene.getSettings().getNumberOfShadowRays();

		double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Point intersectionPoint = intersection.getRay().getPoint(closestDistance);
		Vector lightVector = new Vector(light.getPosition(), intersectionPoint);
		
		double planeOffset = VectorOperations.dotProduct(lightVector, light.getPosition());
		Vector planeVector1 = getPlaneVector(lightVector, planeOffset);
		planeVector1.normalize();
		planeVector1 = VectorOperations.scalarMult(light.getLightWidth() / 2, planeVector1);
		Vector planeVector2 = VectorOperations.crossProduct(lightVector, planeVector1);
		planeVector2.normalize();
		planeVector2 = VectorOperations.scalarMult(light.getLightWidth() / 2, planeVector2);
		Vector rectStart = VectorOperations.subtract(light.getPosition(), VectorOperations.scalarMult(0.5, planeVector1));
		rectStart = VectorOperations.subtract(rectStart, VectorOperations.scalarMult(0.5, planeVector1));
		Random r = new Random();
		double numOfHits = 0;
		for (int i = 0; i < numShadowRays; i++) {
			for (int j = 0; j < numShadowRays; j++) {
				double coeff1 = r.nextDouble();
				double coeff2 = r.nextDouble();
				Vector rayStart = VectorOperations.add(rectStart, VectorOperations.scalarMult((i + coeff1) / numShadowRays, planeVector1));
				rayStart = VectorOperations.add(rayStart, VectorOperations.scalarMult((j + coeff2) / numShadowRays, planeVector2));
				Ray ray = new Ray(rayStart, new Vector(intersectionPoint));

				double acummelateShadow = 1;
				double distanceToLight = VectorOperations.getDistance(intersectionPoint, rayStart) - EPSILON; //epsilon

				Intersection hit = ray.findIntersection(scene);
				for (double t : hit.getIntersections().keySet()) {
					intersectionPoint = hit.getRay().getPoint(t);
					if (VectorOperations.getDistance(intersectionPoint, rayStart) < distanceToLight) {
						acummelateShadow *= hit.getIntersections().get(t).getMaterial(scene).getTransparency();
					}
				}
				numOfHits += acummelateShadow;

			}
		}

		return 1-light.getShadowIntensity() + light.getShadowIntensity()*numOfHits/(numShadowRays*numShadowRays);
	}
	
    private Vector getPlaneVector(Vector normal, double offset){
        double x=0,y=0,z=0;

        if (normal.getZ() != 0) {
            z = offset ;
        }
        else if(normal.getY() != 0) {
            y = offset;
        }
        else {
            x = offset;
        }
        return new Vector(x,y,z);
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

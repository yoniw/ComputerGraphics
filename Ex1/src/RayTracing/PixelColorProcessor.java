package RayTracing;

import java.util.List;

public class PixelColorProcessor {

	private final static double EPSILON = 0.0000000000001;
	
	public static RGB getColor(Scene scene, Intersection intersection, Ray reflectionRay, int currRecursionDepth)
	{
		Intersection currIntersection;
		if (reflectionRay == null)
		{
			currIntersection = intersection;
		}
		else
		{
			currIntersection = reflectionRay.findIntersection(scene);
		}
		
		//System.out.println("current recursion depth: " + currRecursionDepth);	
		if ((currIntersection == null) || (currIntersection.getIntersections().isEmpty()))
		{
			// ray didn't hit any object
			return scene.getSettings().getBackgroundColor();
		}
		
		RGB resultColor = getOriginalColor(scene, currIntersection, currRecursionDepth);
		
		if (currRecursionDepth == 0)
		{
			return resultColor;
		}
	
		Surface closestIntersectedObject = currIntersection.getNthIntersectedSurface(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Material material = closestIntersectedObject.getMaterial(scene);
		
		// transparency 
		resultColor = VectorOperations.scalarMult((1-material.getTransparency()), resultColor);
		RGB transparencyColor = computeTransparency(scene, currIntersection, currRecursionDepth);
		transparencyColor = VectorOperations.scalarMult(material.getTransparency(), transparencyColor);
		resultColor = VectorOperations.add(resultColor, transparencyColor);

//		// reflection 
		RGB reflectionColor = computeReflectionColor(scene, currIntersection, currRecursionDepth);
		resultColor = VectorOperations.add(resultColor, reflectionColor);
	
		return resultColor;
		
	}

	private static RGB getOriginalColor(Scene scene, Intersection intersection, int currRecursionDepth) {	
		RGB resultColor = new RGB(0,0,0);
		
		List<Light> lightsList = scene.getLightsList();
		
		//TODO ignore lights that don't hit the surface? 
		for (Light light : lightsList)
		{
			// diffuse
			RGB diffuseColor = getOriginalDiffuseColor(scene, light, intersection, currRecursionDepth);
			// specular 
			RGB specularColor = getOriginalSpecularColor(scene, light, intersection, currRecursionDepth);
			resultColor = VectorOperations.add(specularColor, VectorOperations.add(resultColor, diffuseColor));
			// shadows
//			resultColor = VectorOperations.scalarMult(ShadowsCalculator.getIntensity(scene,intersection,light,currRecursionDepth), resultColor);
		}
		return resultColor;
		
	}
	
	private static RGB computeTransparency(Scene scene, Intersection intersection, int currRecursionDepth) {
		
		Surface closestIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth,scene.getSettings().getMaxNumberOfRecursions());
		Material material = closestIntersectedObject.getMaterial(scene);
		RGB resultColor = new RGB(0,0,0);
		
		if (material.getTransparency() == 0)
		{
			return resultColor;
		}

		return getColor(scene, intersection,null,currRecursionDepth-1);
		
	}

	private static RGB computeReflectionColor(Scene scene, Intersection intersection, int currRecursionDepth) {
		Surface closestIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth,scene.getSettings().getMaxNumberOfRecursions());
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Material material = closestIntersectedObject.getMaterial(scene);
		
		RGB resultColor = new RGB(0,0,0);
		
		
		RGB reflectionColor = material.getReflectionColor();
		if ((0 == reflectionColor.getRed()) && (0 == reflectionColor.getGreen()) && (0 == reflectionColor.getBlue()))
		{
			return resultColor;
		}
		
		Ray ray = intersection.getRay();
		Vector invertedRayVector = VectorOperations.invert(ray.getV());
		Point intersectionPosition = intersection.getRay().getPoint(closestDistance);
		Vector reflectionVector = VectorOperations.invert(VectorOperations.subtract(intersectionPosition, invertedRayVector));
		Vector normalToReflection = closestIntersectedObject.getNormal(reflectionVector, intersectionPosition, true);
		
		
		Vector reflectionDirection = VectorOperations.scalarMult(2, invertedRayVector);
		normalToReflection.normalize();
		reflectionDirection = VectorOperations.scalarMult(VectorOperations.dotProduct(normalToReflection, reflectionDirection), normalToReflection);
		reflectionDirection = VectorOperations.subtract(reflectionDirection, invertedRayVector);
		reflectionDirection.normalize();
		
		Point p0 = VectorOperations.add(intersectionPosition, reflectionDirection);
		p0 = VectorOperations.scalarMult(EPSILON, p0); // so we wouldn't hit the same spot over and over
		Point p1 = VectorOperations.add(p0, reflectionDirection);
		Vector rayVector = new Vector(p0,p1);
		
		Ray reflectionRay = new Ray(p1, rayVector);
		
		return getColor(scene, intersection, reflectionRay, currRecursionDepth-1);
	}
	
	private static RGB getOriginalSpecularColor(Scene scene, Light light, Intersection intersection, int currRecursionDepth) {
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Surface firstIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Material material = firstIntersectedObject.getMaterial(scene);
		
		RGB basicSpecularColor = material.getSpecularColor();
		RGB basicSpecularColorMULTlightColor = VectorOperations.multiply(basicSpecularColor, light.getColor());
		
		Vector normalizedLightDirection = VectorOperations.normalize(VectorOperations.subtract(light.getPosition(), intersection.getRay().getPoint(closestDistance)));
		Vector reflectionVector = VectorOperations.subtract(normalizedLightDirection, intersection.getRay().getPoint(closestDistance));
		Point intersectionPosition = intersection.getRay().getPoint(closestDistance);
		Vector normalToIntersectedObject = VectorOperations.invert(firstIntersectedObject.getNormal(reflectionVector, intersectionPosition, true));
		
		
		Vector reflectionDirection = VectorOperations.scalarMult(2, normalizedLightDirection);
		normalToIntersectedObject.normalize();
		reflectionDirection = VectorOperations.scalarMult(VectorOperations.dotProduct(normalToIntersectedObject, reflectionDirection), normalToIntersectedObject);
		reflectionDirection = VectorOperations.subtract(reflectionDirection, normalizedLightDirection);
		reflectionDirection.normalize();
		
		double angle = computeAngle(reflectionDirection, intersection);
		double angelPOWphong = Math.pow(angle, material.getPhong());
		double intensity = angelPOWphong * light.getSpecularIntensity();
		
		return  VectorOperations.scalarMult(intensity, basicSpecularColorMULTlightColor);
	}

	private static double computeAngle(Vector reflectionDirection, Intersection intersection) {
		double angle;
		Ray ray = intersection.getRay();
		Vector invertedRayVector = VectorOperations.invert(ray.getV());
		
		invertedRayVector.normalize();
		reflectionDirection.normalize();
		if ((angle = VectorOperations.dotProduct(reflectionDirection, invertedRayVector)) > 0)
		{
			return angle;
		}
		return 0;
	}

	private static RGB getOriginalDiffuseColor(Scene scene, Light light, Intersection intersection, int currRecursionDepth) {
		
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Surface firstIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Material material = firstIntersectedObject.getMaterial(scene);
		
		Vector lightDirection = new Vector(intersection.getRay().getPoint(closestDistance), light.getPosition());
		Point intersectionPosition = intersection.getRay().getPoint(closestDistance);
		Vector normalToIntersectedObject = firstIntersectedObject.getNormal(lightDirection, intersectionPosition, true);
		double intensity = VectorOperations.dotProduct(VectorOperations.normalize(normalToIntersectedObject), VectorOperations.normalize(lightDirection));
		intensity = Math.abs(intensity); // intensity may come out negative
		
		RGB lightColor = light.getColor();
		RGB materialOriginalDiffuseColor = material.getDiffuseColor();
		
		RGB lightColorMULTdiffuseColor = VectorOperations.multiply(lightColor, materialOriginalDiffuseColor);
		return VectorOperations.scalarMult(intensity, lightColorMULTdiffuseColor);
	}
	
	
}

package RayTracing;

import java.util.List;

public class PixelColorProcessor {

	private final double EPSILON = 0.00001;
	
	public RGB getColor(Scene scene, Intersection intersection, Ray reflectionRay, int currRecursionDepth)
	{
		Intersection currIntersection = (reflectionRay == null? intersection : reflectionRay.findIntersection(scene));
		 
		//System.out.println("current recursion depth: " + currRecursionDepth);	
		if ((currIntersection == null) || (currIntersection.getIntersections().isEmpty()))
		{
			// ray didn't hit any object
			return scene.getSettings().getBackgroundColor();
		}
		
		RGB resultColor = getOriginalColor(scene, currIntersection, currRecursionDepth, reflectionRay);
		
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

		// reflection 
		RGB reflectionColor = computeReflectionColor(scene, currIntersection, currRecursionDepth);
		resultColor = VectorOperations.add(resultColor, reflectionColor);
	
		return resultColor;
		
	}

	private RGB getOriginalColor(Scene scene, Intersection intersection, int currRecursionDepth, Ray reflectionRay) {	
		RGB resultColor = new RGB(0,0,0);
		
		List<Light> lightsList = scene.getLightsList();
		ShadowsCalculator shadowsCalc = new ShadowsCalculator(scene);
		
		for (Light light : lightsList)
		{
			
			if (null== reflectionRay && !doesLightHit(scene,light,intersection,currRecursionDepth))
			{
				continue;
			}
			
			// diffuse
			RGB diffuseColor = getOriginalDiffuseColor(scene, light, intersection, currRecursionDepth);
			// specular 
			RGB specularColor = getOriginalSpecularColor(scene, light, intersection, currRecursionDepth);
			resultColor = VectorOperations.add(specularColor, VectorOperations.add(resultColor, diffuseColor));
			// shadows
			resultColor = VectorOperations.scalarMult(shadowsCalc.myGetIntensity(intersection,light,currRecursionDepth), resultColor);
//			resultColor = VectorOperations.scalarMult(shadowsCalc.getIntensity(intersection,light,currRecursionDepth), resultColor);
		}
		return resultColor;
		
	}
	
	//checks whether light is visible in the given intersection
	private boolean doesLightHit(Scene scene, Light light, Intersection intersection, int currRecursionDepth) {
		Vector rayVector = intersection.getRay().getV();
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Point intersectionPosition = intersection.getRay().getPoint(closestDistance);
		Surface closestIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth,scene.getSettings().getMaxNumberOfRecursions());
		if (closestIntersectedObject instanceof Plane)
		{
			return true;
		}
		Vector normal = closestIntersectedObject.getNormal(rayVector, intersectionPosition, true); 
		Vector lightDirection = VectorOperations.subtract(intersectionPosition, light.getPosition());
		
		if (VectorOperations.dotProduct(normal, lightDirection) < EPSILON)
		{
			return false;
		}
		
		return true;
	}

	private RGB computeTransparency(Scene scene, Intersection intersection, int currRecursionDepth) {
		
		Surface closestIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth,scene.getSettings().getMaxNumberOfRecursions());
		Material material = closestIntersectedObject.getMaterial(scene);
		RGB resultColor = new RGB(0,0,0);
		
		if (material.getTransparency() == 0)
		{
			return resultColor;
		}

		return getColor(scene, intersection,null,currRecursionDepth-1);
		
	}

	private RGB computeReflectionColor(Scene scene, Intersection intersection, int currRecursionDepth) {
		Surface closestIntersectedObject = intersection.getNthIntersectedSurface(currRecursionDepth,scene.getSettings().getMaxNumberOfRecursions());
		Double closestDistance = intersection.getNthDistance(currRecursionDepth, scene.getSettings().getMaxNumberOfRecursions());
		Material material = closestIntersectedObject.getMaterial(scene);
		
		RGB resultColor = new RGB(0,0,0);
	
		RGB reflectionColor = material.getReflectionColor();
		if ((0 == reflectionColor.getRed()) && (0 == reflectionColor.getGreen()) && (0 == reflectionColor.getBlue()))
		{
			return resultColor;
		}

		Vector invertedRayVector = VectorOperations.invert(intersection.getRay().getV()); 
		Point intersectionPosition = intersection.getRay().getPoint(closestDistance);
	
		Vector tmp = VectorOperations.subtract(invertedRayVector, intersectionPosition);
		Vector normal = VectorOperations.invert(closestIntersectedObject.getNormal(tmp, intersectionPosition, true));

		Vector reflectionDirection = VectorOperations.scalarMult(2, invertedRayVector);
		Vector normalMULTcos = VectorOperations.scalarMult(VectorOperations.dotProduct(reflectionDirection, normal), normal);
		reflectionDirection = VectorOperations.subtract(normalMULTcos, invertedRayVector);
		reflectionDirection.normalize();

		Point p0 = VectorOperations.add(intersectionPosition, VectorOperations.scalarMult(EPSILON, reflectionDirection));
		Point p1 = VectorOperations.add(reflectionDirection, p0);
		Ray reflectionRay = new Ray(p0,new Vector(p1));
		
		return VectorOperations.multiply(reflectionColor,getColor(scene, intersection, reflectionRay, currRecursionDepth-1));
	}
	
	private RGB getOriginalSpecularColor(Scene scene, Light light, Intersection intersection, int currRecursionDepth) {
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

	private double computeAngle(Vector reflectionDirection, Intersection intersection) {
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

	private RGB getOriginalDiffuseColor(Scene scene, Light light, Intersection intersection, int currRecursionDepth) {
		
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

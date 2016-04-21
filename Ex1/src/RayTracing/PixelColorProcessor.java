package RayTracing;

import java.util.List;

public class PixelColorProcessor {

	private final double EPSILON = 0.0000000000001;
	
	public static RGB getColor(Scene scene, Intersection intersection, Ray ray)
	{
			
		if (intersection == null)
		{
			// ray didn't hit any object
			return scene.getSettings().getBackgroundColor();
		}
		
		RGB resultColor = getOriginalColor(scene, intersection);
		
//		if (scene.getSettings().getMaxNumberOfRecursions() == 0)
//		{
			return resultColor;
//		}
//		
//		Surface firstIntersectedObject = intersection.getIntersections().get(0);
//		Material material = firstIntersectedObject.getMaterial(scene);
//		
//		// transparency 
//		resultColor = VectorOperations.scalarMult((1-material.getTransparency()), resultColor);
//		RGB transparencyColor = computeTransparency(scene, intersection);
//		transparencyColor = VectorOperations.scalarMult(material.getTransparency(), transparencyColor);
//		resultColor = VectorOperations.add(resultColor, transparencyColor);
//		
//		// reflection 
//		RGB reflectionColor = computeReflectionColor(scene, intersection);
//		resultColor = VectorOperations.add(resultColor, reflectionColor);
//		
//		return resultColor;
		
	}

	private static RGB getOriginalColor(Scene scene, Intersection intersection) {	
		RGB resultColor = new RGB(0,0,0);
		
		List<Light> lightsList = scene.getLightsList();
		
		//TODO ignore lights that don't hit the surface? 
		for (Light light : lightsList)
		{
			resultColor = VectorOperations.add(resultColor, getOriginalDiffuseColor(scene, light, intersection));
//			resultColor = VectorOperations.add(resultColor, getOriginalSpecularColor(scene, light, intersection));
			
		}
		return resultColor;
		
	}
	
//	private static RGB computeTransparency(Scene scene, Intersection intersection) {
//		
//		Surface firstIntersectedObject = intersection.getIntersections().get(0);
//		Material material = firstIntersectedObject.getMaterial(scene);
//		RGB resultColor = new RGB(0,0,0);
//		
//		if (material.getTransparency() == 0)
//		{
//			return resultColor;
//		}
//		
//		double[] invertedRayDirection = VectorOperations.invert(intersection.getRayDirection());
//		Ray transparencyRay = new Ray(invertedRayDirection, intersection.getPosition());
//		
//		// TODO work with a recursionDepth param instead
//		scene.getSettings().minusOneReduction();
//		return getColor(scene, intersection, transparencyRay);
//		
//	}

//	private static RGB computeReflectionColor(Scene scene, Intersection intersection) {
//		Surface firstIntersectedObject = intersection.getIntersections().get(0);
//		Material material = firstIntersectedObject.getMaterial(scene);
//		RGB resultColor = new RGB(0,0,0);
//		
//		
//		double[] reflectionColor = material.getReflectionColor().getRGBAsDoubleArr();
//		if ((0 == reflectionColor[0]) && (0 == reflectionColor[1]) && (0== reflectionColor[2]))
//		{
//			return resultColor;
//		}
//		
//		double[] reflectionVector = VectorOperations.subtract(intersection.getRayDirection(), intersection.getPosition());
//		double[] normalToIntersectedObject = firstIntersectedObject.getNomral(reflectionVector);
//		double[] reflectionDirection = VectorOperations.subtract( VectorOperations.scalarMult(VectorOperations.dotProduct(normalToIntersectedObject, normalizedLightDirection), normalToIntersectedObject), normalizedLightDirection);
//		double[] normalizedReflectionDirection = VectorOperations.normalize(reflectionDirection);
//		
//		Ray reflectionRay = new Ray(normalizedReflectionDirection, intersection.getPosition());
//		
//		// TODO work with a recursionDepth param instead
//		scene.getSettings().minusOneReduction();
//		double[] computedColorReflection = getColor(scene, intersection, reflectionRay).getRGBAsDoubleArr();
//		
//		return VectorOperations.multiply(material.getReflectionColor(), computedColorReflection);
//	}
	
//	private static RGB getOriginalSpecularColor(Scene scene, Light light, Intersection intersection) {
//		Double closestDistance = intersection.getClosestDistance();
//		Surface firstIntersectedObject = intersection.getClosestIntersectedSurface();
//		Material material = firstIntersectedObject.getMaterial(scene);
//		
//		RGB basicSpecularColor = material.getSpecularColor();
//		RGB basicSpecularColorMULTlightColor = VectorOperations.multiply(basicSpecularColor, light.getColor());
//		
//		Vector normalizedLightDirection = VectorOperations.normalize(VectorOperations.subtract(light.getPosition(), intersection.getRay().getPoint(closestDistance)));
//		Vector reflectionVector = VectorOperations.subtract(normalizedLightDirection, intersection.getRay().getPoint(closestDistance));
//		Vector normalToIntersectedObject = firstIntersectedObject.getNomral(reflectionVector);
//		Vector reflectionDirection = VectorOperations.subtract( VectorOperations.scalarMult(VectorOperations.dotProduct(normalToIntersectedObject, normalizedLightDirection), normalToIntersectedObject), normalizedLightDirection);
//		Vector normalizedReflectionDirection = VectorOperations.normalize(reflectionDirection);
//		
//		double angel = VectorOperations.dotProduct(VectorOperations.normalize(intersection.getRayDirection()), normalizedReflectionDirection);
//		double angelPOWphong = Math.pow(angel, material.getPhong());
//		double intensity = angelPOWphong * light.getSpecularIntensity();
//		intensity = Math.abs(intensity); // intensity may come out negative
//		
//		return  VectorOperations.scalarMult(intensity, basicSpecularColorMULTlightColor);
//		
//	}

	private static RGB getOriginalDiffuseColor(Scene scene, Light light, Intersection intersection) {
		
		Double closestDistance = intersection.getClosestDistance();
		Surface firstIntersectedObject = intersection.getClosestIntersectedSurface();
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

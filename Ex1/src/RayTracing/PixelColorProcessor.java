package RayTracing;

import java.util.List;


//TODO Object -> Intersection
//TODO we should intersect the ray INSIDE getColor() (because we call it recursively afterwards with new rays)
//TODO pass recursionDepth as a parameter. it wouldn't work otherwise.


public class PixelColorProcessor {

	
	public static RGB getColor(Scene scene, Object intersection, Ray ray)
	{
			
		if (intersection == null)
		{
			// ray didn't hit any object
			return scene.getSettings().getBackgroundColor();
		}
		
		double[] resultColor = getOriginalColor(scene, intersection);
		
		if (scene.getSettings().getMaxNumberOfRecursions() == 0)
		{
			return new RGB(resultColor);
		}
		
		Surface intersectedObject = intersection.getSurface();
		Material material = intersectedObject.getMaterial();
		
		// transparency 
		resultColor = VectorOperations.scalarMult((1-material.getTransparency()), resultColor);
		double[] transparencyColor = computeTransparency(scene, intersection);
		transparencyColor = VectorOperations.scalarMult(material.getTransparency(), transparencyColor);
		resultColor = VectorOperations.add(resultColor, transparencyColor);
		
		// reflection 
		double[] reflectionColor = computeReflectionColor(scene, intersection);
		resultColor = VectorOperations.add(resultColor, reflectionColor);
		
		return new RGB(resultColor);
		
	}

	private static double[] getOriginalColor(Scene scene, Object intersection) {	
		double[] resultColor = {0,0,0};
		
		List<Light> lightsList = scene.getLightsList();
		
		//TODO ignore lights that don't hit the surface? 
		for (Light light : lightsList)
		{
			resultColor = VectorOperations.add(resultColor, getOriginalDiffuseColor(light, intersection));
			resultColor = VectorOperations.add(resultColor, getOriginalSpecularColor(light, intersection));
			
		}
		return resultColor;
		
	}
	
	private static double[] computeTransparency(Scene scene, Object intersection) {
		Surface intersectedObject = intersection.getSurface();
		Material material = intersectedObject.getMaterial();
		double[] resultColor = {0,0,0};
		
		if (material.getTransparency() == 0)
		{
			return resultColor;
		}
		
		double[] invertedRayDirection = VectorOperations.invert(intersection.getRayDirection());
		Ray transparencyRay = new Ray(invertedRayDirection, intersection.getPosition());
		
		// TODO work with a recursionDepth param instead
		scene.getSettings().minusOneReduction();
		return getColor(scene, intersection, transparencyRay).getRGBAsDoubleArr();
		
	}

	private static double[] computeReflectionColor(Scene scene, Object intersection) {
		Surface intersectedObject = intersection.getSurface();
		Material material = intersectedObject.getMaterial();
		double[] resultColor = {0,0,0};
		
		
		double[] reflectionColor = material.getReflectionColor().getRGBAsDoubleArr();
		if ((0 == reflectionColor[0]) && (0 == reflectionColor[1]) && (0== reflectionColor[2]))
		{
			return resultColor;
		}
		
		double[] reflectionVector = VectorOperations.subtract(intersection.getRayDirection(), intersection.getPosition());
		double[] normalToIntersectedObject = intersectedObject.getNomral(reflectionVector);
		double[] reflectionDirection = VectorOperations.subtract( VectorOperations.scalarMult(VectorOperations.dotProduct(normalToIntersectedObject, normalizedLightDirection), normalToIntersectedObject), normalizedLightDirection);
		double[] normalizedReflectionDirection = VectorOperations.normalize(reflectionDirection);
		
		Ray reflectionRay = new Ray(normalizedReflectionDirection, intersection.getPosition());
		
		// TODO work with a recursionDepth param instead
		scene.getSettings().minusOneReduction();
		double[] computedColorReflection = getColor(scene, intersection, reflectionRay).getRGBAsDoubleArr();
		
		return VectorOperations.multiply(material.getReflectionColor().getRGBAsDoubleArr(), computedColorReflection);
	}
	
	private static double[] getOriginalSpecularColor(Light light, Object intersection) {
		Surface intersectedObject = intersection.getSurface();
		Material material = intersectedObject.getMaterial();
		
		double[] basicSpecularColor = material.getSpecularColor().getRGBAsDoubleArr();
		double[] basicSpecularColorMULTlightColor = VectorOperations.multiply(basicSpecularColor, light.getColor());
		
		double[] normalizedLightDirection = VectorOperations.normalize(VectorOperations.subtract(light.getPosition(), intersection.getPosition()));
		double[] reflectionVector = VectorOperations.subtract(normalizedLightDirection, intersection.getPosition());
		double[] normalToIntersectedObject = intersectedObject.getNomral(reflectionVector);
		double[] reflectionDirection = VectorOperations.subtract( VectorOperations.scalarMult(VectorOperations.dotProduct(normalToIntersectedObject, normalizedLightDirection), normalToIntersectedObject), normalizedLightDirection);
		double[] normalizedReflectionDirection = VectorOperations.normalize(reflectionDirection);
		
		double angel = VectorOperations.dotProduct(VectorOperations.normalize(intersection.getRayDirection()), normalizedReflectionDirection);
		double angelPOWphong = Math.pow(angel, material.getPhong());
		double intensity = angelPOWphong * light.getSpecularIntensity();
		intensity = Math.abs(intensity); // intensity may come out negative
		
		return  VectorOperations.scalarMult(intensity, basicSpecularColorMULTlightColor);
		
	}

	private static double[] getOriginalDiffuseColor(Light light, Object intersection) {
		
		Surface intersectedObject = intersection.getSurface();
		Material material = intersectedObject.getMaterial();
		
		double[] lightDirection = VectorOperations.subtract(light.getPosition(), intersection.getPosition());
		double[] normalToIntersectedObject = intersectedObject.getNormal(lightDirection);
		double intensity = VectorOperations.dotProduct(VectorOperations.normalize(normalToIntersectedObject), VectorOperations.normalize(lightDirection));
		intensity = Math.abs(intensity); // intensity may come out negative
		
		double[] lightColor = light.getColor();
		double[] materialOriginalDiffuseColor = material.getDiffuseColor().getRGBAsDoubleArr();
		double[] lightColorMULTdiffuseColor = VectorOperations.multiply(lightColor, materialOriginalDiffuseColor);
		
		return VectorOperations.scalarMult(intensity, lightColorMULTdiffuseColor);
	}
	
	
}

package RayTracing;

import java.util.List;

public abstract class Surface {

	protected int materialIndex;
	
	public Surface(int materialIndex)
	{
		this.materialIndex = materialIndex;
	}
	
	public Material getMaterial(Scene scene)
	{
		List<Material> materialsList = scene.getMaterialsList();
		return materialsList.get(materialIndex-1);
	}
	
	public abstract double getIntersection(Ray ray);

	public abstract Vector getNormal(Vector lightDirection, Point point, boolean withDirection);
}

package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	
	private Camera cam;
	private Settings set;
	private List<Material> materialsList;
	private List<Sphere> spheresList;
	private List<Plane> planesList;
	private List<Cylinder> cylindersList;
	private List<Light> lightsList;

	public Scene(Camera cam, Settings set, List<Material> materialsList, List<Sphere> spheresList,
			List<Plane> planesList, List<Cylinder> cylindersList, List<Light> lightsList) {
		this.cam = cam;
		this.set = set;
		this.materialsList = materialsList;
		this.spheresList = spheresList;
		this.planesList = planesList;
		this.cylindersList = cylindersList;
		this.lightsList = lightsList;
	}

	public Camera getCamera()
	{
		return cam;
	}
	
	public Settings getSettings()
	{
		return set;
	}
	
	public List<Light> getLightsList()
	{
		return lightsList;
	}
	
	public List<Sphere> getSpheresList() {
		return spheresList;
	}
	
	public List<Plane> getPlanesList() {
		return planesList;
	}
	
	public List<Cylinder> getCylindersList() {
		return cylindersList;
	}
	
	public List<Surface> getSurfaces() {
		List<Surface> surfaces = new ArrayList<Surface>();
		surfaces.addAll(spheresList);
		surfaces.addAll(planesList);
		surfaces.addAll(cylindersList);
		return surfaces;
	}
}

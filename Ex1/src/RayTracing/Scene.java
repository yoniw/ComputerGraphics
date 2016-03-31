package RayTracing;

import java.util.List;

public class Scene {
	
	Camera cam;
	Settings set;
	List<Material> materialsList;
	List<Sphere> spheresList;
	List<Plane> planesList;
	List<Cylinder> cylindersList;
	List<Light> lightsList;

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

	
}

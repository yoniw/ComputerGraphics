package RayTracing;

public class Material {

	private RGB diffuseColorRGB;
	private RGB specularColorRGB;
	private RGB reflectionColorRGB;
	private double shininess;
	private double transparency;
	//private double incidence; //bonus
	
	public Material(String[] params)
	{
		diffuseColorRGB = new RGB(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
		specularColorRGB = new RGB(Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]));
		reflectionColorRGB = new RGB(Integer.parseInt(params[6]), Integer.parseInt(params[7]), Integer.parseInt(params[8]));
		shininess = Double.parseDouble(params[9]);
		transparency = Double.parseDouble(params[10]);
		//incidence = Double.parseDouble(params[11]);
	}
}

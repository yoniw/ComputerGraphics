package RayTracing;

public class Material {

	private double[] diffuseColorRGB;
	private double[] specularColorRGB;
	private double[] reflectionColorRGB;
	private double[] shininess;
	private double[] transparency;
	//private double[] incidence; //bonus
	
	public Material(String[] params)
	{
		diffuseColorRGB = new double[]{Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2])};
		specularColorRGB = new double[]{Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5])};
		reflectionColorRGB = new double[]{Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8])};
		shininess = new double[]{Double.parseDouble(params[9])};
		transparency = new double[]{Double.parseDouble(params[10])};
		//incidence = new double[]{Double.parseDouble(params[11])};
	}
}

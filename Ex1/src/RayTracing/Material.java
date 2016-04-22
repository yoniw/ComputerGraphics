package RayTracing;

public class Material {

	private RGB diffuseColorRGB;
	private RGB specularColorRGB;
	private RGB reflectionColorRGB;
	private double phong;
	private double transparency;
	//private double incidence; //bonus
	
	public Material(String[] params)
	{
		diffuseColorRGB = new RGB(Float.parseFloat(params[0]), Float.parseFloat(params[1]), Float.parseFloat(params[2]));
		specularColorRGB = new RGB(Float.parseFloat(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
		reflectionColorRGB = new RGB(Float.parseFloat(params[6]), Float.parseFloat(params[7]), Float.parseFloat(params[8]));
		phong = Double.parseDouble(params[9]);
		transparency = Double.parseDouble(params[10]);
		//incidence = Double.parseDouble(params[11]);
	}
	
	public RGB getDiffuseColor()
	{
		return diffuseColorRGB;
	}
	
	public RGB getSpecularColor()
	{
		return specularColorRGB;
	}
	
	public RGB getReflectionColor()
	{
		return reflectionColorRGB;
	}

	public double getTransparency() {
		return transparency;
	}
	
	public double getPhong()
	{
		return phong;
	}
}

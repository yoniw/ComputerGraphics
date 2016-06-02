
public class Pixel {

	int i;
	int j;

	private int original_i;
	private int original_j;
	
	public Pixel(Pixel pixel)
	{
		this.i = pixel.i;
		this.j = pixel.j;
		this.original_i = pixel.getOriginal_i();
		this.original_j = pixel.getOriginal_j();
	}
	
	public Pixel(int i, int j)
	{
		this.i=i;
		this.j=j;
	}
	
	// for debugging
	public String toString()
	{
		return "<" + i + ", " + j + ">";  
	}

	public void setOriginal_i(int i) {
		original_i = i;	
	}
	public void setOriginal_j(int j) {
		original_j = j;
	}
	
	public int getOriginal_i(){
		return original_i;
	}
	
	public int getOriginal_j(){
		return original_j;
	}
}

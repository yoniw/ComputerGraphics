import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColorMatrix {

	private int height;
	private int width;
	
	Color[][] matrix; // TODO: I guess this is public because of width/height confusion. need to be removed.
	private Pixel[][] pixelsMatrix;

	
	public ColorMatrix(BufferedImage currImage) {
		height = currImage.getHeight();
		width = currImage.getWidth();
		
		matrix = new Color[width][height];
		pixelsMatrix = new Pixel[width][height];
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				matrix[i][j] = new Color(currImage.getRGB(i, j));
				pixelsMatrix[i][j] = new Pixel(i,j);
				pixelsMatrix[i][j].setOriginal_i(i);
				pixelsMatrix[i][j].setOriginal_j(j);
			}
		}
	}

	public ColorMatrix(int height, int width) {
		this.height = height;
		this.width = width;
		matrix = new Color[width][height];
		pixelsMatrix = new Pixel[width][height];
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public Color getRGB(int i, int j) {
		return matrix[i][j];
	}

	public Pixel getPixel(int i, int j) {
		return pixelsMatrix[i][j];
	}

	public Pixel[][] getPixelsMatrix() {
		return pixelsMatrix;
	}

	public void setPixelsMatrix(Pixel[][] pixelsMatrix) {
		this.pixelsMatrix = pixelsMatrix;
		
	}

}

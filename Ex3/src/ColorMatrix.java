import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColorMatrix extends Matrix<Color> {

	private Pixel[][] pixelsMatrix;

	public ColorMatrix(ColorMatrix copyOfMatrixRep) {
		super(copyOfMatrixRep.getRows(), copyOfMatrixRep.getCols(), copyOfMatrixRep.getData());		
		
		pixelsMatrix = new Pixel[copyOfMatrixRep.getRows()][copyOfMatrixRep.getCols()];
		for (int i = 0; i < getRows(); i++)
		{
			for (int j = 0; j < getCols(); j++)
			{
				Pixel other = copyOfMatrixRep.getPixel(i, j);
				if (other == null)
				{
					pixelsMatrix[i][j] = null;
				}
				else
				{
					pixelsMatrix[i][j] = new Pixel(other);
				}
			}
		}
	}
	
	public ColorMatrix(BufferedImage img) {
		super(img.getHeight(), img.getWidth());
		
		pixelsMatrix = new Pixel[img.getHeight()][img.getWidth()];
		
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				
				setCell(i, j, new Color(img.getRGB(j, i)));

				pixelsMatrix[i][j] = new Pixel(i,j);
				pixelsMatrix[i][j].setOriginalRow(i);
				pixelsMatrix[i][j].setOriginalCol(j);
			}
		}
	}

	public ColorMatrix(int rows, int cols) {
		super(rows, cols);
		pixelsMatrix = new Pixel[rows][cols];
	}
	
	


	@Override
	public void transpose() {
		super.transpose();
		
		Pixel[][] newPixelMatrix = new Pixel[getRows()][getCols()];
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				newPixelMatrix[i][j] = pixelsMatrix[j][i];
				newPixelMatrix[i][j].transpose();
			}
		}
		
		pixelsMatrix = newPixelMatrix;
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

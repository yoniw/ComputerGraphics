import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

public class Main {

	public static void main(String[] args)
	{
		String inputImagePath = args[0];
		int numOutputColumns = Integer.parseInt(args[1]);
		int numOutputRows = Integer.parseInt(args[2]);
		int energyType = Integer.parseInt(args[3]); //TODO boolean or int?
		String outputImagePath = args[4];
		
		BufferedImage inputImage = null;
		try {
			inputImage = ImageIO.read(new File(inputImagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// step 1
		int[][] energyValues = computeEnergyFunction(inputImage);
	
		
	}

	private static int[][] computeEnergyFunction(BufferedImage inputImage) {
		int inputImageHeight = inputImage.getHeight();
		int inputImageWidth = inputImage.getWidth();
		
		int[][] energyValues = new int[inputImageHeight][inputImageWidth];
		
		for (int i = 0; i < inputImageHeight; i++)
		{
			for (int j = 0; j < inputImageWidth; j++)
			{
				int pixelGradient = computePixelGradient(inputImage,i,j);
				energyValues[i][j] = pixelGradient;
				//TODO steps 2 & 3: local entropy
			}
		}
		
		return energyValues;
	}

	
	
	
	// TODO change implementation according to page 3 in the instructions 
	private static int computePixelGradient(BufferedImage inputImage, int i, int j) {
		int currPixelValue = getSummedRGBValue(inputImage,i,j);
		
		return 8*currPixelValue - (getSummedRGBValue(inputImage,i-1,j-1)+getSummedRGBValue(inputImage,i,j-1)
		+getSummedRGBValue(inputImage,i+1,j-1)+getSummedRGBValue(inputImage,i+1,j)+getSummedRGBValue(inputImage,i+1,j+1)
		+getSummedRGBValue(inputImage,i,j+1)+getSummedRGBValue(inputImage,i-1,j+1));	
	}

	private static int getSummedRGBValue(BufferedImage inputImage, int i, int j) {
		if (i < 0 || j < 0 || i >= inputImage.getHeight() || j >= inputImage.getWidth())
		{
			return 0;
		}
		
		Color color = new Color(inputImage.getRGB(i, j));
		return color.getRed()+color.getGreen()+color.getBlue();
	}
}

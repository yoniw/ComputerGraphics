import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.*;

public class Main {

	public static void main(String[] args)
	{
		String inputImagePath = args[0];
		int numOutputColumns = Integer.parseInt(args[1]);
		int numOutputRows = Integer.parseInt(args[2]);
		int energyType = Integer.parseInt(args[3]); 
		String outputImagePath = args[4];
		
		BufferedImage inputImage = null;
		try {
			inputImage = ImageIO.read(new File(inputImagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// step 1
		int[][] energyValues = computeEnergyMap(inputImage, energyType);
	
		
	}

	private static int[][] computeEnergyMap(BufferedImage inputImage, int energyType) {
		int inputImageHeight = inputImage.getHeight();
		int inputImageWidth = inputImage.getWidth();
		
		int[][] energyValues = new int[inputImageHeight][inputImageWidth];
		
		for (int i = 0; i < inputImageHeight; i++)
		{
			for (int j = 0; j < inputImageWidth; j++)
			{
				int pixelGradient = computePixelGradient(inputImage,i,j);
				energyValues[i][j] = pixelGradient;
				if (energyType == 1)
				{
					energyValues[i][j] += getEntropyValue(inputImage,i,j);
				}
			}
		}
		
		return energyValues;
	}

	private static int getEntropyValue(BufferedImage inputImage, int i, int j) {
		int sum = 0;
		for (int n = i-4; n < i+4; n++)
		{
			for (int m = j-4; m < j+4; m++)
			{
				int pm = getPmn(inputImage,i,j);
				sum += pm * log2(pm); //TODO base 2 indeed?
			}
		}
		return -1*sum;
	}

	private static int getPmn(BufferedImage inputImage, int i, int j) {
		int fmn = getGrayscaleValue(inputImage,i,j);
		int sum = 0;
		for (int l = i-4; l < i+4; l++)
		{
			for (int k = j-4; k < j+4; k++)
			{
				sum += getGrayscaleValue(inputImage,i,j);
			}
		}
		return fmn/sum;
	}

	private static int getGrayscaleValue(BufferedImage inputImage, int i, int j) {
		Color color = new Color(inputImage.getRGB(i, j));
		return (color.getRed()+color.getGreen()+color.getBlue())/3;
	}

	private static double log2(int n) {
		return Math.log(n)/Math.log(2);
	}

	private static int computePixelGradient(BufferedImage inputImage, int i, int j) {
		int energy = 0;
		int numNeighbours = 0;
		List<Integer> neighboursValues = getNeighboursValues(inputImage,i,j);
		for (int neighbourValue : neighboursValues)
		{
			if (neighbourValue != -1)
			{
				energy += neighbourValue;
				numNeighbours++;
			}

		}
		return energy/numNeighbours;
	}

	
	private static List<Integer> getNeighboursValues(BufferedImage inputImage, int i, int j) {
		Color inputPixel = new Color(inputImage.getRGB(i, j));
		List<Integer> neighboursValues = new ArrayList<>();
		
		neighboursValues.add(getValue(inputImage,i-1,j-1,inputPixel));
		neighboursValues.add(getValue(inputImage,i-1,j,inputPixel));
		neighboursValues.add(getValue(inputImage,i-1,j+1,inputPixel));
		neighboursValues.add(getValue(inputImage,i,j+1,inputPixel));
		neighboursValues.add(getValue(inputImage,i+1,j+1,inputPixel));
		neighboursValues.add(getValue(inputImage,i+1,j,inputPixel));
		neighboursValues.add(getValue(inputImage,i+1,j-1,inputPixel));
		neighboursValues.add(getValue(inputImage,i,j-1,inputPixel));

		return neighboursValues;
	}

	private static int getValue(BufferedImage inputImage, int i, int j, Color inputPixel)
	{
		if (i < 0 || j < 0 || i >= inputImage.getHeight() || j >= inputImage.getWidth())
		{
			return -1;
		}
		Color currPixel = new Color(inputImage.getRGB(i, j));
		
		return Math.abs(inputPixel.getRed()-currPixel.getRed()) + Math.abs(inputPixel.getGreen()-currPixel.getGreen()) + Math.abs(inputPixel.getBlue()-currPixel.getBlue());
	}
	
	

}

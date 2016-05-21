import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.*;

/*
 * TODO problems:
 *	confusion between x,y indexes..  everywhere :(
 *	missing: increase image size
 */


public class Main {

	// holds M values 
	private static int[][] dynamicProgrammingCache;
	
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
		
		
		ColorMatrix matrixRep = new ColorMatrix(inputImage);
		
		while (matrixRep.getHeight() > numOutputRows || matrixRep.getWidth() > numOutputColumns)
		{
			// step 1
			int[][] energyValues = computeEnergyMap(matrixRep, energyType);
		
			// step 2
			if (matrixRep.getHeight() > numOutputRows)
			{
				// TODO transpose what: energyValues or matrixRep?
				matrixRep.matrix = Utils.transpose(matrixRep.matrix);
				
			}
			
			// step 3
			int[][] cummulativeEnergy = computeCummulativeMap(energyValues);
			
			// step 4
			List<Pixel> seam = computeLowestEnergySeam(cummulativeEnergy);
			
			// step 5
			matrixRep = removeSeam(matrixRep,seam);
			
		}
 
		BufferedImage outputImage = convertColorMatrixToBufferedImage(matrixRep);
		
		try {
			ImageIO.write(outputImage, "jpg", new File(outputImagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static BufferedImage convertColorMatrixToBufferedImage(ColorMatrix matrixRep) {
		BufferedImage bufferedImage = new BufferedImage(matrixRep.getWidth(), matrixRep.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < matrixRep.matrix.length; i++)
		{
			for (int j = 0; j < matrixRep.matrix[0].length; j++)
			{
				bufferedImage.setRGB(i, j, matrixRep.getRGB(i, j).getRGB());
			}
		}
		return bufferedImage;
	}

	private static ColorMatrix removeSeam(ColorMatrix matrixRep, List<Pixel> seam) {
		ColorMatrix result = new ColorMatrix(matrixRep.getHeight(), matrixRep.getWidth()-1);
		
		for (int i = 0; i < result.getHeight(); i++)
		{
			int j = getSeamJIndex(seam,i);
			for (int jj = 0; jj < j; jj++)
			{
				result.matrix[i][jj] = matrixRep.getRGB(i, jj);
			}
			for (int jj = j+1; jj < result.getWidth(); jj++)
			{
				result.matrix[i][jj-1] = matrixRep.getRGB(i, jj);
			}
		}
		
		return result;
		
	}

	private static int getSeamJIndex(List<Pixel> seam, int ii) {
		for (Pixel p : seam)
		{
			if (p.i == ii)
			{
				return p.j;
			}
		}
		return -1;
	}

	private static List<Pixel> computeLowestEnergySeam(int[][] cummulativeEnergy) {
		List<Pixel> seam = new LinkedList<>();
		
		int currRow = cummulativeEnergy.length;
		Pixel currPixel = getMinBottomPixel(cummulativeEnergy);
		seam.add(currPixel);
		while (currRow >= 0)
		{
			int leftNeighborValue = Integer.MAX_VALUE;
			int rightNeighborValue = Integer.MAX_VALUE;
			//left neighbor
			if (currPixel.i > 0 && currPixel.i <= cummulativeEnergy[0].length-1)
			{

				leftNeighborValue = cummulativeEnergy[currPixel.i-1][currRow-1];
			}
			
			//right neighbor
			if (currPixel.i >= 0 && currPixel.i < cummulativeEnergy[0].length-1)
			{
				rightNeighborValue = cummulativeEnergy[currPixel.i+1][currRow-1];
			}
			
			if (leftNeighborValue < rightNeighborValue)
			{
				currPixel = new Pixel(currPixel.i-1,currRow-1);
			}
			else
			{
				currPixel = new Pixel(currPixel.i+1,currRow-1);
			}
			
			seam.add(currPixel);
			currRow--;
		}
		
		return seam;
	}

	private static Pixel getMinBottomPixel(int[][] cummulativeEnergy) {
		Pixel minBottomPixel = new Pixel(-1, cummulativeEnergy[0].length-1);
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < cummulativeEnergy.length; i++)
		{
			if (cummulativeEnergy[i][cummulativeEnergy[0].length-1] < min)
			{
				min = cummulativeEnergy[i][cummulativeEnergy[0].length-1];
				minBottomPixel.i = i;
			}
		}
		return minBottomPixel;
	}

	private static int[][] computeCummulativeMap(int[][] energyValues) {
		dynamicProgrammingCache = new int[energyValues.length][energyValues[0].length];
		for (int i = 1; i < dynamicProgrammingCache.length; i++)
		{
			for (int j = 0; j < dynamicProgrammingCache[0].length; j++)
			{
				System.out.println("i: " + i + " j:" + j);
				dynamicProgrammingCache[i][j] = getM(energyValues,i,j);
			}
		}
		return dynamicProgrammingCache;
	}

	private static int getM(int[][] energyValues, int i, int j) {
		
		if (i < 0 || j < 0 || i >= energyValues.length || j >= energyValues[0].length)
		{
			return Integer.MAX_VALUE;
		}
		if (i == 0)
		{
			return energyValues[i][j];
		}
		
		if(dynamicProgrammingCache[i][j] != 0)
		{
			return dynamicProgrammingCache[i][j];
		}
		dynamicProgrammingCache[i][j] = energyValues[i][j] + Math.min(Math.min(getM(energyValues,i-1,j-1), getM(energyValues,i-1,j)),  getM(energyValues,i-1,j+1));
		return dynamicProgrammingCache[i][j];
	}

	private static int[][] computeEnergyMap(ColorMatrix matrixRep, int energyType) {
		
		int[][] energyValues = new int[matrixRep.matrix.length][matrixRep.matrix[0].length];
		
		for (int i = 0; i < energyValues.length; i++)
		{
			for (int j = 0; j < energyValues[0].length; j++)
			{
				int pixelGradient = computePixelGradient(matrixRep,i,j);
				energyValues[i][j] = pixelGradient;
				if (energyType == 1)
				{
					energyValues[i][j] += getEntropyValue(matrixRep,i,j);
				}
			}
		}
		
		return energyValues;
	}

	private static int getEntropyValue(ColorMatrix matrixRep, int i, int j) {
		int sum = 0;
		for (int n = i-4; n < i+4; n++)
		{
			for (int m = j-4; m < j+4; m++)
			{
				int pm = getPmn(matrixRep,i,j);
				sum += pm * log2(pm); //TODO base 2 indeed?
			}
		}
		return -1*sum;
	}

	private static int getPmn(ColorMatrix matrixRep, int i, int j) {
		int fmn = getGrayscaleValue(matrixRep,i,j);
		int sum = 0;
		for (int l = i-4; l < i+4; l++)
		{
			for (int k = j-4; k < j+4; k++)
			{
				sum += getGrayscaleValue(matrixRep,i,j);
			}
		}
		return fmn/sum;
	}

	private static int getGrayscaleValue(ColorMatrix matrixRep, int i, int j) {
		Color color = matrixRep.getRGB(i, j);
		return (color.getRed()+color.getGreen()+color.getBlue())/3;
	}

	private static double log2(int n) {
		return Math.log(n)/Math.log(2);
	}

	private static int computePixelGradient(ColorMatrix matrixRep, int i, int j) {
		int energy = 0;
		int numNeighbours = 0;
		List<Integer> neighboursValues = getNeighboursValues(matrixRep,i,j);
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

	
	private static List<Integer> getNeighboursValues(ColorMatrix matrixRep, int i, int j) {
		Color inputPixel = matrixRep.getRGB(i, j);
		List<Integer> neighboursValues = new ArrayList<>();
		
		neighboursValues.add(getValue(matrixRep,i-1,j-1,inputPixel));
		neighboursValues.add(getValue(matrixRep,i-1,j,inputPixel));
		neighboursValues.add(getValue(matrixRep,i-1,j+1,inputPixel));
		neighboursValues.add(getValue(matrixRep,i,j+1,inputPixel));
		neighboursValues.add(getValue(matrixRep,i+1,j+1,inputPixel));
		neighboursValues.add(getValue(matrixRep,i+1,j,inputPixel));
		neighboursValues.add(getValue(matrixRep,i+1,j-1,inputPixel));
		neighboursValues.add(getValue(matrixRep,i,j-1,inputPixel));

		return neighboursValues;
	}

	private static int getValue(ColorMatrix matrixRep, int i, int j, Color inputPixel)
	{
		if (i < 0 || j < 0 || i >= matrixRep.getWidth() || j >= matrixRep.getHeight())
		{
			return -1;
		}
		Color currPixel = matrixRep.getRGB(i, j);
		
		return Math.abs(inputPixel.getRed()-currPixel.getRed()) + Math.abs(inputPixel.getGreen()-currPixel.getGreen()) + Math.abs(inputPixel.getBlue()-currPixel.getBlue());
	}
	
	

}

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.*;


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
		
		int loopNumber = 1;
		
		while (matrixRep.getHeight() > numOutputRows || matrixRep.getWidth() > numOutputColumns)
		{
			System.out.println("loop number: " + loopNumber++);
			
			
			// step 1
			int[][] energyValues = computeEnergyMap(matrixRep, energyType);
		
			// step 2
			if (matrixRep.getHeight() > numOutputRows)
			{
				// TODO transpose energyValues or matrixRep (or both)?
				matrixRep.matrix = Utils.transpose(matrixRep.matrix);
			}
			
			// step 3
			int[][] cummulativeEnergy = computeCummulativeMap(energyValues);
			
			// step 4
			List<Pixel> seam = computeLowestEnergySeam(cummulativeEnergy);
			
			// step 5
			matrixRep = removeSeam(matrixRep,seam);
		}
 
		
		//TODO delete before submission
		// draw seam
//		
//		// step 1
//		int[][] energyValues = computeEnergyMap(matrixRep, energyType);
//	
//		// step 2
//		if (matrixRep.getWidth() > numOutputRows)
//		{
//			// TODO transpose energyValues or matrixRep (or both)?
//			matrixRep.matrix = Utils.transpose(matrixRep.matrix);
//			System.out.println("transposed");
//		}
//		
//		// step 3
//		int[][] cummulativeEnergy = computeCummulativeMap(energyValues);
//		
//		// step 4
//		List<Pixel> seam = computeLowestEnergySeam(cummulativeEnergy);
//		
//		// step 5
//		matrixRep = drawSeam(matrixRep,seam);
		
		
		// end of draw seam
		
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

	// TODO delete before submission
	private static ColorMatrix drawSeam(ColorMatrix matrixRep, List<Pixel> seam) {
		ColorMatrix result = matrixRep;
		for (int i = 0; i < result.matrix.length; i++)
		{
			int j = getSeamJIndex(seam,i);
			for (int jj = 0; jj < matrixRep.matrix[0].length; jj++)
			{
				if (j == jj)
				{
					// paint seam in white
					result.matrix[i][jj] = new Color(255,255,255);
				}
			}
		}
		return result;
	}
	
	
	private static ColorMatrix removeSeam(ColorMatrix matrixRep, List<Pixel> seam) {
		ColorMatrix result = new ColorMatrix(matrixRep.getHeight(), matrixRep.getWidth()-1);
		for (int i = 0; i < result.matrix[0].length; i++)
		{
			int j = getSeamJIndex(seam,i);
			for (int jj = 0; jj < j; jj++)
			{
				result.matrix[jj][i] = matrixRep.getRGB(jj, i);
			}
			for (int jj = j+1; jj < matrixRep.matrix.length; jj++)
			{
				result.matrix[jj-1][i] = matrixRep.getRGB(jj, i);
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
		
		int currRow = cummulativeEnergy.length-1;
		Pixel currPixel = getMinBottomPixel(cummulativeEnergy);
		seam.add(currPixel);
		while (currRow > 0)
		{
			int leftNeighborValue = Integer.MAX_VALUE;
			int rightNeighborValue = Integer.MAX_VALUE;
			//left neighbor
			if (currPixel.j > 0 && currPixel.j <= cummulativeEnergy[0].length-1)
			{
				leftNeighborValue = cummulativeEnergy[currRow-1][currPixel.j-1];
			}
			
			//right neighbor
			if (currPixel.j >= 0 && currPixel.j < cummulativeEnergy[0].length-1)
			{
				rightNeighborValue = cummulativeEnergy[currRow-1][currPixel.j+1];
			}
			
			if (leftNeighborValue < rightNeighborValue)
			{
				currPixel = new Pixel(currRow-1,currPixel.j-1);
			}
			else
			{
				currPixel = new Pixel(currRow-1, currPixel.j+1);
			}
			
			seam.add(currPixel);
			currRow--;
		}
		
		return seam;
	}

	private static Pixel getMinBottomPixel(int[][] cummulativeEnergy) {
		Pixel minBottomPixel = new Pixel(cummulativeEnergy.length-1, -1);
		int min = Integer.MAX_VALUE;
		for (int j = 0; j < cummulativeEnergy[0].length; j++)
		{
			if (cummulativeEnergy[cummulativeEnergy.length-1][j] < min)
			{
				min = cummulativeEnergy[cummulativeEnergy.length-1][j] ;
				minBottomPixel.j = j;
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
					//TODO should we take the average instead?
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
				sum += pm * Math.log(pm); 
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
		if (sum == 0)
		{
			return fmn;
		}
		return fmn/sum;
	}

	private static int getGrayscaleValue(ColorMatrix matrixRep, int i, int j) {
		Color color = matrixRep.getRGB(i, j);
		return (color.getRed()+color.getGreen()+color.getBlue())/3;
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
		if (i < 0 || j < 0 || i >= matrixRep.matrix.length || j >= matrixRep.matrix[0].length)
		{
			return -1;
		}

		Color currPixel = matrixRep.getRGB(i, j);

		return Math.abs(inputPixel.getRed()-currPixel.getRed()) + Math.abs(inputPixel.getGreen()-currPixel.getGreen()) + Math.abs(inputPixel.getBlue()-currPixel.getBlue());
	}
	
	

}

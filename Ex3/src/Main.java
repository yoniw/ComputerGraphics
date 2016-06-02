import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.*;


public class Main {

	// holds M values 
	private static int[][] MValuesCache;
	// holds Pmn values
	private static int[][] PmnValuesCache;
	

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
		ColorMatrix copyOfMatrixRep = new ColorMatrix(inputImage);
		
		int loopNumber = 1;
		boolean enlarge = false;
		List<List<Pixel>> seamsRemoved = new LinkedList<>();
		
		int numRowsLeft, numColsLeft;
		numRowsLeft = Math.abs(matrixRep.getHeight() - numOutputRows);
		numColsLeft = Math.abs(matrixRep.getWidth() - numOutputColumns);
		
		while (numRowsLeft > 0 || numColsLeft > 0)
		{
			System.out.println("loop number: " + loopNumber++ + " height: " + matrixRep.getHeight() + " width: " + matrixRep.getWidth());
			
			enlarge = false;
			if (numColsLeft > 0 && matrixRep.getWidth() < numOutputColumns)
			{
				enlarge = true;
			}
			else if (numColsLeft == 0 && matrixRep.getHeight() < numOutputRows)
			{
				enlarge = true;
			}
			
			// step 1
			int[][] energyValues = computeEnergyMap(matrixRep, energyType);
		
			// step 2
			boolean transposed = false;
			if (numColsLeft > 0)
			{
				matrixRep.matrix = Utils.transpose(matrixRep.matrix);
				matrixRep.setPixelsMatrix(Utils.transpose(matrixRep.getPixelsMatrix()));
				int height = matrixRep.getHeight();
				int width = matrixRep.getWidth();
				matrixRep.setHeight(width);
				matrixRep.setWidth(height);
				energyValues = Utils.transpose(energyValues);
				transposed = true;
				numColsLeft--; 
			}
			else
			{
				numRowsLeft--;
			}
			
			// step 3
			int[][] cummulativeEnergy = computeCummulativeMap(energyValues);
			
			// step 4
			List<Pixel> seam = computeLowestEnergySeam(cummulativeEnergy, matrixRep);
			if (enlarge)
			{
				seamsRemoved.add(seam);
			}
			
			// step 5
			matrixRep = removeSeam(matrixRep,seam);

			if (enlarge && numColsLeft == 0)
			{
				// it's time to add the removed seams.
				matrixRep = addSeams(copyOfMatrixRep,seamsRemoved);
				// later, when we add rows, the "original image" will be matrixRep 
				copyOfMatrixRep = matrixRep;
				// clean seams list
				seamsRemoved = new LinkedList<>();
			}
			
			// Transpose back.
			if (transposed) {
				matrixRep.matrix = Utils.transpose(matrixRep.matrix);
				copyOfMatrixRep.matrix = Utils.transpose(copyOfMatrixRep.matrix);
				matrixRep.setPixelsMatrix(Utils.transpose(matrixRep.getPixelsMatrix()));
				int height = matrixRep.getHeight();
				int width = matrixRep.getWidth();
				matrixRep.setHeight(width);
				matrixRep.setWidth(height);
				int copyOfMatrixRepHeight = copyOfMatrixRep.getHeight();
				int copyOfMatrixRepWidth = copyOfMatrixRep.getWidth();
				copyOfMatrixRep.setHeight(copyOfMatrixRepWidth);
				copyOfMatrixRep.setWidth(copyOfMatrixRepHeight);
			}
			
		}
	
		if (enlarge && numRowsLeft == 0)
		{
			// transpose copyOf 
			copyOfMatrixRep.matrix = Utils.transpose(copyOfMatrixRep.matrix);
			int copyOfMatrixRepHeight = copyOfMatrixRep.getHeight();
			int copyOfMatrixRepWidth = copyOfMatrixRep.getWidth();
			copyOfMatrixRep.setHeight(copyOfMatrixRepWidth);
			copyOfMatrixRep.setWidth(copyOfMatrixRepHeight);
			
			matrixRep = addSeams(copyOfMatrixRep,seamsRemoved);
		}
		
		
		BufferedImage outputImage = convertColorMatrixToBufferedImage(matrixRep);
		
		try {
			ImageIO.write(outputImage, "jpg", new File(outputImagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private static ColorMatrix addSeams(ColorMatrix copyOf, List<List<Pixel>> seamsRemoved) {
		int numSeams = seamsRemoved.size();
		ColorMatrix enlargedColorMatrix = new ColorMatrix(copyOf.getHeight() + numSeams, copyOf.getWidth());
		
		// maps a row to (sorted) j-indexes of pixels we have to duplicate
		TreeMap<Integer,List<Integer>> pixelsToDuplicateInEachRow = prepareRowToPixelsMap(copyOf, seamsRemoved);
		
		Pixel[][] enlargedPixelMatrix = enlargedColorMatrix.getPixelsMatrix();
		
		// fill enlarged matrix
		for (int i = 0; i < enlargedColorMatrix.getWidth(); i++)
		{
			List<Integer> currRowPixelsToDuplicate = pixelsToDuplicateInEachRow.get(i);
			int numPixelsDuplicatedCurrRow = 0;
			for (int j = 0; j < enlargedColorMatrix.getHeight(); j++)
			{
				// get a 'regular' pixel
				enlargedColorMatrix.matrix[i][j] = copyOf.matrix[i][j-numPixelsDuplicatedCurrRow];
				Pixel p = new Pixel(i, j-numPixelsDuplicatedCurrRow);
				p.setOriginal_i(p.i);
				p.setOriginal_j(p.j);
				enlargedPixelMatrix[i][j] = p;
				
				if (currRowPixelsToDuplicate.contains(j-numPixelsDuplicatedCurrRow))
				{
					// we have to duplicate this pixel to the right					
					enlargedColorMatrix.matrix[i][j+1] = copyOf.matrix[i][j-numPixelsDuplicatedCurrRow];
					p = new Pixel(i, j-numPixelsDuplicatedCurrRow+1);
					p.setOriginal_i(p.i);
					p.setOriginal_j(p.j);
					enlargedPixelMatrix[i][j+1] = p;
					
					numPixelsDuplicatedCurrRow++;
					j++;
				}
			}
		}
		enlargedColorMatrix.setPixelsMatrix(enlargedPixelMatrix);
		
		return enlargedColorMatrix;
	}


	
	private static TreeMap<Integer, List<Integer>> prepareRowToPixelsMap(ColorMatrix copyOf,List<List<Pixel>> seamsRemoved) {
		TreeMap<Integer,List<Integer>> pixelsToDuplicateInEachRow = new TreeMap<>();
		for (int i = 0; i < copyOf.getWidth(); i++)
		{
			List<Integer> currRow = new LinkedList<>();
			
			// go over all seams and add pixels of row i
			for (List<Pixel> seam : seamsRemoved)
			{
				for (Pixel pixel : seam)
				{
					if (pixel.getOriginal_i() == i)
					{
						currRow.add(pixel.getOriginal_j());
					}
				}
			}
			Collections.sort(currRow);
			pixelsToDuplicateInEachRow.put(i, currRow);
		}
		return pixelsToDuplicateInEachRow;
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
		ColorMatrix result = new ColorMatrix(matrixRep.getHeight()-1, matrixRep.getWidth());
		Pixel[][] pixelsResult = result.getPixelsMatrix();
		
		for (int i = 0; i < result.matrix.length; i++)
		{
			int j = getSeamJIndex(seam,i);
			for (int jj = 0; jj < j; jj++)
			{
				result.matrix[i][jj] = matrixRep.getRGB(i, jj);
				pixelsResult[i][jj] = matrixRep.getPixel(i, jj);
			}
			for (int jj = j+1; jj < matrixRep.matrix[0].length; jj++)
			{
				result.matrix[i][jj-1] = matrixRep.getRGB(i, jj);
				Pixel tmp = matrixRep.getPixel(i, jj);
				tmp.j--;
				pixelsResult[i][jj-1] = tmp;
			}
		}
		
		result.setPixelsMatrix(pixelsResult);
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

	private static List<Pixel> computeLowestEnergySeam(int[][] cummulativeEnergy, ColorMatrix matrixRep) {
		List<Pixel> seam = new LinkedList<>();
		
		int currRow = cummulativeEnergy.length-1;
		Pixel currPixel = getMinBottomPixel(cummulativeEnergy, matrixRep);
		seam.add(currPixel);
		while (currRow > 0)
		{
			TreeMap<Integer,Pixel> neighborsValueToPixelMap = new TreeMap<>();

			// left upper neighbor
			if (currPixel.j > 0 && currPixel.j <= cummulativeEnergy[0].length-1)
			{
				int leftNeighborValue = cummulativeEnergy[currRow-1][currPixel.j-1];
				neighborsValueToPixelMap.put(leftNeighborValue, matrixRep.getPixel(currRow-1,currPixel.j-1));
			}
			
			// right upper neighbor
			if (currPixel.j >= 0 && currPixel.j < cummulativeEnergy[0].length-1)
			{
				int rightNeighborValue = cummulativeEnergy[currRow-1][currPixel.j+1];
				neighborsValueToPixelMap.put(rightNeighborValue, matrixRep.getPixel(currRow-1,currPixel.j+1));
			}
			
			// upper neighbor
			int upperNeighborValue = cummulativeEnergy[currRow-1][currPixel.j];
			neighborsValueToPixelMap.put(upperNeighborValue,matrixRep.getPixel(currRow-1,currPixel.j));
			
			// minimum value
			currPixel = neighborsValueToPixelMap.firstEntry().getValue();
			seam.add(currPixel);
			currRow--;
		}
		
		return seam;
	}

	private static Pixel getMinBottomPixel(int[][] cummulativeEnergy, ColorMatrix matrixRep) {
		int i = cummulativeEnergy.length-1;
		Pixel minBottomPixel = null;
		int min = Integer.MAX_VALUE;
		for (int j = 0; j < cummulativeEnergy[0].length; j++)
		{
			if (cummulativeEnergy[cummulativeEnergy.length-1][j] < min)
			{
				min = cummulativeEnergy[cummulativeEnergy.length-1][j] ;
				minBottomPixel = matrixRep.getPixel(i, j);
			}
		}
		return minBottomPixel;
	}

	private static int[][] computeCummulativeMap(int[][] energyValues) {
	
			MValuesCache = new int[energyValues.length][energyValues[0].length];
			for (int i = 1; i < MValuesCache.length; i++)
			{
				for (int j = 0; j < MValuesCache[0].length; j++)
				{
					MValuesCache[i][j] = getM(energyValues,i,j);
				}
			}
			return MValuesCache;


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
		
		if(MValuesCache[i][j] != 0)
		{
			return MValuesCache[i][j];
		}
		MValuesCache[i][j] = energyValues[i][j] + Math.min(Math.min(getM(energyValues,i-1,j-1), getM(energyValues,i-1,j)),  getM(energyValues,i-1,j+1));
		return MValuesCache[i][j];
	}

	private static int[][] computeEnergyMap(ColorMatrix matrixRep, int energyType) {
		
		int[][] energyValues = new int[matrixRep.matrix.length][matrixRep.matrix[0].length];
		if (energyType == 1)
		{
			PmnValuesCache = new int[matrixRep.matrix.length][matrixRep.matrix[0].length];
		}
		
		for (int i = 0; i < energyValues.length; i++)
		{
			for (int j = 0; j < energyValues[0].length; j++)
			{
				int pixelGradient = computePixelGradient(matrixRep,i,j);
				if (energyType == 0)
				{
					energyValues[i][j] = pixelGradient;
				}
				else if (energyType == 1)
				{
					energyValues[i][j] = (pixelGradient + getEntropyValue(matrixRep,i,j))/2;
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
		if (PmnValuesCache[i][j] != 0)
		{
			return PmnValuesCache[i][j];
		}
		
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
		PmnValuesCache[i][j] = fmn/sum;
		return fmn/sum;
	}

	private static int getGrayscaleValue(ColorMatrix matrixRep, int i, int j) {
		Color color = matrixRep.getRGB(i, j);
		return (color.getRed()+color.getGreen()+color.getBlue())/3;
	}



	private static int computePixelGradient(ColorMatrix matrixRep, int i, int j) {

		Color inputPixel = matrixRep.getRGB(i, j);

		int neighboursCount = 0;
		int sumEnergy = 0;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				
				int curr_i = i + x;
				int curr_j = j + y;
				
				if ((curr_i >= 0) && (curr_j >= 0) 
						&& (curr_i < matrixRep.matrix.length) 
						&& (curr_j < matrixRep.matrix[0].length)) {
					
					// Neighbour inside matrix. Calculating value.
					neighboursCount++;
					Color currPixel = matrixRep.getRGB(curr_i, curr_j);
					sumEnergy += Math.abs(inputPixel.getRed() - currPixel.getRed()) 
							+ Math.abs(inputPixel.getGreen() - currPixel.getGreen()) 
							+ Math.abs(inputPixel.getBlue() - currPixel.getBlue());
				}
			}
		}
		
		
		return sumEnergy / neighboursCount;
	}

}

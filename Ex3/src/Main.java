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
	private static EnergyMatrix MValuesCache;
	// holds Pmn values
	private static EnergyMatrix PmnValuesCache;
	

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
		numRowsLeft = Math.abs(matrixRep.getRows() - numOutputRows);
		numColsLeft = Math.abs(matrixRep.getCols() - numOutputColumns);
		
		while (numRowsLeft > 0 || numColsLeft > 0)
		{
			System.out.println("loop number: " + loopNumber++ + " rows: " + matrixRep.getRows() + " cols: " + matrixRep.getCols());
			
			enlarge = false;
			if (numColsLeft > 0 && matrixRep.getCols() < numOutputColumns)
			{
				enlarge = true;
			}
			else if (numColsLeft == 0 && matrixRep.getRows() < numOutputRows)
			{
				enlarge = true;
			}
			
			// step 1
			EnergyMatrix energyValues = computeEnergyMap(matrixRep, energyType);
		
			// step 2
			boolean transposed = false;
			if (numRowsLeft > 0)
			{
				matrixRep.transpose();
				energyValues.transpose();
				transposed = true;
				numRowsLeft--; 
			}
			else
			{
				numColsLeft--;
			}
			
			// step 3
			EnergyMatrix cummulativeEnergy = computeCummulativeMap(energyValues);
			
			// step 4
			List<Pixel> seam = computeLowestEnergySeam(cummulativeEnergy, matrixRep, energyType);
			if (enlarge)
			{
				seamsRemoved.add(seam);
			}
			
			// step 5
			matrixRep = removeSeam(matrixRep,seam);

			if (enlarge && numRowsLeft == 0)
			{
				// it's time to add the removed seams.
				
				//transpose copyOf
				copyOfMatrixRep.transpose();
							
				matrixRep = addSeams(copyOfMatrixRep,seamsRemoved);
				// later, when we add rows, the "original image" will be matrixRep 
				copyOfMatrixRep = matrixRep;
				// clean seams list
				seamsRemoved = new LinkedList<>();
			}
			
			// Transpose back.
			if (transposed) {
				matrixRep.transpose();
				energyValues.transpose();
			}
			
		}
	
		if (enlarge && numColsLeft == 0)
		{		
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
		ColorMatrix enlargedColorMatrix = new ColorMatrix(copyOf.getRows() , copyOf.getCols()+ numSeams);
		
		// maps a row to (sorted) j-indexes of pixels we have to duplicate
		TreeMap<Integer,List<Integer>> pixelsToDuplicateInEachRow = prepareRowToPixelsMap(copyOf, seamsRemoved);
		
		Pixel[][] enlargedPixelMatrix = enlargedColorMatrix.getPixelsMatrix();
		
		// fill enlarged matrix
		for (int i = 0; i < enlargedColorMatrix.getRows(); i++)
		{
			List<Integer> currRowPixelsToDuplicate = pixelsToDuplicateInEachRow.get(i);
			int numPixelsDuplicatedCurrRow = 0;
			for (int j = 0; j < enlargedColorMatrix.getCols(); j++)
			{
				// get a 'regular' pixel
				enlargedColorMatrix.setCell(i, j, copyOf.getCell(i, j-numPixelsDuplicatedCurrRow));
				Pixel p = new Pixel(i, j-numPixelsDuplicatedCurrRow);
				p.setOriginalRow(p.getRow());
				p.setOriginalCol(p.getCol());
				enlargedPixelMatrix[i][j] = p;
				
				if (currRowPixelsToDuplicate.contains(j-numPixelsDuplicatedCurrRow))
				{
					// we have to duplicate this pixel to the right					
					enlargedColorMatrix.setCell(i, j+1, copyOf.getCell(i, j-numPixelsDuplicatedCurrRow));
					p = new Pixel(i, j-numPixelsDuplicatedCurrRow+1);
					p.setOriginalRow(p.getRow());
					p.setOriginalCol(p.getCol());
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
		for (int i = 0; i < copyOf.getRows(); i++)
		{
			List<Integer> currRow = new LinkedList<>();
			
			// go over all seams and add pixels of row i
			for (List<Pixel> seam : seamsRemoved)
			{
				for (Pixel pixel : seam)
				{
					if (pixel.getOriginalRow() == i)
					{
						currRow.add(pixel.getOriginalCol());
					}
				}
			}
			Collections.sort(currRow);
			pixelsToDuplicateInEachRow.put(i, currRow);
		}
		return pixelsToDuplicateInEachRow;
	}


	private static BufferedImage convertColorMatrixToBufferedImage(ColorMatrix matrixRep) {
		BufferedImage bufferedImage = new BufferedImage(matrixRep.getCols(), matrixRep.getRows(), BufferedImage.TYPE_INT_RGB);
				
		for (int i = 0; i < matrixRep.getRows(); i++)
		{
			for (int j = 0; j < matrixRep.getCols(); j++)
			{
				bufferedImage.setRGB(j, i, matrixRep.getCell(i, j).getRGB());
			}
		}
		return bufferedImage;
	}

	
	private static ColorMatrix removeSeam(ColorMatrix matrixRep, List<Pixel> seam) {
		ColorMatrix result = new ColorMatrix(matrixRep.getRows(), matrixRep.getCols() - 1);
		Pixel[][] pixelsResult = result.getPixelsMatrix();
		
		for (int i = 0; i < result.getRows(); i++)
		{
			int j = getSeamJIndex(seam,i);
			for (int jj = 0; jj < j; jj++)
			{
				result.setCell(i, jj, matrixRep.getCell(i, jj));
				pixelsResult[i][jj] = matrixRep.getPixel(i, jj);
			}
			for (int jj = j+1; jj < matrixRep.getCols(); jj++)
			{
				result.setCell(i, jj-1, matrixRep.getCell(i, jj));
				Pixel tmp = matrixRep.getPixel(i, jj);
				tmp.setCol(tmp.getCol() - 1);
				pixelsResult[i][jj-1] = tmp;
			}
		}
		
		result.setPixelsMatrix(pixelsResult);
		return result;
		
	}

	private static int getSeamJIndex(List<Pixel> seam, int ii) {
		for (Pixel p : seam)
		{
			if (p.getRow() == ii)
			{
				return p.getCol();
			}
		}
		return -1;
	}

	private static List<Pixel> computeLowestEnergySeam(EnergyMatrix cummulativeEnergy, ColorMatrix matrixRep, int energyType) {
		List<Pixel> seam = new LinkedList<>();
		
		int currRow = cummulativeEnergy.getRows() - 1;
		Pixel currPixel = getMinBottomPixel(cummulativeEnergy, matrixRep);
		seam.add(currPixel);
		while (currRow > 0)
		{
			TreeMap<Integer,Pixel> neighborsValueToPixelMap = new TreeMap<>();

			// left upper neighbor
			if (currPixel.getCol() > 0 && currPixel.getCol() <= cummulativeEnergy.getCols() - 1)
			{
				int leftNeighborValue = cummulativeEnergy.getCell(currRow-1, currPixel.getCol()-1);
				
				if (energyType == 2) {
					int gradRightPixel = 0;
					if (currPixel.getCol() < matrixRep.getCols() - 1) {
						gradRightPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() + 1);
					}
					int gradLeftPixel = 0;
					if (currPixel.getCol() > 0) {
						gradLeftPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() - 1);
					}
					int gradUpPixel = computePixelGradient(matrixRep, currRow - 1, currPixel.getCol());
					
					leftNeighborValue += Math.abs(gradRightPixel - gradLeftPixel) + Math.abs(gradUpPixel - gradLeftPixel);
				}
				
				neighborsValueToPixelMap.put(leftNeighborValue, matrixRep.getPixel(currRow-1,currPixel.getCol()-1));
			}
			
			// right upper neighbor
			if (currPixel.getCol() >= 0 && currPixel.getCol() < cummulativeEnergy.getCols()-1)
			{
				int rightNeighborValue = cummulativeEnergy.getCell(currRow-1, currPixel.getCol() + 1);
				
				if (energyType == 2) {
					int gradRightPixel = 0;
					if (currPixel.getCol() < matrixRep.getCols() - 1) {
						gradRightPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() + 1);
					}					
					int gradLeftPixel = 0;
					if (currPixel.getCol() > 0) {
						gradLeftPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() - 1);
					}
					int gradUpPixel = computePixelGradient(matrixRep, currRow - 1, currPixel.getCol());
					
					rightNeighborValue += Math.abs(gradRightPixel - gradLeftPixel) + Math.abs(gradUpPixel - gradRightPixel);
				}
				
				neighborsValueToPixelMap.put(rightNeighborValue, matrixRep.getPixel(currRow-1,currPixel.getCol()+1));
			}
			
			// upper neighbor
			int upperNeighborValue = cummulativeEnergy.getCell(currRow-1 , currPixel.getCol());
			
			if (energyType == 2) {
				int gradRightPixel = 0;
				if (currPixel.getCol() < matrixRep.getCols() - 1) {
					gradRightPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() + 1);
				}				
				int gradLeftPixel = 0;
				if (currPixel.getCol() > 0) {
					gradLeftPixel = computePixelGradient(matrixRep, currRow, currPixel.getCol() - 1);
				}
				
				upperNeighborValue += Math.abs(gradRightPixel - gradLeftPixel);
			}
			
			neighborsValueToPixelMap.put(upperNeighborValue,matrixRep.getPixel(currRow-1,currPixel.getCol()));
			
			// minimum value
			currPixel = neighborsValueToPixelMap.firstEntry().getValue();
			seam.add(currPixel);
			currRow--;
		}
		
		return seam;
	}

	private static Pixel getMinBottomPixel(EnergyMatrix cummulativeEnergy, ColorMatrix matrixRep) {
		int row = cummulativeEnergy.getRows() - 1;
		Pixel minBottomPixel = null;
		int min = Integer.MAX_VALUE;
		for (int j = 0; j < cummulativeEnergy.getCols(); j++)
		{
			int currEnergy = cummulativeEnergy.getCell(cummulativeEnergy.getRows()-1, j);
			if (currEnergy < min)
			{
				min = currEnergy ;
				minBottomPixel = matrixRep.getPixel(row, j);
			}
		}
		return minBottomPixel;
	}

	private static EnergyMatrix computeCummulativeMap(EnergyMatrix energyValues) {
	
			MValuesCache = new EnergyMatrix(energyValues.getRows(), energyValues.getCols());
			for (int i = 1; i < MValuesCache.getRows(); i++)
			{
				for (int j = 0; j < MValuesCache.getCols(); j++)
				{
					MValuesCache.setCell(i, j, getM(energyValues,i,j));
				}
			}
			return MValuesCache;


	}
	

	private static int getM(EnergyMatrix energyValues, int i, int j) {
		
		if (i < 0 || j < 0 || i >= energyValues.getRows() || j >= energyValues.getCols())
		{
			return Integer.MAX_VALUE;
		}
		if (i == 0)
		{
			return energyValues.getCell(i, j);
		}
		
		if(MValuesCache.getCell(i, j) != 0)
		{
			return MValuesCache.getCell(i, j);
		}
		MValuesCache.setCell(i, j, energyValues.getCell(i, j) + Math.min(Math.min(getM(energyValues,i-1,j-1), getM(energyValues,i-1,j)),  getM(energyValues,i-1,j+1)));
		return MValuesCache.getCell(i, j);
	}

	private static EnergyMatrix computeEnergyMap(ColorMatrix matrixRep, int energyType) {
		
		EnergyMatrix energyValues = new EnergyMatrix(matrixRep.getRows(), matrixRep.getCols());
		if (energyType == 1)
		{
			PmnValuesCache = new EnergyMatrix(matrixRep.getRows(), matrixRep.getCols());
		}
		
		for (int i = 0; i < energyValues.getRows(); i++)
		{
			for (int j = 0; j < energyValues.getCols(); j++)
			{
				int pixelGradient = computePixelGradient(matrixRep,i,j);
				if (energyType == 0 || energyType == 2)
				{
					energyValues.setCell(i, j, pixelGradient);
				}
				else if (energyType == 1)
				{
					energyValues.setCell(i, j, (pixelGradient + getEntropyValue(matrixRep,i,j))/2);
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
		if (PmnValuesCache.getCell(i, j) != 0)
		{
			return PmnValuesCache.getCell(i, j);
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
		PmnValuesCache.setCell(i, j, fmn/sum);
		return fmn/sum;
	}

	private static int getGrayscaleValue(ColorMatrix matrixRep, int i, int j) {
		Color color = matrixRep.getCell(i, j);
		return (color.getRed()+color.getGreen()+color.getBlue())/3;
	}



	private static int computePixelGradient(ColorMatrix matrixRep, int i, int j) {

		Color inputPixel = matrixRep.getCell(i, j);

		int neighboursCount = 0;
		int sumEnergy = 0;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				
				int curr_i = i + x;
				int curr_j = j + y;
				
				if ((curr_i >= 0) && (curr_j >= 0) 
						&& (curr_i < matrixRep.getRows()) 
						&& (curr_j < matrixRep.getCols())) {
					
					// Neighbour inside matrix. Calculating value.
					neighboursCount++;
					Color currPixel = matrixRep.getCell(curr_i, curr_j);
					sumEnergy += Math.abs(inputPixel.getRed() - currPixel.getRed()) 
							+ Math.abs(inputPixel.getGreen() - currPixel.getGreen()) 
							+ Math.abs(inputPixel.getBlue() - currPixel.getBlue());
				}
			}
		}
		
		
		return sumEnergy / neighboursCount;
	}

}

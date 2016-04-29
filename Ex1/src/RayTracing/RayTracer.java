package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;

	private Scene scene;
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);
			
			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		Camera cam = null;
		Settings set = null;
		List<Material> materialsList = new ArrayList<>();
		List<Sphere> spheresList = new ArrayList<>();
		List<Plane> planesList = new ArrayList<>();
		List<Cylinder> cylindersList = new ArrayList<>();
		List<Light> lightsList = new ArrayList<>();
		
		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					cam = new Camera(params);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					set = new Settings(params);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					materialsList.add(new Material(params));
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					spheresList.add(new Sphere(params));
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					planesList.add(new Plane(params));
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("cyl"))
				{
					cylindersList.add(new Cylinder(params));
					System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					lightsList.add(new Light(params));
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}

		scene = new Scene(cam, set, materialsList, spheresList, planesList, cylindersList, lightsList);
		
		System.out.println("Finished parsing scene file " + sceneFileName);

		
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		
		//TODO make sure there's no confusion between x and y, width and height etc.
		RGB[][] RGBMatrix = new RGB[this.imageWidth][this.imageHeight];
		
		//TODO fill up the matrix
		renderScene(RGBMatrix);
		
		//TODO delete
//		printMatrix(RGBMatrix,200,300);
		
		convertRGBMatrixtoByteArray(rgbData, RGBMatrix);
		
		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	//TODO delete
	private void printMatrix(RGB[][] rGBMatrix, int start, int end) {
		System.out.println("Reds:");
		for (int i = start; i < end; i++)
		{
			for (int j = start; j < end; j++)
			{
				System.out.print(rGBMatrix[i][j].getRed() +" | ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Greens:");
		for (int i = start; i < end; i++)
		{
			for (int j = start; j < end; j++)
			{
				System.out.print(rGBMatrix[i][j].getGreen() +" | ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Blues:");
		for (int i = start; i < end; i++)
		{
			for (int j = start; j < end; j++)
			{
				System.out.print(rGBMatrix[i][j].getBlue() +" | ");
			}
			System.out.println();
		}
		
		
	}

	private void renderScene(RGB[][] RGBMatrix) {
		Camera cam = scene.getCamera();
		ScreenSimulator screen = new ScreenSimulator(cam, imageWidth, imageHeight);
		PixelColorProcessor pixelCP = new PixelColorProcessor();
		
		for (int i = 0; i < this.imageWidth; i++) {
			screen.initCurrentV();
			
			for (int j = 0; j < this.imageHeight; j++) {
				// Ray ray = cam.constructRayThroughPixel(i, j, imageWidth, imageHeight);
				Ray ray = new Ray(cam.getPosition(), screen.getCurrentV());
				Intersection hit = ray.findIntersection(scene);
				RGBMatrix[i][j] = pixelCP.getColor(scene, hit, null, scene.getSettings().getMaxNumberOfRecursions());
				
				screen.nextX();
			}
			
			screen.nextY();
		}

	}

	//TODO make sure there's no confusion between x and y, width and height etc.
	private void convertRGBMatrixtoByteArray(byte[] rgbData, RGB[][] RGBMatrix) {
		
		for (int x = 0; x < this.imageWidth; x++)
		{
			for (int y = 0; y < this.imageHeight; y++)
			{
				rgbData[(y * this.imageWidth + x) * 3] = (byte)(Math.floor(255*RGBMatrix[y][x].getRed()));
				rgbData[(y * this.imageWidth + x) * 3 + 1] = (byte)(Math.floor(255*RGBMatrix[y][x].getGreen()));
				rgbData[(y * this.imageWidth + x) * 3 + 2] = (byte)(Math.floor(255*RGBMatrix[y][x].getBlue()));
			}
		}
		
	}
	
	

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}

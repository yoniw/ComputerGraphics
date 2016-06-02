import java.awt.Color;

public class Utils {

	public static Color[][] transpose(Color[][] matrix) {
		Color[][] result = new Color[matrix[0].length][matrix.length];
		
		for (int i = 0; i < result.length; i++)
		{
			for (int j = 0; j < result[0].length; j++)
			{
				result[i][j] = matrix[j][i];
			}
		}
		return result;
	}
	
	public static int[][] transpose(int[][] matrix) {
		int[][] result = new int[matrix[0].length][matrix.length];
		
		for (int i = 0; i < result.length; i++)
		{
			for (int j = 0; j < result[0].length; j++)
			{
				result[i][j] = matrix[j][i];
			}
		}
		return result;
	}

	public static Pixel[][] transpose(Pixel[][] matrix) {
		Pixel[][] result = new Pixel[matrix[0].length][matrix.length];
		
		for (int i = 0; i < result.length; i++)
		{
			for (int j = 0; j < result[0].length; j++)
			{
				Pixel p = new Pixel(matrix[j][i]);
				int tmp = p.i;
				p.i = p.j;
				p.j = tmp;
				result[i][j] = p;
				
				tmp = p.getOriginal_i();
				p.setOriginal_i(p.getOriginal_j());
				p.setOriginal_j(tmp);
			}
		}
		return result;
	}

}

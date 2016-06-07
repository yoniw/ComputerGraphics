
public class EnergyMatrix extends Matrix<Integer> {

	public EnergyMatrix(int rows, int cols) {
		super(rows, cols);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				setCell(i, j, 0);
			}
		}
	}

}

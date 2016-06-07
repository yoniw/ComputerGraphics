
public class Matrix<T> {
	
	private int rows;
	private int cols;
	
	private Object[][] data;
	
	
	public Matrix(int rows, int cols) {

		this.rows = rows;
		this.cols = cols;
		
		this.data = new Object[rows][cols];
	}

	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	
	@SuppressWarnings("unchecked")
	public T getCell(int row, int col) {
		return (T)data[row][col];
	}
	
	
	public void setCell(int row, int col, T content) {
		data[row][col] = content;
	}
	
	
	public Object[][] getData() {
		return data;
	}
	
	
	public void transpose() {
		Object[][] newData = new Object[cols][rows];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newData[j][i] = data[i][j];
			}
		}
		
		data = newData;
		rows += cols;
		cols = rows - cols;
		rows = rows - cols;
	}
}

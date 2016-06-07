
public class Pixel {

	private int row;
	private int col;

	private int originalRow;
	private int originalCol;
	
	public Pixel(Pixel pixel)
	{
		this.row = pixel.row;
		this.col = pixel.col;
		this.originalRow = pixel.getOriginalRow();
		this.originalCol = pixel.getOriginalCol();
	}
	
	public Pixel(int row, int col)
	{
		this.row=row;
		this.col=col;
	}
	
	// for debugging
	public String toString()
	{
		return "<" + row + ", " + col + ">";  
	}

	public void setOriginalRow(int row) {
		originalRow = row;	
	}
	public void setOriginalCol(int col) {
		originalCol = col;
	}
	
	public int getOriginalRow(){
		return originalRow;
	}
	
	public int getOriginalCol(){
		return originalCol;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}

	public void transpose() {
		row += col;
		col = row - col;
		row = row - col;
		
		originalRow += originalCol;
		originalCol = originalRow - originalCol;
		originalRow = originalRow - originalCol;
	}
}


public class Tester {

	public static void main(String[] args)
	{
		// originalcastle.jpg dimensions: 1428 971
		
		
		// energyType == 0
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "0", "1400_971_0.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "940", "0", "1428_940_0.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1400", "940", "0", "1400_940_0.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "0", "1435_971_0.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "971", "0", "1428_971_0.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "0", "1435_971_0.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1435", "940", "0", "1435_940_0.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "0", "1400_971_0.jpg"});
		
	
		// energyType == 1
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "1", "1400_971_1.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "940", "1", "1428_940_1.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1400", "940", "1", "1400_940_1.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "1", "1435_971_1.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "971", "1", "1428_971_1.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "1", "1435_971_1.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1435", "940", "1", "1435_940_1.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "1", "1400_971_1.jpg"});
		
		// energyType == 2
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "2", "1400_971_2.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "940", "2", "1428_940_2.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1400", "940", "2", "1400_940_2.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "2", "1435_971_2.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "971", "2", "1428_971_2.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1435", "971", "2", "1435_971_2.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1435", "940", "2", "1435_940_2.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "971", "2", "1400_971_2.jpg"});
	}
}

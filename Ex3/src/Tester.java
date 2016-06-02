
public class Tester {

	public static void main(String[] args)
	{
		// originalcastle.jpg dimensions: 1428 968
		
		
		// energyType == 0
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1000", "968", "0", "1000_968_0.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "850", "0", "1428_850_0.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1000", "850", "0", "1000_850_0.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1500", "968", "0", "1500_968_0.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "1050", "0", "1428_1050_0.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1500", "1050", "0", "1500_1050_0.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1500", "850", "0", "1500_850_0.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1000", "1050", "0", "1000_1050_0.jpg"});
		
	
		// energyType == 1
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1000", "968", "1", "1000_968_1.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "850", "1", "1428_850_1.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1000", "850", "1", "1000_850_1.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1500", "968", "1", "1500_968_1.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "1050", "1", "1428_1050_1.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1500", "1050", "1", "1500_1050_1.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1500", "850", "1", "1500_850_1.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1000", "1050", "1", "1000_1050_1.jpg"});
		
		// energyType == 2
		
		// TODO
	}
}

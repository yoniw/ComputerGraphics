
public class Tester {

	public static void main(String[] args)
	{
		// originalcastle.jpg dimensions: 1428 968
		
		
		// energyType == 0
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "968", "0", "1400_968_0.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "950", "0", "1428_950_0.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1400", "950", "0", "1400_950_0.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1450", "968", "0", "1450_968_0.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "1000", "0", "1428_1000_0.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1453", "1000", "0", "1453_1000_0.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1453", "950", "0", "1453_950_0.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "1000", "0", "1400_1000_0.jpg"});
		
	
		// energyType == 1
		
		// reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "968", "1", "1400_968_1.jpg"});
		// reducing height
		Main.main(new String[]{"originalcastle.jpg", "1428", "950", "1", "1428_950_1.jpg"});
		// reducing width and height
		Main.main(new String[]{"originalcastle.jpg", "1400", "950", "1", "1400_950_1.jpg"});
		// enlarging width
		Main.main(new String[]{"originalcastle.jpg", "1453", "968", "1", "1453_968_1.jpg"});
		// enlarging height
		Main.main(new String[]{"originalcastle.jpg", "1428", "1000", "1", "1428_1000_1.jpg"});
		// enlarging width and height
		Main.main(new String[]{"originalcastle.jpg", "1453", "1000", "1", "1453_1000_1.jpg"});
		// enlarging width reducing height
		Main.main(new String[]{"originalcastle.jpg", "1453", "950", "1", "1453_950_1.jpg"});
		// enlarging height reducing width
		Main.main(new String[]{"originalcastle.jpg", "1400", "1000", "1", "1400_1000_1.jpg"});
		
		// energyType == 2
		
		// TODO
	}
}

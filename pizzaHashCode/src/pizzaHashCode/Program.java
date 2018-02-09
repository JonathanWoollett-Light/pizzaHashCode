package pizzaHashCode;

//import java.util.List;
//import java.util.ArrayList;
import java.util.Random;

public class Program {
	private final static int maxSliceSize = 6;
	private final static int minTomatoes = 1;
	private final static int minMushrooms = 1;
	
	//private ArrayList<ArrayList<Char>> pizza;
	
	private final static int pizzaXSize = 10;
	private final static int pizzaYSize = 10;
	private static char[][] pizza = new char[pizzaYSize][pizzaXSize];
	private static int[][] pizzaSlices = new int[pizzaYSize][pizzaXSize];
	
	public static void main(String args[]) {
		
		makePizza();
		
		int sliceCounter = 0;
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaXSize; x++) {
				if(pizzaSlices[y][x] == 0) {
					if(createSlice(y, x, sliceCounter)) {
						sliceCounter++;
						
					}
				}
			}
		}
	}
	public static boolean createSlice(int originX, int originY, int sliceCounter) {
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		
		int firstMethodSize = 0;
		firstMethod:
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < 2; x++) {
				
				if(pizzaSlices[y][x] != 0) {
					revertSlice(1, originX, originY, x, y);
					firstMethodSize = -1;
					break firstMethod;
				}
				
				pizzaSlices[y][x] = sliceCounter + 1;
				switch(pizza[y][x]) {
				case 'T':
					containedTomatoes++;
					break;
				case 'M':
					containedMushrooms++;
				}
				firstMethodSize++;
				if(containedTomatoes > minTomatoes && containedMushrooms > minMushrooms) {
					break firstMethod;
				}
			}
		}
	}
	public static void revertSlice(int method, int originX, int originY, int lastX, int lastY) {
		if(method == 1) {
			for(int y = lastY; y > originY; y--) {
				for(int x = lastX; x > originX - 1; x--) {
					pizzaSlices[y][x] = 0;
				}
			}
		}else if(method == 2) {
			
			for(int y = lastY; y > originY; y--) {
				for(int x = lastX; x > originX - 1; x--) {
					pizzaSlices[y][x] = 0;
				}
			}
		}
	}
	public static void makePizza() {
		Random rand = new Random();
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaXSize; x++) {
				switch(rand.nextInt() % 2) {
				case 0:
					pizza[y][x] = 'T';
					break;
				case 1:
					pizza[y][x] = 'M';
				}
			}
		}
	}
}

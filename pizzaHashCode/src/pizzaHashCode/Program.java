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
		int firstMethodSize = verticalXSlice(originX, originY, sliceCounter);
		if(firstMethodSize == minTomatoes + minMushrooms) {
			return true;
		}
		revertSlice(sliceCounter + 1);
		int minValue = firstMethodSize;
		int minIndex = 0;
		
		int secondMethodSize = verticalYSlice(originX, originY, sliceCounter);
		if(secondMethodSize == minTomatoes + minMushrooms) {
			return true;
		}
		revertSlice(sliceCounter + 1);
		
		if(secondMethodSize < minValue) {
			minValue = secondMethodSize;
			minIndex = 1;
		}
		
		int thirdMethodSize = horizontalXSlice(originX, originY, sliceCounter);
		if(thirdMethodSize == minTomatoes + minMushrooms) {
			return true;
		}
		revertSlice(sliceCounter + 1);
		
		if(thirdMethodSize < minValue) {
			minValue = thirdMethodSize;
			minIndex = 2;
		}
		
		int fourthMethodSize = horizontalYSlice(originX, originY, sliceCounter);
		if(fourthMethodSize == minTomatoes + minMushrooms) {
			return true;
		}
		revertSlice(sliceCounter + 1);
		if(fourthMethodSize < minValue) {
			minValue = fourthMethodSize;
			minIndex = 3;
			
		}
		
		if(minValue == Integer.MAX_VALUE) {
			return false;
		}
		switch(minIndex) {
		case 0:
			verticalXSlice(originX, originY, sliceCounter);
			break;
		case 1:
			verticalYSlice(originX, originY, sliceCounter);
			break;
		case 2:
			horizontalXSlice(originX, originY, sliceCounter);
			break;
		case 3:
			horizontalYSlice(originX, originY, sliceCounter);
		}
		return true;
	}
	
	public static int verticalXSlice(int originX, int originY, int sliceCounter) {
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		int firstMethodSize = 0;
		
		for(int y = originY; y < pizzaYSize; y++) {
			if(pizzaSlices[y][originX] != 0 || pizzaSlices[y][originX + 1] != 0 || firstMethodSize == maxSliceSize) {
				revertSlice(true, originX, originY, y);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices[y][originX] = sliceCounter + 1;
			pizzaSlices[y][originX + 1] = sliceCounter + 1;
			
			if(pizza[y][originX] == 'T') {
				containedTomatoes++;
			}else if(pizza[y][originX] == 'M') {
				containedMushrooms++;
			}
			if(pizza[y][originX + 1] == 'T') {
				containedTomatoes++;
			}else if(pizza[y][originX + 1] == 'M') {
				containedMushrooms++;
			}
			
			firstMethodSize += 2;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				return firstMethodSize;
			}
		}
		return Integer.MAX_VALUE;//this is actually impossible to hit but Eclipse is complaining when this is not here
	}
	public static int verticalYSlice(int originX, int originY, int sliceCounter) {
		int secondMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int y = originY; y < pizzaYSize; y++) {
			if(pizzaSlices[y][originX] != 0 || secondMethodSize == maxSliceSize) {
				revertSlice(true, originX, originY, false, y);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices[y][originX] = sliceCounter + 1;
			
			if(pizza[y][originX] == 'T') {
				containedTomatoes++;
			}else if(pizza[y][originX] == 'M') {
				containedMushrooms++;
			}
			secondMethodSize++;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				return secondMethodSize;
			}
		}
		for(int y = originY; y < pizzaYSize; y++) {
			if(pizzaSlices[y][originX + 1] != 0) {
				revertSlice(true, originX, originY, true, y);
				return Integer.MAX_VALUE;
			}
			
			if(pizza[y][originX + 1] == 'T') {
				containedTomatoes++;
			}else if(pizza[y][originX + 1] == 'M') {
				containedMushrooms++;
			}
			
			pizzaSlices[y][originX + 1] = sliceCounter + 1;
		}
		secondMethodSize += pizzaYSize - originY;
		if(!(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) || secondMethodSize > maxSliceSize) {
			revertSlice(true, originX, originY, true, pizzaYSize - 1);
			return Integer.MAX_VALUE;
		}
		return secondMethodSize;
	}
	public static int horizontalXSlice(int originX, int originY, int sliceCounter) {
		int thirdMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < pizzaXSize; x++) {
			if(pizzaSlices[originY][x] != 0 || pizzaSlices[originY + 1][x] != 0 || thirdMethodSize == maxSliceSize) {
				revertSlice(false, originX, originY, x);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices[originY][x] = sliceCounter + 1;
			pizzaSlices[originY + 1][x] = sliceCounter + 1;
			
			if(pizza[originY][x] == 'T') {
				containedTomatoes++;
			}else if(pizza[originY][x] == 'M') {
				containedMushrooms++;
			}
			if(pizza[originY + 1][x] == 'T') {
				containedTomatoes++;
			}else if(pizza[originY + 1][x] == 'M') {
				containedMushrooms++;
			}
			
			thirdMethodSize += 2;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				return thirdMethodSize;
			}
		}
		return Integer.MAX_VALUE;//this is actually impossible to hit but Eclipse is complaining when this is not here
	}
	public static int horizontalYSlice(int originX, int originY, int sliceCounter) {
		int fourthMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < pizzaXSize; x++) {
			if(pizzaSlices[originY][x] != 0 || fourthMethodSize == maxSliceSize) {
				revertSlice(false, originX, originY, false, x);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices[originY][x] = sliceCounter + 1;
			
			if(pizza[originY][x] == 'T') {
				containedTomatoes++;
			}else if(pizza[originY][x] == 'M') {
				containedMushrooms++;
			}
			fourthMethodSize++;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				return fourthMethodSize;
			}
		}
		for(int x = originX; x < pizzaXSize; x++) {
			if(pizzaSlices[originY + 1][x] != 0) {
				revertSlice(false, originX, originY, true, x);
				return Integer.MAX_VALUE;
			}
			
			if(pizza[originY + 1][x] == 'T') {
				containedTomatoes++;
			}else if(pizza[originY + 1][x] == 'M') {
				containedMushrooms++;
			}
			
			pizzaSlices[originY + 1][x] = sliceCounter + 1;
		}
		fourthMethodSize += pizzaXSize - originX;
		if(!(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) || fourthMethodSize > maxSliceSize) {
			revertSlice(false, originX, originY, true, pizzaXSize - 1);
			return Integer.MAX_VALUE;
		}
		return fourthMethodSize;
	}
	
	
	public static void revertSlice(int sliceCounter) {
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaYSize; x++) {
				if(pizza[y][x] == sliceCounter) {
					pizza[y][x] = 0;
				}
			}
		}
	}
	public static void revertSlice(boolean method, int originX, int originY, int lastValue) {
		if(method) {
			for(int y = lastValue; y >= originY; y--) {
				pizzaSlices[y][originX] = 0;
				pizzaSlices[y][originX + 1] = 0;
			}
		}else {
			for(int x = lastValue; x >= originX; x--) {
				pizzaSlices[originY][x] = 0;
				pizzaSlices[originY + 1][x] = 0;
			}
		}
		
	}
	public static void revertSlice(boolean method, int originX, int originY, boolean twoColumns, int lastValue) {
		if(method) {
			for(int y = lastValue; y >= originY; y--) {
				pizzaSlices[y][originX] = 0;
			}
			if(twoColumns) {
				for(int y = lastValue; y >= originY; y--) {
					pizzaSlices[y][originX + 1] = 0;
				}
			}
		}else {
			for(int x = lastValue; x >= originX; x--) {
				pizzaSlices[originY][x] = 0;
			}
			if(twoColumns) {
				for(int x = lastValue; x >= originX; x--) {
					pizzaSlices[originY + 1][x] = 0;
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

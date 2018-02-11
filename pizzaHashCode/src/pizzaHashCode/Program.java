package pizzaHashCode;

//import java.util.List;
//import java.util.ArrayList;
import java.util.Random;

public class Program {
	private final static int maxSliceSize = 6;
	private final static int minTomatoes = 1;
	private final static int minMushrooms = 1;
	
	private final static int pizzaXSize = 10;
	private final static int pizzaYSize = 10;
	private static char[][] pizza = new char[pizzaYSize][pizzaXSize];//this will likely have to be changed to be an ArrayList or Vector to accomodate inputs of varying pizza sizes
	private static int[][] pizzaSlices = new int[pizzaYSize][pizzaXSize];//the same comment as made above applies here
	
	public static void main(String args[]) {
		
		makePizza();
		
		int sliceCounter = 0;
		//loops through all coordinates in the pizza and creates a slice if possible, keep in mind there will still almost certainly be spaces after this has finished
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaXSize; x++) {
				if(pizzaSlices[y][x] == 0) {
					if(createSlice(y, x, sliceCounter)) {//if a slice can be created from this origin
						sliceCounter++;
					}
				}
			}
		}
	}
	public static boolean createSlice(int originX, int originY, int sliceCounter) {
		/* runs through all methods of creating slices then picks slice with smallest size
		 * the red underline here seems stupid since it actually does always return a boolean)
		 */
		int firstMethodSize = verticalXSlice(originX, originY, sliceCounter);
		if(firstMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		int minValue = firstMethodSize;
		int minIndex = 0;
		
		int secondMethodSize = verticalYSlice(originX, originY, sliceCounter);
		if(secondMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(secondMethodSize < minValue) {
			minValue = secondMethodSize;
			minIndex = 1;
		}
		
		int thirdMethodSize = horizontalXSlice(originX, originY, sliceCounter);
		if(thirdMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(thirdMethodSize < minValue) {
			minValue = thirdMethodSize;
			minIndex = 2;
		}
		
		int fourthMethodSize = horizontalYSlice(originX, originY, sliceCounter);
		if(fourthMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(fourthMethodSize < minValue) {
			minValue = fourthMethodSize;
			minIndex = 3;
			
		}
		
		if(minValue == Integer.MAX_VALUE) {
			return false;
		}
		switch(minIndex) {//pick smallest slicing method
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
			if(pizzaSlices[y][originX] != 0 || pizzaSlices[y][originX + 1] != 0 || firstMethodSize >= maxSliceSize) {
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
				if(firstMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(true, originX, originY, y);
				}
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
			if(pizzaSlices[y][originX] != 0 || secondMethodSize >= maxSliceSize) {
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
				if(secondMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(true, originX, originY, false, y);
				}
				return secondMethodSize;//there is not a revert here since if
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
		if(secondMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
			revertSlice(true, originX, originY, true, pizzaYSize - 1);
		}
		return secondMethodSize;
	}
	public static int horizontalXSlice(int originX, int originY, int sliceCounter) {
		int thirdMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < pizzaXSize; x++) {
			if(pizzaSlices[originY][x] != 0 || pizzaSlices[originY + 1][x] != 0 || thirdMethodSize >= maxSliceSize) {
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
				if(thirdMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(false, originX, originY, x);
				}
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
			if(pizzaSlices[originY][x] != 0 || fourthMethodSize >= maxSliceSize) {
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
				if(fourthMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(false, originX, originY, false, x);
				}
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
		if(fourthMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
			revertSlice(false, originX, originY, true, pizzaXSize - 1);
		}
		return fourthMethodSize;
	}
	
	public static void revertSlice(int sliceCounter) {//although this method can be used for every slicing type, it is more efficient to use the more specific methods
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaYSize; x++) {
				if(pizza[y][x] == sliceCounter) {
					pizza[y][x] = 0;
				}
			}
		}
	}
	public static void revertSlice(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
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
	public static void revertSlice(boolean method, int originX, int originY, boolean twoColumns, int lastValue) {//if method = true its vertical slice otherwise its horizontal
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
	
	public static void makePizza() {//randomly generate a pizza for testing
		Random rand = new Random();
		for(int y = 0; y < pizzaYSize; y++) {
			for(int x = 0; x < pizzaXSize; x++) {
				if(rand.nextInt() % 2 == 0) {
					pizza[y][x] = 'T';
				}else {
					pizza[y][x] = 'M';
				}
			}
		}
	}
}

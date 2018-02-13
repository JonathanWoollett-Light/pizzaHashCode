package pizzaHashCode;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Program {

	// This is H on the problem sheet
	private static int maxSliceSize = 6;
	
	// Both equivalent to L
	private static int minTomatoes = 1;
	private static int minMushrooms = 2;
	
	// Y is rows
	// X is columns
	
	private static int rows = 6;
	private static int columns = 6;
	
	private static ArrayList <ArrayList<Character>> pizza = new ArrayList<ArrayList<Character>>();
	private static ArrayList <ArrayList<Integer>> pizzaSlices = new ArrayList<ArrayList<Integer>>();
	
	private static int sliceCounter = 0;
	
	private static int minimumGaps;//must be intialised to a non zero value
	
	public static void main(String args[])
	{
		//inputParser();
		makePizza();
		print2dCharArrayList(pizza);
		
		intialisePizzaSlices();
		
		System.out.println("minTomatoes: " + minTomatoes);
		System.out.println("minMushrooms: " + minMushrooms);
		System.out.println("maxSliceSize: " + maxSliceSize);
		
		
		//loops through all coordinates in the pizza and creates a slice if possible, keep in mind there will still almost certainly be spaces after this has finished
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				if(pizzaSlices.get(y).get(x) == 0) {//if this space is not in any other slice
					if(createSlice(x, y, sliceCounter)) {//if a slice can be created from this origin
						sliceCounter++;
					}
				}
			}
		}
		if(countZeroes(pizzaSlices) != 0) {
			expandSlices();
		}
		double percentageCovered = ((rows * columns) - countZeroes(pizzaSlices)) / (rows * columns);
		System.out.println("\n--------------------------------------\n" + percentageCovered);
	}
	
	private static void expandSlices() {
		ArrayList<Integer> arr = new ArrayList<Integer>(sliceCounter);
		for(int i=0;i<sliceCounter;i++) {
			arr.add(i+1);
		}
		ArrayList<ArrayList<Integer>> orders = new ArrayList<ArrayList<Integer>>(getOrders(arr));
		print2dIntArrayList(orders);
		print2dIntArrayList(pizzaSlices);
		minimumGaps = countZeroes(pizzaSlices);
		for(int i=0;i<orders.size();i++) {
			recursiveExpansion(new ArrayList<ArrayList<Integer>>(pizzaSlices), orders.get(i), 0);
			//System.out.println("\n\n----------------------------------------------------------------------------------------------\n\n");
		}
	}
	
	private static void recursiveExpansion(ArrayList<ArrayList<Integer>> arr, ArrayList<Integer> order, int indexInOrder) {
		
		if(indexInOrder == order.size() || minimumGaps == 0) {
			return;
		}
		
		int activeSlice = order.get(indexInOrder);
		
		/*System.out.println("\nBest pizza:");
		print2dIntArrayList(pizzaSlices);
		System.out.println("Current pizza");
		print2dIntArrayList(arr);
		System.out.println("Current order");
		printIntArrayList(order);
		System.out.println();
		System.out.println("activeSlice: " + activeSlice);*/
		
		
		
		
		int holder = countZeroes(arr);
		if(holder < minimumGaps) {
			copyArray(arr, pizzaSlices);
			System.out.println("Updated best");
			print2dIntArrayList(pizzaSlices);
			minimumGaps = holder;
		}
		
		ArrayList<ArrayList<Integer>> tempPizzaSlices = new ArrayList<ArrayList<Integer>>(arr);
		boolean expandedSlice = false;
		boolean expandableSlice = false;
		
		while(indexInOrder < sliceCounter && !expandableSlice) {
			expandableSlice = false;
			
			activeSlice = order.get(indexInOrder);
			//System.out.println("activeSlice: " + activeSlice);
			//System.out.println("Current pizza");
			//print2dIntArrayList(arr);
			while(canExpand(tempPizzaSlices, 0, activeSlice)) {//-y
				//System.out.println("0: " + 0);
				expandedSlice = false;
				for(int y=0;y<rows;y++) {
					for(int x=0;x<columns;x++) {
						if(pizzaSlices.get(y).get(x) == activeSlice && pizzaSlices.get(y - 1).get(x) != activeSlice) {
							/*System.out.println("Pre edit");
							print2dIntArrayList(tempPizzaSlices);
							System.out.println("In edit");
							System.out.println("y: " + y + " x: " + x + " activeSlice: " + activeSlice);*/
							
							tempPizzaSlices.get(y - 1).set(x, activeSlice);
							
							//System.out.println("Post edit");
							//print2dIntArrayList(tempPizzaSlices);
							
							expandedSlice = true;
						}
					}
					if(expandedSlice) {
						//System.out.println("Next pizza");
						//print2dIntArrayList(tempPizzaSlices);
						expandableSlice = true;
						recursiveExpansion(new ArrayList<ArrayList<Integer>>(tempPizzaSlices), order, indexInOrder + 1);
						break;
					}
				}
			}
			copyArray(arr, tempPizzaSlices);
			
			while(canExpand(tempPizzaSlices, 1, activeSlice)) {//+y
				//System.out.println("1: " + 1);
				expandedSlice = false;
				for(int y=0;y<rows;y++) {
					for(int x=0;x<columns;x++) {
						if(pizzaSlices.get(y).get(x) == activeSlice  && pizzaSlices.get(y + 1).get(x) != activeSlice) {
							
							/*System.out.println("Pre edit");
							print2dIntArrayList(tempPizzaSlices);
							System.out.println("In edit");
							System.out.println("y: " + y + " x: " + x + " activeSlice: " + activeSlice);*/
							
							tempPizzaSlices.get(y + 1).set(x, activeSlice);
							
							//System.out.println("Post edit");
							//print2dIntArrayList(tempPizzaSlices);
							expandedSlice = true;
						}
					}
					if(expandedSlice) {
						//System.out.println("Next pizza");
						//print2dIntArrayList(tempPizzaSlices);
						expandableSlice = true;
						recursiveExpansion(new ArrayList<ArrayList<Integer>>(tempPizzaSlices), order, indexInOrder + 1);
						break;
					}
				}
			}
			copyArray(arr, tempPizzaSlices);
			
			while(canExpand(tempPizzaSlices, 2, activeSlice)) {//-x
				//System.out.println("2: " + 2);
				expandedSlice = false;
				for(int x=0;x<columns;x++) {
					for(int y=0;y<rows;y++) {
						if(pizzaSlices.get(y).get(x) == activeSlice  && pizzaSlices.get(y).get(x - 1) != activeSlice) {
							/*System.out.println("Pre edit");
							print2dIntArrayList(tempPizzaSlices);
							System.out.println("In edit");
							System.out.println("y: " + y + " x: " + x + " activeSlice: " + activeSlice);*/
							tempPizzaSlices.get(y).set(x - 1, activeSlice);
							
							//System.out.println("Post edit");
							//print2dIntArrayList(tempPizzaSlices);
							expandedSlice = true;
						}
					}
					if(expandedSlice) {
						//System.out.println("Next pizza");
						//print2dIntArrayList(tempPizzaSlices);
						expandableSlice = true;
						recursiveExpansion(new ArrayList<ArrayList<Integer>>(tempPizzaSlices), order, indexInOrder + 1);
						break;
					}
				}
			}
			copyArray(arr, tempPizzaSlices);
			
			while(canExpand(tempPizzaSlices, 3, activeSlice)) {//+x
				//System.out.println("3: " + 3);
				expandedSlice = false;
				for(int x=0;x<columns;x++) {
					for(int y=0;y<rows;y++) {
						if(pizzaSlices.get(y).get(x) == activeSlice  && pizzaSlices.get(y).get(x + 1) != activeSlice) {
							/*System.out.println("Pre edit");
							print2dIntArrayList(tempPizzaSlices);
							System.out.println("In edit");
							System.out.println("y: " + y + " x: " + x + " activeSlice: " + activeSlice);*/
							tempPizzaSlices.get(y).set(x + 1, activeSlice);
							//System.out.println("Post edit");
							//print2dIntArrayList(tempPizzaSlices);
							expandedSlice = true;
						}
					}
					if(expandedSlice) {
						//System.out.println("Next pizza");
						//print2dIntArrayList(tempPizzaSlices);
						expandableSlice = true;
						recursiveExpansion(new ArrayList<ArrayList<Integer>>(tempPizzaSlices), order, indexInOrder + 1);
						break;
					}
				}
			}
			copyArray(arr, tempPizzaSlices);
			indexInOrder++;
			//System.out.println("expandableSlice: " + expandableSlice);
		}
		
	}
	
	private static int countZeroes(ArrayList<ArrayList<Integer>> arr) {
		int count = 0;
		for(int y=0;y<arr.size();y++) {
			for(int x=0;x<arr.get(y).size();x++) {
				if(arr.get(y).get(x) == 0) {
					count++;
				}
			}
		}
		return count;
	}
	
	private static boolean canExpand(ArrayList<ArrayList<Integer>> arr, int direction, int slice) {
		//System.out.println("direxction: " + direction);
		//System.out.println("slice: " + slice);
		int newSize = 0;
		switch(direction) {
		case 0://-y
			for(int y=0;y<rows;y++) {
				for(int x=0;x<columns;x++) {
					if(arr.get(y).get(x) == slice) {
						newSize++;
						if(y == 0) {
							return false;
						}else if(arr.get(y - 1).get(x) == 0) {
							newSize++;
						}else if(arr.get(y - 1).get(x) != slice) {
							return false;
						}
						
					}
				}
			}
			break;
		case 1://+y
			for(int y=0;y<rows;y++) {
				for(int x=0;x<columns;x++) {
					if(arr.get(y).get(x) == slice) {
						newSize++;
						if(y == rows - 1) {
							return false;
						}else if(arr.get(y + 1).get(x) == 0) {
							newSize++;
						}else if(arr.get(y + 1).get(x) != slice) {
							return false;
						}
					}
				}
			}
			break;
		case 2://-x
			for(int x=0;x<columns;x++) {
				for(int y=0;y<rows;y++) {
					if(arr.get(y).get(x) == slice) {
						newSize++;
						if(x == 0) {
							return false;
						}else if(arr.get(y).get(x - 1) == 0) {
							newSize++;
						}else if(arr.get(y).get(x - 1) != slice) {
							return false;
						}
					}
				}
			}
			break;
		case 3://+x
			for(int x=0;x<columns;x++) {
				for(int y=0;y<rows;y++) {
					if(arr.get(y).get(x) == slice) {
						newSize++;
						if(x == columns - 1) {
							return false;
						}else if(arr.get(y).get(x + 1) == 0) {
							newSize++;
						}else if(arr.get(y).get(x + 1) != slice) {
							return false;
						}
					}
				}
			}
		}
		//System.out.println("newSize: " + newSize);
		if(newSize > maxSliceSize) {
			return false;
		}
		//System.out.println("Can expand");
		return true;
	}
	
	private static ArrayList<ArrayList<Integer>> getOrders(ArrayList<Integer> arr){
		ArrayList<ArrayList<Integer>> returnArray = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> checkingHolder = new ArrayList<Integer>(sliceCounter);
		for(int i=0;i<sliceCounter;i++) {
			returnArray.add(new ArrayList<Integer>(shiftArray(arr, i)));
		}
		//print2dIntArrayList(returnArray);
		for(int i=0;i<sliceCounter;i++) {
			//System.out.println("i: " + i);
			//printIntArrayList(returnArray.get(i));
			//System.out.println();
			for(int t=0;t<sliceCounter;t++) {
				//System.out.println("t: " + t);
				specificIndexShifts:
					for(int u=1;true;u++) {
						//System.out.println("u: " + u);
						checkingHolder = shiftAroundIndex(returnArray.get(i), t, u);
						for(int q=0;q<returnArray.size();q++) {
							if(ArrayListEqaulity(returnArray.get(q), checkingHolder)) {
								break specificIndexShifts;
							}
						}
						//printIntArrayList(checkingHolder);
						//System.out.println();
						returnArray.add(new ArrayList<Integer>(checkingHolder));
					}
			}
		}
		return returnArray;
		
	}
	
	private static ArrayList<Integer> shiftAroundIndex(ArrayList<Integer> arr, int index, int numbOfShifts){
		ArrayList<Integer> holder = new ArrayList<Integer>(arr);
		//System.out.println("Index: " + index);
		//System.out.print("Pre: ");
		//printIntArrayList(holder);
		//System.out.println();
		//copyArray(arr, holder);
		if(numbOfShifts > 0) {
			for(int i = 0; i < holder.size(); i++) {
				if(i == index) {
					continue;
				}
				if(i + 1 == index) {
					if(index == holder.size() - 1) {
						holder.set(0, arr.get(i));
					}else {
						holder.set(i + 2, arr.get(i));
					}
				}else if(i == holder.size() - 1) {
					if(index == 0) {
						holder.set(1, arr.get(i));
					}else {
						holder.set(0, arr.get(i));
					}
				}else {
					holder.set(i + 1, arr.get(i));
				}
			}
			if(numbOfShifts > 1) {
				return shiftAroundIndex(holder, index, numbOfShifts - 1);
			}
		}
		//System.out.print("Post: ");
		//printIntArrayList(holder);
		//System.out.println();
		return holder;
	}
	
	private static ArrayList<Integer> shiftArray(ArrayList<Integer> arr, int numbOfShifts) {
		ArrayList<Integer> holder = new ArrayList<Integer>(arr);
		//copyArray(arr, holder);
		
		if(numbOfShifts > 0) {
			for(int i = 1;i < holder.size() + 1; i++) {
				if(i == holder.size()) {
					holder.set(0, arr.get(i - 1));
				}else {
					holder.set(i, arr.get(i - 1));
				}
			}
			if(numbOfShifts > 1) {
				return shiftArray(holder, numbOfShifts - 1);
			}
		}
		return holder;
	}

	
	private static boolean ArrayListEqaulity(ArrayList<Integer> a, ArrayList<Integer> b) {
		if(a.size() != b.size()) {
			return false;
		}
		for(int i=0;i<a.size();i++) {
			if(a.get(i) != b.get(i)) {
				return false;
			}
		}
		return true;
	}
 	
	private static void copyArray(ArrayList<ArrayList<Integer>> from, ArrayList<ArrayList<Integer>> to) {
		for(int i=0;i<from.size();i++) {
			for(int t=0;t<from.size();t++) {
				to.get(i).set(t, from.get(i).get(t));
			}
			
		}
	}
	
	private static boolean createSlice(int originX, int originY, int sliceCounter) {//runs through all methods of creating slices then picks slice with smallest size
		//System.out.println("---------------------" + (sliceCounter + 1) + "---------------------");
		int firstMethodSize = verticalXSlice(originX, originY, sliceCounter, true);
		//System.out.println("firstMethodSize: " + firstMethodSize);
		if(firstMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		int minValue = firstMethodSize;
		int minIndex = 0;
		int secondMethodSize = verticalYSlice(originX, originY, sliceCounter, true);
		//System.out.println("secondMethodSize: " + secondMethodSize);
		if(secondMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(secondMethodSize < minValue) {
			minValue = secondMethodSize;
			minIndex = 1;
		}
		
		int thirdMethodSize = horizontalXSlice(originX, originY, sliceCounter, true);
		//System.out.println("thirdMethodSize: " + thirdMethodSize);
		if(thirdMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(thirdMethodSize < minValue) {
			minValue = thirdMethodSize;
			minIndex = 2;
		}
		
		int fourthMethodSize = horizontalYSlice(originX, originY, sliceCounter, true);
		//System.out.println("fourthMethodSize: " + fourthMethodSize);
		if(fourthMethodSize == minTomatoes + minMushrooms) {//if this method is the smallest any method can be
			return true;
		}
		
		if(fourthMethodSize < minValue) {
			minValue = fourthMethodSize;
			minIndex = 3;
			
		}
		
		//System.out.println("minValue: " + minValue);
		//System.out.println("minIndex: " + minIndex);
		if(minValue > maxSliceSize) {//if none of the functions could create a slice
			return false;
		}
		switch(minIndex) {//pick smallest slicing method
		case 0:
			verticalXSlice(originX, originY, sliceCounter, false);
			break;
		case 1:
			verticalYSlice(originX, originY, sliceCounter, false);
			break;
		case 2:
			horizontalXSlice(originX, originY, sliceCounter, false);
			break;
		case 3:
			horizontalYSlice(originX, originY, sliceCounter, false);
		}
		return true;
	}
	
	private static int verticalXSlice(int originX, int originY, int sliceCounter, boolean isTest) {
		if(originX == columns - 1) {
			return Integer.MAX_VALUE;
		}
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		int firstMethodSize = 0;
		
		for(int y = originY; y < rows; y++) {
			if(pizzaSlices.get(y).get(originX) != 0 || pizzaSlices.get(y).get(originX + 1) != 0 || firstMethodSize >= maxSliceSize) {
				revertSliceRect(true, originX, originY, y - 1);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices.get(y).set(originX, sliceCounter + 1);
			pizzaSlices.get(y).set(originX + 1, sliceCounter + 1);
			
			if(pizza.get(y).get(originX) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(y).get(originX) == 'M') {
				containedMushrooms++;
			}
			if(pizza.get(y).get(originX + 1) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(y).get(originX + 1) == 'M') {
				containedMushrooms++;
			}
			
			firstMethodSize += 2;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				if(firstMethodSize != minTomatoes + minMushrooms && isTest) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSliceRect(true, originX, originY, y);
				}
				return firstMethodSize;
			}
		}
		revertSliceRect(true, originX, originY, rows - 1);
		return Integer.MAX_VALUE;//this is actually impossible to hit but Eclipse is complaining when this is not here
	}
	private static int verticalYSlice(int originX, int originY, int sliceCounter, boolean isTest) {
		int secondMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int y = originY; y < rows; y++) {
			if(pizzaSlices.get(y).get(originX) != 0 || secondMethodSize >= maxSliceSize) {
				revertSliceLine(true, originX, originY, y - 1);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices.get(y).set(originX, sliceCounter + 1);
			
			if(pizza.get(y).get(originX) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(y).get(originX) == 'M') {
				containedMushrooms++;
			}
			secondMethodSize++;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				if(secondMethodSize != minTomatoes + minMushrooms && isTest) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSliceLine(true, originX, originY, y);
				}
				return secondMethodSize;//there is not a revert here since if
			}
		}
		revertSliceLine(true, originX, originY, rows - 1);
		return Integer.MAX_VALUE;
	}
	private static int horizontalXSlice(int originX, int originY, int sliceCounter, boolean isTest) {
		if(originY == rows - 1) {
			return Integer.MAX_VALUE;
		}
		int thirdMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < columns; x++) {
			if(pizzaSlices.get(originY).get(x) != 0 || pizzaSlices.get(originY + 1).get(x) != 0 || thirdMethodSize >= maxSliceSize) {
				revertSliceRect(false, originX, originY, x - 1);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices.get(originY).set(x, sliceCounter + 1);
			pizzaSlices.get(originY + 1).set(x, sliceCounter + 1);
			
			if(pizza.get(originY).get(x) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(originY).get(x) == 'M') {
				containedMushrooms++;
			}
			if(pizza.get(originY + 1).get(x) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(originY + 1).get(x) == 'M') {
				containedMushrooms++;
			}
			
			thirdMethodSize += 2;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				if(thirdMethodSize != minTomatoes + minMushrooms && isTest) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSliceRect(false, originX, originY, x);
				}
				return thirdMethodSize;
			}
		}
		revertSliceRect(false, originX, originY, columns - 1);
		return Integer.MAX_VALUE;//this is actually impossible to hit but Eclipse is complaining when this is not here
	}
	private static int horizontalYSlice(int originX, int originY, int sliceCounter, boolean isTest) {
		int fourthMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < columns; x++) {
			if(pizzaSlices.get(originY).get(x) != 0 || fourthMethodSize >= maxSliceSize) {
				revertSliceLine(false, originX, originY, x - 1);
				return Integer.MAX_VALUE;
			}
			
			pizzaSlices.get(originY).set(x, sliceCounter + 1);
			
			if(pizza.get(originY).get(x) == 'T') {
				containedTomatoes++;
			}else if(pizza.get(originY).get(x) == 'M') {
				containedMushrooms++;
			}
			fourthMethodSize++;
			if(containedTomatoes >= minTomatoes && containedMushrooms >= minMushrooms) {
				if(fourthMethodSize != minTomatoes + minMushrooms && isTest) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSliceLine(false, originX, originY, x);
				}
				return fourthMethodSize;
			}
		}
		revertSliceLine(false, originX, originY, columns - 1);
		return Integer.MAX_VALUE;
	}
	
	private static void revertSlice(int sliceCounter) {//although this method can be used for every slicing type, it is more efficient to use the more specific methods
		for(int y = 1; y < rows; y++) {
			for(int x = 1; x < columns; x++) {
				if(pizza.get(y).get(x) == sliceCounter) {
					pizza.get(y).set(x, (char) 0);
				}
			}
		}
	}
	private static void revertSliceRect(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
		if(method) {
			for(int y = lastValue; y >= originY; y--) {
				pizzaSlices.get(y).set(originX, 0);
				pizzaSlices.get(y).set(originX + 1, 0);
			}
		}else {
			for(int x = lastValue; x >= originX; x--) {
				pizzaSlices.get(originY).set(x, 0);
				pizzaSlices.get(originY + 1).set(x, 0);
			}
		}
		
	}
	private static void revertSliceLine(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
		if(method) {
			for(int y = lastValue; y >= originY; y--) {
				pizzaSlices.get(y).set(originX, 0);
			}
		}else {
			for(int x = lastValue; x >= originX; x--) {
				pizzaSlices.get(originY).set(x, 0);
			}
		}
		
	}
	
	
	private static void makePizza() {
		Random rand = new Random();
		ArrayList<Character> holder = new ArrayList<Character>(columns);
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				
				if(rand.nextInt() % 2 == 0) {
					holder.add('T');
					
				}else {
					holder.add('M');
				}
			}
			pizza.add(new ArrayList<Character>(holder));
			holder.clear();
		}
	}
	
	private static void printIntArrayList(ArrayList<Integer> currentList) {
		System.out.print("{");
		for(int i = 0;i < currentList.size(); i++) {
			if(i == 0) {
				System.out.print(currentList.get(i));
			}else {
				System.out.print("," + currentList.get(i));
			}
		}
		System.out.print("}");
	}
	private static void print2dIntArrayList(ArrayList<ArrayList<Integer>> currentList) {
		System.out.println("{");
		for(int i = 0;i < currentList.size(); i++) {
			printIntArrayList(currentList.get(i));
			System.out.println(" : " + i);
		}
		System.out.println("}");
		System.out.println("Count: " + currentList.size());
	}
	private static void printCharArrayList(ArrayList<Character> currentList) {
		System.out.print("{");
		for(int i = 0 ;i < currentList.size(); i++) {
			if(i == 0) {
				System.out.print(currentList.get(i));
			}else {
				System.out.print("," + currentList.get(i));
			}
		}
		System.out.print("}");
	}
	private static void print2dCharArrayList(ArrayList<ArrayList<Character>> currentList) {
		System.out.println("{");
		for(int i=0;i<currentList.size();i++) {
			printCharArrayList(currentList.get(i));
			System.out.println(" : " + i);
		}
		System.out.println("}");
		System.out.println("Count: " + currentList.size());
	}
	
	private static void intialisePizzaSlices() {
		ArrayList<Integer> holder = new ArrayList<Integer>(Collections.nCopies(columns, 0));
		for(int y = 0; y < rows; y++) {
			pizzaSlices.add(new ArrayList<Integer>(holder));
		}
	}
}

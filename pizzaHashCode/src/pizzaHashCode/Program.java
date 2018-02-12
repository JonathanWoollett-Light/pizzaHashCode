package pizzaHashCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class Program {

	// This is H on the problem sheet
	private static int maxSliceSize = 6;
	
	// Both equivalent to L
	private static int minTomatoes = 1;
	private static int minMushrooms = 1;
	
	// Y is rows
	// X is columns
	
	private static int rows = 0;
	private static int columns = 0;
	
	static ArrayList <ArrayList<Character>> pizza = new ArrayList<ArrayList<Character>>();
	static ArrayList <ArrayList<Integer>> pizzaSlices = new ArrayList<ArrayList<Integer>>();
	
	public static void main(String args[]) throws FileNotFoundException, IOException 
	{
		inputParser();
		makePizza();
		print2dCharArrayList(pizza);
		
		intialisePizzaSlices();
		print2dIntArrayList(pizzaSlices);
		
		int sliceCounter = 0;
		//loops through all coordinates in the pizza and creates a slice if possible, keep in mind there will still almost certainly be spaces after this has finished
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				if(pizzaSlices.get(y).get(x) == 0) {
					if(createSlice(x, y, sliceCounter)) {//if a slice can be created from this origin
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
		if(originX == columns - 1) {
			return Integer.MAX_VALUE;
		}
		
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		int firstMethodSize = 0;
		
		for(int y = originY; y < rows; y++) {
			if(pizzaSlices.get(y).get(originX) != 0 || pizzaSlices.get(y).get(originX + 1) != 0 || firstMethodSize >= maxSliceSize) {
				revertSlice(true, originX, originY, y);
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
		for(int y = originY; y < rows; y++) {
			if(pizzaSlices.get(y).get(originX) != 0 || secondMethodSize >= maxSliceSize) {
				revertSlice(true, originX, originY, false, y);
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
				if(secondMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(true, originX, originY, false, y);
				}
				return secondMethodSize;//there is not a revert here since if
			}
		}
		return Integer.MAX_VALUE;
	}
	public static int horizontalXSlice(int originX, int originY, int sliceCounter) {
		if(originY == rows - 1) {
			return Integer.MAX_VALUE;
		}
		int thirdMethodSize = 0;
		int containedTomatoes = 0;
		int containedMushrooms = 0;
		for(int x = originX; x < columns; x++) {
			if(pizzaSlices.get(originY).get(x) != 0 || pizzaSlices.get(originY + 1).get(x) != 0 || thirdMethodSize >= maxSliceSize) {
				revertSlice(false, originX, originY, x);
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
		for(int x = originX; x < columns; x++) {
			if(pizzaSlices.get(originY).get(x) != 0 || fourthMethodSize >= maxSliceSize) {
				revertSlice(false, originX, originY, false, x);
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
				if(fourthMethodSize != minTomatoes + minMushrooms) {//it is only necessary to revert slice size if it is not the smallest possible size
					revertSlice(false, originX, originY, false, x);
				}
				return fourthMethodSize;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	public static void revertSlice(int sliceCounter) {//although this method can be used for every slicing type, it is more efficient to use the more specific methods
		for(int y = 1; y < rows; y++) {
			for(int x = 1; x < columns; x++) {
				if(pizza.get(y).get(x) == sliceCounter) {
					pizza.get(y).set(x, (char) 0);
				}
			}
		}
	}
	public static void revertSlice(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
		if(method) {
			for(int y = lastValue; y >= originY + 1; y--) {
				pizzaSlices.get(y).set(originX, 0);
				pizzaSlices.get(y).set(originX + 1, 0);
			}
		}else {
			for(int x = lastValue; x >= originX + 1; x--) {
				pizzaSlices.get(x).set(originY, 0);
				pizzaSlices.get(x).set(originY + 1, 0);
			}
		}
		
	}
	public static void revertSlice(boolean method, int originX, int originY, boolean twoColumns, int lastValue) {//if method = true its vertical slice otherwise its horizontal
		if(method) {
			for(int y = lastValue; y >= originY + 1; y--) {
				pizzaSlices.get(y).set(originX, 0);
			}
			if(twoColumns) {
				for(int y = lastValue; y >= originY + 1; y--) {
					pizzaSlices.get(y).set(originX + 1, 0);
				}
			}
		}else {
			for(int x = lastValue; x >= originX + 1; x--) {
				pizzaSlices.get(x).set(originY, 0);
			}
			if(twoColumns) {
				for(int x = lastValue; x >= originX + 1; x--) {
					pizzaSlices.get(x).set(originY + 1, 0);
				}
			}
		}
		
	}
	
	public static void inputParser() throws FileNotFoundException, IOException
	{
		Scanner reader = new Scanner(System.in);  
		
		System.out.println("Choose which file to parse (1 is big, 2 is example, 3 is medium, 4 is small):");
		int inputChoice = reader.nextInt(); 
		
		while(inputChoice > 0 && inputChoice > 4)
		{
			System.out.println("Wrong input.  Please try again (1 is big, 2 is example, 3 is medium, 4 is small):");
			inputChoice = reader.nextInt(); 
		}
		
		reader.close();
		
		String typeOfPizza = "";
		String file = "";
		
		if(inputChoice == 1)
		{
			typeOfPizza = "big.";
			file = "big.in";
		}
		else if(inputChoice == 2)
		{
			typeOfPizza = "example.";
			file = "example.in";
		}
		else if(inputChoice == 3)
		{
			typeOfPizza = "medium.";
			file = "medium.in";
		}
		else if(inputChoice == 4)
		{
			typeOfPizza = "small.";
			file = "small.in";
		}
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) 
		{				
		    String line = br.readLine();
		    
		    char rChar = line.charAt(0);
		    char cChar = line.charAt(2);
		    char lChar = line.charAt(4);
		    char hChar = line.charAt(6);
		    
		    
	        int r = Character.getNumericValue(rChar); 
	        int c = Character.getNumericValue(cChar); 
	        int l = Character.getNumericValue(lChar); 
	        int h = Character.getNumericValue(hChar); 
		    
		    rows = r;
		    columns = c;
		    maxSliceSize = h;
		    minTomatoes = l;
		    minMushrooms = l;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
	}
	
	public static void makePizza() {
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
			//printCharArrayList(holder);
			holder.clear();
			//print2dCharArrayList(pizza);
		}
	}
	
	public static void printIntArrayList(ArrayList<Integer> currentList) {
		System.out.print("{");
		for(int i = 0;i < currentList.size(); i++) {
			if(i == 0) {
				System.out.print(currentList.get(i));
			}else {
				System.out.print("," + currentList.get(i));
			}
		}
		System.out.println("}");
	}
	public static void print2dIntArrayList(ArrayList<ArrayList<Integer>> currentList) {
		System.out.println("{");
		for(int i = 0;i < currentList.size(); i++) {
			printIntArrayList(currentList.get(i));
		}
		System.out.println("}");
	}
	public static void printCharArrayList(ArrayList<Character> currentList) {
		System.out.print("{");
		for(int i = 0 ;i < currentList.size(); i++) {
			if(i == 0) {
				System.out.print(currentList.get(i));
			}else {
				System.out.print("," + currentList.get(i));
			}
		}
		System.out.println("}");
	}
	public static void print2dCharArrayList(ArrayList<ArrayList<Character>> currentList) {
		System.out.println("{");
		for(int i=0;i<currentList.size();i++) {
			printCharArrayList(currentList.get(i));
		}
		System.out.println("}");
	}
	public static void intialisePizzaSlices() {
		ArrayList<Integer> holder = new ArrayList<Integer>(Collections.nCopies(columns, 0));
		for(int y = 0; y < rows; y++) {
			pizzaSlices.add(new ArrayList<Integer>(holder));
		}
	}
}

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
		
		System.out.println("minTomatoes: " + minTomatoes);
		System.out.println("minMushrooms: " + minMushrooms);
		System.out.println("maxSliceSize: " + maxSliceSize);
		
		int sliceCounter = 0;
		//loops through all coordinates in the pizza and creates a slice if possible, keep in mind there will still almost certainly be spaces after this has finished
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				if(pizzaSlices.get(y).get(x) == 0) {//if this space is not in any other slice
					if(createSlice(x, y, sliceCounter)) {//if a slice can be created from this origin
						sliceCounter++;
						//print2dIntArrayList(pizzaSlices);
					}
				}
			}
		}
		print2dIntArrayList(pizzaSlices);
	}
	
	public static boolean createSlice(int originX, int originY, int sliceCounter) {//runs through all methods of creating slices then picks slice with smallest size
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
	
	public static int verticalXSlice(int originX, int originY, int sliceCounter, boolean isTest) {
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
	public static int verticalYSlice(int originX, int originY, int sliceCounter, boolean isTest) {
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
	public static int horizontalXSlice(int originX, int originY, int sliceCounter, boolean isTest) {
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
	public static int horizontalYSlice(int originX, int originY, int sliceCounter, boolean isTest) {
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
	
	public static void revertSlice(int sliceCounter) {//although this method can be used for every slicing type, it is more efficient to use the more specific methods
		for(int y = 1; y < rows; y++) {
			for(int x = 1; x < columns; x++) {
				if(pizza.get(y).get(x) == sliceCounter) {
					pizza.get(y).set(x, (char) 0);
				}
			}
		}
	}
	public static void revertSliceRect(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
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
	public static void revertSliceLine(boolean method, int originX, int originY, int lastValue) {//if method = true its vertical slice otherwise its horizontal
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

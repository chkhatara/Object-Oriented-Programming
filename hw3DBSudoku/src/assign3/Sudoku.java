package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0",
	"0 0 0 0 0 0 0 0 0");
	private int numberOfSolutions=0;
	private int [][] answer;
	private int [][] sudokuGrid;
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private long getElapsed;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {

		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		answer=ints;
		sudokuGrid=new int[SIZE][SIZE];
		
	}
	/**
	 * returns coordXY  object  with  x,y coordiantes of the  first spot of the square
	 * where our number is
	 */
	private coordXY findBigSquareXY(int x, int y){
		int coordX=findCount(x);
		int coordY=findCount(y);
		coordXY spot=new coordXY(PART*coordX, PART*coordY);
		return spot;		
	}
	/**
	 * returns  how many PART  is in given number
	 */
	private int findCount(int num){
		int count=0;
		while(true){
			num=num-PART;
			if(num>=0){
				count++;
			}
			if(num<=0)break;			
		}
		return count;		
	}
	/**
	 * checks if number  can be placed in  [x][y]  position
	 */
	private boolean checkNumber(int num,int x,int y){
		coordXY spot=findBigSquareXY(x, y);	
		if(!checkForSquare(num,spot))return false;
		if(!checkForSides(num,x,y))return false;
		return true;
	}
	/**
	 * checks if there is not  our number in small 3x3 square
	 */
	private boolean checkForSquare(int num,coordXY spot){
		for(int x=0;x<PART;x++){
			for(int y=0;y<PART;y++){
				if(answer[spot.x+x][spot.y+y]==num){
					return false;				}
			}
		}
		return true;
	}
	/**
	 * checks if there is number on horizontal or vertical line of the grid
	 * returns true if there is not
	 */
	private boolean checkForSides(int num,int x,int y){
		if(checkForHorizontal(num,x)&&checkForVertical(num,y))return true;
		return false;
	}
	/**
	 * checks if there is this number on horizontal line of the grid
	 * returns true if there is not
	 */
	private boolean checkForHorizontal(int num , int x){
		for(int coordY=0;coordY<SIZE;coordY++){
			if(answer[x][coordY]==num)return false;
		}
		return true;
		
	/**
	 * checks if there is number on vertical line of the grid
	 * returns true if there is not
	 */
	}
	private boolean checkForVertical(int num, int y){
		for(int coordX=0;coordX<SIZE;coordX++){
			if(answer[coordX][y]==num)return false;
		}
		return true;
	}
	/**
	 *  returns  sorted ArrayList  by  the number of assignable values
	 */
	ArrayList<Spot> sortByAssignableNums(){
		ArrayList<Spot> arr= new ArrayList<Spot>();
		for(int x=0;x<SIZE;x++){
			for(int y=0;y<SIZE;y++){
				if(answer[x][y]==0){
					int count=countAssignableNumber(x,y);
					coordXY spot= new coordXY(x,y);
					Spot addSpot=new Spot(spot, count);
					arr=addElement(arr,addSpot,count);
				}
			}
		}
		return arr;
	}
	
	/**
	 * returns number of assignable values  to  x,y positions
	 */
	int countAssignableNumber(int x,int y){
		int count=0;
		for(int i=1;i<=SIZE;i++){
			if(checkNumber(i, x, y))count++;
		}
		return count;
	}
	/**
	 * adds  element to the ArrayList with  ascending order
	 */
	
	ArrayList<Spot>addElement(ArrayList<Spot>arr,Spot spot,int count){
		ArrayList<Spot> tempArr=new ArrayList<Spot>();
		tempArr=arr;
		int checkIfBiggest=0;
		for(int i=0;i<arr.size();i++){
			Spot tempSpot=arr.get(i);
			int num=tempSpot.count;
			if(num>=count){
				tempArr.add(i,spot);
				checkIfBiggest++;
				return tempArr;
			}
		}
		if(checkIfBiggest==0){
			tempArr.add(spot);
		}
		return tempArr;
	}
	
	/**
	 *computes how many non zero  numbers are in grid at start
	 */
	private int countNotFilledSpots(){
		int count=0;
		for(int x=0;x<SIZE;x++){
			for(int y=0;y<SIZE;y++){
				if(answer[x][y]==0)count++;
			}
		}
		return count;
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	
	public int solve() {
		long startTime = System.currentTimeMillis();
		ArrayList<Spot>arr=sortByAssignableNums();	
		int count=countNotFilledSpots();
		recursivelySolveSudoku(arr,0,count);
		System.out.println(toString(sudokuGrid));
		long endTime = System.currentTimeMillis();
		getElapsed=endTime-startTime;
		return numberOfSolutions; 
	}
	/**
	 * solves  Sudoku  with backtracking algorithm
	 */
	private void recursivelySolveSudoku(ArrayList<Spot> arr,int startCount,int endCount){
		if(numberOfSolutions>=MAX_SOLUTIONS){
			return;
		}
		if(startCount==endCount){
			if(numberOfSolutions==0)
				saveSolution();		
			numberOfSolutions++;
			return;		
		}
		Spot spot=arr.get(startCount);
		for(int i=1;i<=SIZE;i++){
			if(checkNumber(i, spot.coord.x, spot.coord.y)){
				answer[spot.coord.x][spot.coord.y]=i;
				recursivelySolveSudoku(arr, startCount+1, endCount);
				answer[spot.coord.x][spot.coord.y]=0;			
			}
		}		
	}
	
	public String getSolutionText() {
		return toString(sudokuGrid); 
	}
	
	public long getElapsed() {
		return getElapsed; 
	}
	private void saveSolution() {
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(answer[i], 0, sudokuGrid[i], 0, SIZE);
    }

	 private String toString(int[][] grid) {
	        String result = "";
	        if (grid != null)
	            for (int i = 0; i < SIZE; i++) {
	                for (int j = 0; j < SIZE; j++)
	                    result += grid[i][j] + " ";
	                result += "\n";
	            }
	        return result;
	    }
}

// Board.java

package tetris;
import java.util.ArrayList;
import java.util.Arrays;
/**
/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/

public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private boolean[][] backUpGrid;
	private int [] xWidths;
	private int [] xHeights;
	private int [] backUpWidths;
	private int [] backUpHeights;
	private int maxHeight;
	private int backUpMaxHeight;
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		backUpGrid = new boolean[width][height];
		xWidths=new int [height];
		xHeights=new int [width];
		backUpWidths=new int [height];
		backUpHeights=new int [width];
		maxHeight=0;
		backUpMaxHeight=0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	
		return maxHeight;
	}
	
	private boolean checkForXWidths(){
		for(int col=0;col<height;col++){
			int result=0;
			for(int row=0;row<width;row++){
				if(grid[row][col]){
					result++;
				}
			}
			if(result!=xWidths[col]) return false;
		}
		return true;
	}
	private boolean checkForXHeights(){
		for(int row=0;row<width;row++){
			int result=0;
			for(int col=0;col<height;col++){
				if(grid[row][col]){
					result++;
				}
			}
			if(result!=xHeights[row]) return false;
		}
		return true;
	}
	private boolean checkForXMaxHeight(){
		if(calculateMaxHeight()!=maxHeight) return false;
		return true;
	}
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			if(checkForXHeights()==false){
				throw new RuntimeException("Heights Is Wrong");
			}
			if(checkForXWidths()==false){
				throw new RuntimeException("Widths Is Wrong");
			}
			if(checkForXMaxHeight()==false){
				throw new RuntimeException("Max Height Is Wrong");
			}
			
			
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int result = 0;
		for(int i = 0; i < piece.getSkirt().length; i++){
			if(xHeights[x + i] - piece.getSkirt()[i] > result){
				result = xHeights[x + i] - piece.getSkirt()[i];
			}
		}
		return result;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return xHeights[x]; 
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return xWidths[y];
		 
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(grid[x][y]){
			return true;
		}
		if(x>=width||y>=height||x<0||y<0){
			return true;
		}
		return false; 
	}
	
	private int calculateMaxHeight(){
		int result=0;
		for(int i=0;i<xHeights.length;i++){
			if(xHeights[i]>result){
				result=xHeights[i];
			}
		}
		return result; 
	}
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		if (!committed) throw new RuntimeException("place commit problem");
        committed=false;
        backUpData();
		int result = PLACE_OK;
        for(TPoint point: piece.getBody()){
            if(checkForBoundry(point, x, y)==false){
                return  PLACE_OUT_BOUNDS;
            }
            if(checkForOverlap(point, x, y)){
                return PLACE_BAD;
            }
            grid[point.x + x][point.y + y] = true;
            xHeights[point.x+x]= point.y+y+1;
            xWidths[point.y+y]++;
            maxHeight=calculateMaxHeight();
            if(xWidths[point.y+y]==width){
                  result = PLACE_ROW_FILLED; 
            }
        }
        return result;
     }
	
	/**
	 * checks if some point of body is out of body after placing 
	 * and returns  false if so
	 */
	private boolean checkForBoundry(TPoint point,int x,int y){
			if(point.x+x>=width||point.x+x<0||point.y
					+y>=height||point.y+y<0){
				return false;									
		}
		return true;
	}
	/**
	 * @return false if some part of piece overlaps existing placed piece in grid
	 */
	private boolean checkForOverlap(TPoint point,int x,int y){
			if(grid[point.x+x][point.y+y]==true){
				return true;	
			}
		return false;
	}
	
	/**
	 * returns  true if some row is filled in grid. false otherwise
	 */
	private ArrayList<Integer> checkForFirstFullRow(){
		ArrayList<Integer> arr=new ArrayList<Integer>();
		for(int i=0;i<height;i++){
			if(xWidths[i]==width){
				arr.add(i);
			}
		}
		return arr;
	}
		
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		committed=false;
		backUpData();
		int rowsCleared = 0;
		ArrayList<Integer> arr=checkForFirstFullRow();
		for(int i=0;i<arr.size();i++){
			int row=arr.get(i)-rowsCleared;
			for(int k=row;k<height;k++){
				for(int q=0;q<width;q++){	
					if(k+1<height){
						grid[q][k]=grid[q][k+1];
						grid[q][k+1]=false;
					}
				}
			}
			rowsCleared++;
		}
		calculateGridHeight();
		calculateGridWidth();
		maxHeight=calculateMaxHeight();
		return rowsCleared;
	}

	/**
	 * updates  array of xheights after deleting  a row
	 */
	private void calculateGridHeight(){
		for(int row=0;row<width;row++){
			int xCol=0;
			for(int col=0;col<height;col++){
				if(grid[row][col]==true){
					xCol++;
				}
			}
			xWidths[row]=xCol;
		}
	}
	private void backUpData() {
        backUpWidths = Arrays.copyOf(xWidths, xWidths.length);
        backUpHeights = Arrays.copyOf(xHeights, xHeights.length);
        for (int i=0; i<width; i++) {
            backUpGrid[i] = Arrays.copyOf(grid[i], height);
        }
		backUpMaxHeight = maxHeight;
	}
	/**
	 * updates  array of xWidths after deleting  a row
	 */
	private void calculateGridWidth(){
		for(int col=0;col<height;col++){
			int xRow=0;
			for(int row=0;row<width;row++){
				if(grid[row][col]==true){
					xRow++;
				}
			}
			xWidths[col]=xRow;
		}
	}
	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed==false){
			committed=true;
			boolean[][] tempForGrid;			
			tempForGrid = grid;
			grid = backUpGrid;
			backUpGrid = tempForGrid;
			int[] tempForWidths=Arrays.copyOf(xWidths, height);
			xWidths=Arrays.copyOf(backUpWidths,height);
			backUpWidths=Arrays.copyOf(tempForWidths, height);
			int[] tempForHeight=Arrays.copyOf(xHeights,width);
			xHeights=Arrays.copyOf(backUpHeights,width);
			backUpHeights=Arrays.copyOf(tempForHeight, width);
			maxHeight=backUpMaxHeight;		
		}	
	}
	
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}
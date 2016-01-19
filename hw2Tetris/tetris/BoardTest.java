package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b, board;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		board = new Board(4, 5);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	

	
	@Test
	public void placementTest(){
		board.commit();
		int result = board.place(new Piece(Piece.SQUARE_STR), 0, 0);
		assertEquals(Board.PLACE_OK, result);
		board.commit();

		result = board.place(new Piece(Piece.SQUARE_STR), 2, 0);
		assertEquals(board.PLACE_ROW_FILLED, result);
		
		board.undo();
		board.commit();
		
		result = board.place(new Piece(Piece.L1_STR), 2, 0);
		assertEquals(board.PLACE_ROW_FILLED, result);
		board.commit();
		
		result = board.place(new Piece(Piece.STICK_STR), 2, -1);
		assertEquals(board.PLACE_OUT_BOUNDS, result);
		
		result = board.place(new Piece(Piece.STICK_STR), 0, 0);
		assertEquals(board.PLACE_BAD, result);
		
		result = board.place(new Piece(Piece.STICK_STR), 0, 5);
		assertEquals(board.PLACE_OUT_BOUNDS, result);
		
		result = board.place(new Piece(Piece.STICK_STR), 5, 0);
		assertEquals(board.PLACE_OUT_BOUNDS, result);
		}
		
		
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	@Test
	public void clearRowsTest(){
		b = new Board(3,4);
		b.commit();
		b.place(new Piece(Piece.SQUARE_STR), 0, 0);
		b.commit();
		b.place(new Piece(Piece.STICK_STR), 2, 0);
		b.clearRows();
		assertEquals(false, b.getGrid(0, 0));
		assertEquals(false, b.getGrid(1, 0));
		assertEquals(true, b.getGrid(2, 0));
		assertEquals(true, b.getGrid(2, 1));
	}
	
	@Test
	public void undoTest(){
		Board t = new Board(4,5);
		
		t.place(new Piece(Piece.SQUARE_STR), 0, 0);
		assertEquals(true, t.getGrid(0, 0));
		assertEquals(true, t.getGrid(1, 0));
		
		t.undo();
		assertEquals(false, t.getGrid(0, 0));
		assertEquals(false, t.getGrid(1, 0));
		
		int res = t.place(new Piece(Piece.STICK_STR).computeNextRotation(), 0, 0);
		assertEquals(t.PLACE_ROW_FILLED,res);
		
		t.clearRows();
		assertEquals(false, t.getGrid(0,0));
		assertEquals(false, t.getGrid(1,0));
		assertEquals(false, t.getGrid(2,0));
		assertEquals(false, t.getGrid(3,0));
		
		t.commit();
		t.place(pyr1,0,0);
		t.commit();
		t.place(sRotated,2,0);
		assertEquals(true, t.getGrid(3, 0));
		assertEquals(true, t.getGrid(3, 1));
		t.undo();
		assertEquals(false, t.getGrid(3, 0));
		assertEquals(false, t.getGrid(3, 1));
		
	}
	
	@Test 
	public void maxHeightTest(){
		Board b1 = new Board(5,6);
		b1.commit();
		b1.place(pyr1, 0, 0);
		assertEquals(2, b1.getMaxHeight());
		b1.commit();
		b1.place(pyr2, 2, 0);
		assertEquals(3, b1.getMaxHeight());
		b1.commit();
		b1.place(new Piece(Piece.STICK_STR),4,0);
		assertEquals(4, b1.getMaxHeight());
		
	}
	
}

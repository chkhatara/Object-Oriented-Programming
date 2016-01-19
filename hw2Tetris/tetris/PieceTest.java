package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece stick, stickRotated;
	private Piece s, sRotated, s2, s2Rotated;
	private Piece L11, L12, L13, L14;
	private Piece L21, L22, L23, L24;
	private Piece square;
	

	@Before
	public void setUp() throws Exception {
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		s2 = new Piece(Piece.S2_STR);
		s2Rotated = s2.computeNextRotation();
		
		stick = new Piece(Piece.STICK_STR);
		stickRotated= stick.computeNextRotation();
		
		square = new Piece(Piece.SQUARE_STR);
		square = square.computeNextRotation();
		
		L11 = new Piece(Piece.L1_STR);
		L12 = L11.computeNextRotation();
		L13 = L12.computeNextRotation();
		L14 = L13.computeNextRotation();
		
		L21 = new Piece(Piece.L2_STR);
		L22 = L21.computeNextRotation();
		L23 = L22.computeNextRotation();
		L24 = L23.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr3.getHeight());
		
		assertEquals(2, pyr4.getWidth());
		assertEquals(3, pyr4.getHeight());
		
		
		// Now try with some other piece, made a different way
		Piece stick = new Piece(Piece.STICK_STR);
		assertEquals(1, stick.getWidth());
		assertEquals(4, stick.getHeight());
		assertEquals(1, stickRotated.getHeight());
		assertEquals(4, stickRotated.getWidth());
		
		// Size of S
		assertEquals(2, s.getHeight());
		assertEquals(3, s.getWidth());
		assertEquals(3, sRotated.getHeight());
		assertEquals(2, sRotated.getWidth());
		
		// Size of S2
		assertEquals(2, s2.getHeight());
		assertEquals(3, s2.getWidth());
		assertEquals(3, s2Rotated.getHeight());
		assertEquals(2, s2Rotated.getWidth());
		
		//Size of Square
		assertEquals(2, square.getWidth());
		assertEquals(2, square.getHeight());
		
		//Size of L1
		assertEquals(3, L11.getHeight());
		assertEquals(2, L11.getWidth());
		assertEquals(3, L12.getWidth());
		assertEquals(2, L12.getHeight());
		assertEquals(2, L13.getWidth());
		assertEquals(3, L13.getHeight());
		assertEquals(3, L14.getWidth());
		assertEquals(2, L14.getHeight());
		
		//Size of L2
		assertEquals(3, L21.getHeight());
		assertEquals(2, L21.getWidth());
		assertEquals(3, L22.getWidth());
		assertEquals(2, L22.getHeight());
		assertEquals(2, L23.getWidth());
		assertEquals(3, L23.getHeight());
		assertEquals(3, L24.getWidth());
		assertEquals(2, L24.getHeight());
		
	}
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0}, stick.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stickRotated.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0}, L11.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 1, 0}, L22.getSkirt()));

	}
	@Test
	public void testEqual(){
		String pyrPoints = "0 0  1 0  1 1  2 0";
		Piece p = new Piece(pyrPoints);
		assertTrue(pyr1.equals(p));
		
		String stickPoints = "0 0	0 1	 0 2  0 3";
		Piece s = new Piece(stickPoints);
		assertTrue(stick.equals(s));
	}
	
	@Test
	public void testRotations(){
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[Piece.STICK].equals(stick));
		assertTrue(pieces[Piece.STICK].fastRotation().equals(stickRotated));
		
		assertTrue(pieces[Piece.L1].fastRotation().equals(L12));
		assertTrue(pieces[Piece.L1].fastRotation().fastRotation().equals(L13));
		
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, pieces[Piece.PYRAMID].getSkirt()));
		//assertTrue(Arrays.equals(new int[]{1, 0}, pieces[Piece.PYRAMID].fastRotation().getSkirt()));
	}
}
	

package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {

	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt");
		board.initialize(6);
	}

	//Adjacency Tests

	//Orange
	@Test
	public void testSpaceOnlyWalkwaysAdj() {
		LinkedList<BoardCell> testList = board.getAdjList(16, 15);
		System.out.println(testList);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 14)));
		assertTrue(testList.contains(board.getCellAt(16, 16)));
		assertTrue(testList.contains(board.getCellAt(15, 15)));
		assertTrue(testList.contains(board.getCellAt(17, 15)));
	}

	//Purple
	@Test
	public void testEdgeLocations(){

		LinkedList<BoardCell> testList = board.getAdjList(0, 8);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(1, 8)));
		assertTrue(testList.contains(board.getCellAt(0, 7)));

		testList = board.getAdjList(21, 14);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(20, 14)));

		testList = board.getAdjList(7, 22);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 22)));
		assertTrue(testList.contains(board.getCellAt(7, 21)));

		testList = board.getAdjList(14, 0);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(13, 0)));
		assertTrue(testList.contains(board.getCellAt(15, 0)));
		assertTrue(testList.contains(board.getCellAt(14, 1)));

	}
	//Green
	@Test
	public void testRoomCellNotDoorWay(){

		LinkedList<BoardCell> testList = board.getAdjList(17, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 7)));
		assertTrue(testList.contains(board.getCellAt(18, 7)));
		assertTrue(testList.contains(board.getCellAt(17, 8)));


		testList = board.getAdjList(10, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(11, 7)));
		assertTrue(testList.contains(board.getCellAt(9, 7)));
		assertTrue(testList.contains(board.getCellAt(10, 8)));


	}
	//Brown
	@Test
	public void testAdjDoorWithDirection(){

		LinkedList<BoardCell> testList = board.getAdjList(11, 14);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(12, 14)));
		assertTrue(testList.contains(board.getCellAt(10, 14)));
		assertTrue(testList.contains(board.getCellAt(11, 15)));

		testList = board.getAdjList(17, 11);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(17, 10)));
		assertTrue(testList.contains(board.getCellAt(17, 12)));
		assertTrue(testList.contains(board.getCellAt(16, 11)));
		assertTrue(testList.contains(board.getCellAt(18, 11)));

		testList = board.getAdjList(13, 4);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(13, 5)));
		assertTrue(testList.contains(board.getCellAt(13, 3)));
		assertTrue(testList.contains(board.getCellAt(12, 4)));
		assertTrue(testList.contains(board.getCellAt(14, 4)));

		testList = board.getAdjList(4, 8);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(4, 9)));
		assertTrue(testList.contains(board.getCellAt(4, 7)));
		assertTrue(testList.contains(board.getCellAt(5, 8)));
		assertTrue(testList.contains(board.getCellAt(3, 8)));

	}
	//Pink
	@Test 
	public void testDoorways(){

		LinkedList<BoardCell> testList = board.getAdjList(6, 17);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(7, 17)));

		testList = board.getAdjList(16, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(15, 3)));

	}

	//Target Tests
	//
	@Test
	public void testWalkWaysVariousSteps(){
		//Test with 1 step
		board.calcTargets(14, 6, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 7)));
		assertTrue(targets.contains(board.getCellAt(14, 5)));
		assertTrue(targets.contains(board.getCellAt(13, 6)));
		assertTrue(targets.contains(board.getCellAt(15, 6)));

		//Test with 2 step
		board.calcTargets(5, 12, 2);
		targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 13)));
		assertTrue(targets.contains(board.getCellAt(5, 14)));
		assertTrue(targets.contains(board.getCellAt(6, 13)));
		assertTrue(targets.contains(board.getCellAt(6, 11)));
		assertTrue(targets.contains(board.getCellAt(5, 10)));

		//Test with 3 step
		board.calcTargets(0, 14, 3);
		targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(3, 14)));

		//Test with 4 step
		board.calcTargets(21, 7, 4);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(19, 7)));
		assertTrue(targets.contains(board.getCellAt(17, 7)));
		assertTrue(targets.contains(board.getCellAt(18, 8)));
		assertTrue(targets.contains(board.getCellAt(20, 8)));


	}

	@Test
	public void testTargetsWhereRoomEntryPossible(){

		board.calcTargets(6, 2, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 2)));
		assertTrue(targets.contains(board.getCellAt(5, 3)));
		assertTrue(targets.contains(board.getCellAt(6, 0)));
		assertTrue(targets.contains(board.getCellAt(7, 1)));
		assertTrue(targets.contains(board.getCellAt(6, 4)));
		assertTrue(targets.contains(board.getCellAt(5, 1)));

		board.calcTargets(16, 18, 1);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 19)));
		assertTrue(targets.contains(board.getCellAt(16, 17)));
		assertTrue(targets.contains(board.getCellAt(15, 18)));
		assertTrue(targets.contains(board.getCellAt(17, 18)));

		board.calcTargets(16, 18, 2);
		targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 16)));
		assertTrue(targets.contains(board.getCellAt(17, 17)));
		assertTrue(targets.contains(board.getCellAt(18, 18)));
		assertTrue(targets.contains(board.getCellAt(17, 19)));
		assertTrue(targets.contains(board.getCellAt(16, 20)));
		assertTrue(targets.contains(board.getCellAt(15, 19)));
		assertTrue(targets.contains(board.getCellAt(15, 18)));
	}

	@Test 
	public void testTargetInDoorWay(){

		board.calcTargets(4, 3, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 2)));
		assertTrue(targets.contains(board.getCellAt(6, 4)));
		assertTrue(targets.contains(board.getCellAt(5, 1)));
		assertTrue(targets.contains(board.getCellAt(5, 5)));
		assertTrue(targets.contains(board.getCellAt(6, 2)));

		board.calcTargets(18, 18, 3);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(15, 18)));
		assertTrue(targets.contains(board.getCellAt(16, 17)));
		assertTrue(targets.contains(board.getCellAt(17, 16)));
		assertTrue(targets.contains(board.getCellAt(17, 20)));
		assertTrue(targets.contains(board.getCellAt(16, 19)));
		assertTrue(targets.contains(board.getCellAt(18, 17)));



	}

}

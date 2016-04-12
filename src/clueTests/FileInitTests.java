package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class FileInitTests {

	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 22;
	public static final int NUM_COLUMNS = 23;

	private static Board board;

	@BeforeClass
	public static void setUp() {
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt");
		board.initialize(6);
	}

	@Test
	public void testRooms() {

		Map<Character, String> rooms = Board.getRooms();
		assertEquals(NUM_ROOMS, rooms.size());
		assertEquals("Bathroom", rooms.get('B'));
		assertEquals("Poolhouse", rooms.get('P'));
		assertEquals("Lounge", rooms.get('L'));
		assertEquals("Den", rooms.get('D'));
		assertEquals("Showroom", rooms.get('S'));
		assertEquals("Guest Room", rooms.get('G'));
		assertEquals("Master Bedroom", rooms.get('M'));
		assertEquals("Kitchen", rooms.get('K'));
		assertEquals("Walkway", rooms.get('W'));
		assertEquals("Closet", rooms.get('X'));
		assertEquals("Joe's Room", rooms.get('J'));
	}

	@Test
	public void testBoardDims() {
		assertEquals(NUM_COLUMNS, board.getNumColumns());
		assertEquals(NUM_ROWS, board.getNumRows());
	}

	@Test
	public void testRoomInitials() {

		assertEquals('B', board.getCellAt(0, 0).getRoomLetter());
		assertEquals('D', board.getCellAt(0, 9).getRoomLetter());
		assertEquals('K', board.getCellAt(0, 15).getRoomLetter());
		assertEquals('P', board.getCellAt(7, 2).getRoomLetter());
		assertEquals('X', board.getCellAt(7, 9).getRoomLetter());
		assertEquals('M', board.getCellAt(8, 17).getRoomLetter());
		assertEquals('L', board.getCellAt(21, 0).getRoomLetter());
		assertEquals('S', board.getCellAt(18, 13).getRoomLetter());
		assertEquals('G', board.getCellAt(21, 22).getRoomLetter());
		assertEquals('J', board.getCellAt(9, 20).getRoomLetter());
	}

	@Test
	public void testFourDoorDirections() {
		BoardCell room = board.getCellAt(7, 6);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(15, 18);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(12, 15);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(16, 3);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		room = board.getCellAt(21, 12);
		assertFalse(room.isDoorway());
		BoardCell cell = board.getCellAt(16, 14);
		assertFalse(cell.isDoorway());

	}

	@Test(expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("ClueLayoutBadColumns.csv", "Clue_LegendStudent.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}

	@Test(expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("ClueLayoutBadRoom.csv", "Clue_LegendStudent.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}

	@Test(expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("Clue_LayoutStudent.csv", "ClueLegendBadFormat.txt");
		board.loadRoomConfig();
	}

}

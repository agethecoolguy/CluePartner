package clueTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.*;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.DoorDirection;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

import java.awt.Color;

public class GameActionTests {
    private static Board board;
    public static Player tester;
    public static Card mechE;
    public static Card quad;
    public static Card bathroom;
    public static ArrayList<Card> testCards;
    public static Player npc1;
    public static Player npc2;
    public static Player npc3;
    public static Player npc4;
    public static Player npc5;
    public static Player human;
    
    @BeforeClass
    public static void initialSetUp() {
    	tester = new Player("Unoriginal MechE", Color.red, 0, 7);
    	mechE = new Card("Unoriginal MechE", CardType.PERSON);
    	quad = new Card("QuadCopter", CardType.WEAPON);
    	bathroom = new Card("Bathroom", CardType.ROOM);
    	
    	testCards = new ArrayList<Card>();
    	testCards.add(mechE);
    	testCards.add(new Card("Stinky Physicist", CardType.PERSON));
    	testCards.add(quad);
    	testCards.add(new Card("Loncapa Red Box", CardType.WEAPON));
    	testCards.add(bathroom);
    	testCards.add(new Card("Poolhouse", CardType.ROOM));
    	
    	tester.setMyCards(testCards);
    }
    
    @Before
    public void setUp() {
        board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent.txt", "ClueWeaponsStudent.txt");
        board.initialize();
        board.dealCards();
    }
    
    //-------------------------------------TEST ACCUSATIONS---------------------------------------------------
    
    @Test
    public void testCorrectAccusation() { // tests a solution with all correct components
    	Solution accusation = board.getSolution();
    	assertTrue(board.checkAccusation(accusation));
    }
    
    @Test
    public void testIncorrectPerson() { // tests a solution with an incorrect person component
    	Solution accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unfunny Name";
    	assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testIncorrectWeapon() { // tests a solution with an incorrect weapon component
    	Solution accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unoriginal Weapon";
    	assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testIncorrectRoom() { // tests a solution with an incorrect room component
    	Solution accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.room = "Generic Room";
    	assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testTwoWrong() { // Test case where two aspects of the accusation are false
    	Solution accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unfunny Name";
    	accusation.person = "Unoriginal Weapon";
    	assertFalse(board.checkAccusation(accusation));
    	
    	accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unfunny Name";
    	accusation.room = "Generic Room";
    	assertFalse(board.checkAccusation(accusation));
    	
    	accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unoriginal Weapon";
    	accusation.room = "Generic Room";
    	assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testAllWrong() { // tests a solution with all wrong components
    	Solution accusation = new Solution(board.getSolution().person, board.getSolution().weapon, board.getSolution().room);
    	accusation.person = "Unfunny Name";
    	accusation.person = "Unoriginal Weapon";
    	accusation.room = "Generic Room";
    	assertFalse(board.checkAccusation(accusation));
    }
    
    //---------------------------------TEST SUGGESTIONS: ONE PERSON-----------------------------------------
    
    @Test
    public void testSuggestionOneCorrect() { // test case where only one part of the suggestion is correct
    	Solution suggestion = new Solution("Unoriginal MechE", "wrongWeapon", "wrongRoom");
    	assertEquals(tester.disproveSuggestion(suggestion), mechE);

    	suggestion = new Solution("wrongPerson", "QuadCopter", "wrongRoom");
    	assertEquals(tester.disproveSuggestion(suggestion), quad);
    	
    	suggestion = new Solution("wrongPerson", "wrongWeapon", "Bathroom");
    	assertEquals(tester.disproveSuggestion(suggestion), bathroom);    	
    }
    
    @Test
    public void testSuggestionMultipleCorrect() { // test case where only two parts of the suggestion are correct
    	int mechEReturned = 0;
    	int quadReturned = 0;
    	int bathroomReturned = 0;
    	
		for (int i = 0; i < 1000; i++) {
			Solution suggestion = new Solution("Unoriginal MechE", "QuadCopter", "wrongRoom");
			Card result = tester.disproveSuggestion(suggestion);
			if (result.equals(mechE)) {
				mechEReturned++;
			} else if (result.equals(quad)) {
				quadReturned++;
			} else {
				fail("Unexpected card returned");
			}

			suggestion = new Solution("wrongPerson", "QuadCopter", "Bathroom");
			result = tester.disproveSuggestion(suggestion);
			if (result.equals(quad)) {
				quadReturned++;
			} else if (result.equals(bathroom)) {
				bathroomReturned++;
			} else {
				fail("Unexpected card returned");
			}

			suggestion = new Solution("Unoriginal MechE", "wrongWeapon", "Bathroom");
			result = tester.disproveSuggestion(suggestion);
			if (result.equals(mechE)) {
				mechEReturned++;
			} else if (result.equals(bathroom)) {
				bathroomReturned++;
			} else {
				fail("Unexpected card returned");
			}
		}
		
		assertTrue(mechEReturned > 0);
		assertTrue(quadReturned > 0);
		assertTrue(bathroomReturned > 0);
    }
    
    //----------------------------------TEST SUGGESTIONS: MULTIPLE PLAYERS--------------------------------------------

    public static void createPlayers() { // this method used only for multi-player tests

        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<Card> cards = new ArrayList<Card>();

        npc1 = new ComputerPlayer("npc1", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("John", CardType.PERSON));
        npc1.setMyCards(cards);

        npc2 = new ComputerPlayer("npc2", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("Joe", CardType.PERSON));
        npc2.setMyCards(cards);

        npc3 = new ComputerPlayer("npc3", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("Jack", CardType.PERSON));
        npc3.setMyCards(cards);

        npc4 = new ComputerPlayer("npc4", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("Jane", CardType.PERSON));
        npc4.setMyCards(cards);

        npc5 = new ComputerPlayer("npc5", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("Jill", CardType.PERSON));
        npc5.setMyCards(cards);

        human = new HumanPlayer("human", Color.red, 0, 0);
        cards = new ArrayList<Card>();
        cards.add(new Card("Knife", CardType.WEAPON));
        human.setMyCards(cards);

        players.add(npc1);
        players.add(npc2);
        players.add(npc3);
        players.add(npc4);
        players.add(npc5);
        players.add(human);
        
        board.setPlayers(players);
    }
    
    @Test
    public void testSuggestionAllPlayersUndisprovable() { // this tests a case where no players can disprove a suggestion
    	createPlayers();
    	Solution suggestion = new Solution("wrongPerson", "wrongWeapon", "wrongRoom");
    	assertEquals(null, board.handleSuggestion(suggestion, npc1, new BoardCell(DoorDirection.NONE, 'W')));
    }
    
    @Test
    public void testAllPlayersHumanDisproving() { // tests case where only human can disprove
    	createPlayers();
    	Solution suggestion = new Solution("wrongPerson", "Knife", "wrongRoom");
    	assertEquals(new Card("Knife", CardType.WEAPON), board.handleSuggestion(suggestion, npc1, new BoardCell(DoorDirection.NONE, 'W')));
    }
    
    @Test
    public void testDecoySuggestionHuman() { // tests case where the person suggesting is the only person able to disprove, such that null is returned
    	createPlayers();
    	Solution suggestion = new Solution("wrongPerson", "Knife", "wrongRoom");
    	assertEquals(null, board.handleSuggestion(suggestion, human, new BoardCell(DoorDirection.NONE, 'W'))); // human has Knife, so null should be returned
    }
    
    @Test
    public void testDecoySuggestionNPC1() { // tests case where the person suggesting is the only person able to disprove, such that null is returned
    	createPlayers();
    	Solution suggestion = new Solution("John", "wrongWeapon", "wrongRoom");
    	assertEquals(null, board.handleSuggestion(suggestion, npc1, new BoardCell(DoorDirection.NONE, 'W'))); //npc1 has John, so null should be returned
    }
    
    @Test
    public void testOrderOfDisproving() { // this tests the case where multiple people can disprove, ensuring that the first person disproves
    	createPlayers();
    	Solution suggestion = new Solution("Jack", "Knife", "wrongRoom"); // both npc3 and human have cards. npc3 is first in line so Jack should be returned
    	assertEquals(new Card("Jack", CardType.PERSON), board.handleSuggestion(suggestion, npc1, new BoardCell(DoorDirection.NONE, 'W')));
    }
    
    @Test
    public void testAllDisproving() { // this tests ensures that all people are being queried for disproving
    	createPlayers();
    	Solution suggestion = new Solution("Jack", "wrongWeapon", "wrongRoom"); // npc3 has Jack, and is farthest from npc4, so all players should be queried first before returning Jack
    	assertEquals(new Card("Jack", CardType.PERSON), board.handleSuggestion(suggestion, npc4, new BoardCell(DoorDirection.NONE, 'W')));
    }
    
    //---------------------------- TARGET SELECTION TESTS ------------------------------------
    @Test
    public void testEnteringUnvisitedRoom() { // tests visiting a room we did not come from previously
        int initialRow = 14;
        int initialColumn = 2;
        int steps = 2;
        int expectedRow = 16; // TODO how are we defining a player being "in" a room?
        int expectedColumn = 2;
        board.calcTargets(initialRow, initialColumn, steps);
        Set<BoardCell> targets = board.getTargets();
        for (int i = 0; i < 100; i++) {
            ComputerPlayer npc = new ComputerPlayer("npc", Color.red, initialRow, initialColumn);
            npc.setRoomLastVisited('K');
            npc.move(npc.pickLocation(targets));
            assertEquals(expectedRow, npc.getRow()); // supposed to be in the lounge
            assertEquals(expectedColumn, npc.getColumn());
        }
    }
    
    @Test
    public void testEnteringVisitedRoom() { // testing moving within the range of a room we were just in
        int initialRow = 15;
        int initialColumn = 2;
        int steps = 1;
        int possibility_room = 0;
        int possibility_15_1 = 0;
        int possibility_15_3 = 0;
        int possibility_14_2 = 0;
        board.calcTargets(initialRow, initialColumn, steps);
        Set<BoardCell> targets = board.getTargets();
        for (int i = 0; i < 100; i++) {
            ComputerPlayer npc = new ComputerPlayer("npc", Color.red, initialRow, initialColumn);
            npc.setRoomLastVisited('L'); // coming from lounge, and possibly going to lounge
            npc.move(npc.pickLocation(targets));
            if (npc.getRow() == 16 && npc.getColumn() == 2) {
                possibility_room++;
            } else if (npc.getRow() == 15 && npc.getColumn() == 1){
                possibility_15_1++;
            } else if (npc.getRow() == 15 && npc.getColumn() == 3){
                possibility_15_3++;
            } else if (npc.getRow() == 14 && npc.getColumn() == 2){
                possibility_14_2++;
            } else {
                fail("Incorrect square reached");
            }
        }
        assertTrue(possibility_room != 0);
        assertTrue(possibility_15_1 != 0);
        assertTrue(possibility_15_3 != 0);
        assertTrue(possibility_14_2 != 0);
    }
    
    @Test
    public void testRandomSelectionWithoutRoom() { // testing moving when no rooms are valid targets
        int initialRow = 17;
        int initialColumn = 9;
        int steps = 1;
        int possibility_18_9 = 0;
        int possibility_17_8 = 0;
        int possibility_17_10 = 0;
        int possibility_16_9 = 0;
        board.calcTargets(initialRow, initialColumn, steps);
        Set<BoardCell> targets = board.getTargets();
        for (int i = 0; i < 100; i++) {
            ComputerPlayer npc = new ComputerPlayer("npc", Color.red, initialRow, initialColumn);
            npc.move(npc.pickLocation(targets));
            if (npc.getRow() == 18 && npc.getColumn() == 9) {
                possibility_18_9++;
            } else if (npc.getRow() == 17 && npc.getColumn() == 8){
                possibility_17_8++;
            } else if (npc.getRow() == 17 && npc.getColumn() == 10){
                possibility_17_10++;
            } else if (npc.getRow() == 16 && npc.getColumn() == 9){
                possibility_16_9++;
            } else {
                fail("Incorrect square reached");
            }
        }
        assertTrue(possibility_18_9 != 0);
        assertTrue(possibility_17_8 != 0);
        assertTrue(possibility_17_10 != 0);
        assertTrue(possibility_16_9 != 0);
    }
}
package clueTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.junit.*;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

import java.awt.Color;
import java.awt.Point;

public class GameSetupTests {
    private static Board board;

    @Before
    public void setUp() {
        board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent.txt", "ClueWeaponsStudent.txt");
        board.initialize();
        //deal cards is not included in this section because certain tests are dependent on its absence
    }

	//---------------------PLAYER TESTS---------------------------------
	
    @Test
    public void testCorrectPlayerAttributes() {
        ArrayList<Player> gamePlayers = board.getPlayers();
        assertEquals(6, gamePlayers.size()); // Assert that the number of players in gamePlayers does not exceed 6

        // Assert that names are read in correctly
        assertEquals(gamePlayers.get(0).getPlayerName(), "Unoriginal MechE");
        assertEquals(gamePlayers.get(1).getPlayerName(), "Stinky Physicist");
        assertEquals(gamePlayers.get(2).getPlayerName(), "Sleep Deprived ChemE");
        assertEquals(gamePlayers.get(3).getPlayerName(), "Lightly Charred EE");
        assertEquals(gamePlayers.get(4).getPlayerName(), "CompSci Neckbeard");
        assertEquals(gamePlayers.get(5).getPlayerName(), "Heartless Petro");

        // Assert that correct colors are assigned
        assertEquals(gamePlayers.get(0).getColor(), Color.red);
        assertEquals(gamePlayers.get(1).getColor(), Color.white);
        assertEquals(gamePlayers.get(2).getColor(), Color.green);
        assertEquals(gamePlayers.get(3).getColor(), Color.cyan);
        assertEquals(gamePlayers.get(4).getColor(), Color.blue);
        assertEquals(gamePlayers.get(5).getColor(), Color.black);

        // Assert that initial location is correct
        // This section uses the Point class rather than individual ints for row/column for the sake of simplicity
        assertEquals(gamePlayers.get(0).getLocation(), new Point(0, 7));
        assertEquals(gamePlayers.get(1).getLocation(), new Point(0, 14));
        assertEquals(gamePlayers.get(2).getLocation(), new Point(7, 22));
        assertEquals(gamePlayers.get(3).getLocation(), new Point(16, 22));
        assertEquals(gamePlayers.get(4).getLocation(), new Point(21, 14));
        assertEquals(gamePlayers.get(5).getLocation(), new Point(21, 8));
    }
	
	
	@Test(expected = BadConfigFormatException.class)
	public void testTooFewPlayers() throws BadConfigFormatException { // Exception test for a file with an insufficient number of players
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_TooFew.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testTooManyPlayers() throws BadConfigFormatException { // Exception test for a file with an excessive number of players 
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_TooMany.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testPlayersIncorrectFormat() throws BadConfigFormatException { // // Exception test for a file with bad format
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_BadFormat.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testPlayersMissingFile() throws BadConfigFormatException { // Exception test for a file that does not exist
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "THIS_FILE_SHOULD_BE_MISSING.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testPlayersInvalidLocationNegativeLocation() throws BadConfigFormatException { // Exception test for a file with a negative starting location for a player
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_InvalidLocation1.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testPlayersInvalidLocationExceedsBounds() throws BadConfigFormatException { // Exception test for a file with an invalid location for a player
		board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_InvalidLocation2.txt", "ClueWeaponsStudent.txt");
		board.loadConfigFiles();
	}
	
	@Test(expected = BadConfigFormatException.class)
	public void testPlayersInvalidColor() throws BadConfigFormatException { // Exception test for a file with an invalid for a player
        board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent_BadColor.txt", "ClueWeaponsStudent.txt");
        board.loadConfigFiles();
    }
	
	//--------------------------------CARD TESTS ------------------------------------------
	
	// Test that the deck is the right size, and that it contains the correct number of each type of card
    @Test
    public void testCardCreation() {
        Set<Card> cards = board.getCards();
        // deck should have 21 cards total, before dealing
        assertEquals(21, cards.size());

        int numPeople = 0;
        int numWeapons = 0;
        int numRooms = 0;
        for (Card testCard : cards) {
            if (testCard.getCardType() == CardType.PERSON) {
                numPeople++;
            } else if (testCard.getCardType() == CardType.WEAPON) {
                numWeapons++;
            } else if (testCard.getCardType() == CardType.ROOM) {
                numRooms++;
            } else {
                fail("Unexpected card type.");
            }
        }
        assertEquals(6, numPeople);
        assertEquals(6, numWeapons);
        assertEquals(9, numRooms);
    }

    // Test that the deck of cards contains cards we expect it to
    @Test
    public void testCardAttributes() {
        Set<Card> cards = board.getCards();
        
        // create some cards that should be in the deck, to compare to
        Card mechE = new Card("Unoriginal MechE", CardType.PERSON);
        Card quadCopter = new Card("QuadCopter", CardType.WEAPON);
        Card bathroom = new Card("Bathroom", CardType.ROOM);
        
        assertTrue(cards.contains(mechE));
        assertTrue(cards.contains(quadCopter));
        assertTrue(cards.contains(bathroom));
    }
    
    // Test that cards are dealt correctly
    @Test
    public void testDealingCards() {
        board.dealCards(); // TODO should be in initialize?
        // test that there are three cards, one of each type, in the solution
        assertTrue(board.getSolution().person != null);
        assertTrue(board.getSolution().weapon != null);
        assertTrue(board.getSolution().room != null);
        // as we hand out cards, they should be removed from the deck set named "cards". when its empty, they're all dealt
        assertTrue(board.getCards().isEmpty());
        // everyone should have the same number of cards, within one of each other
        for (int i = 0; i < board.getPlayers().size() - 2; i++) {
            Player currentPlayer = board.getPlayers().get(i);
            Player nextPlayer = board.getPlayers().get(i + 1);
            assertTrue(currentPlayer.getSeenCards().size() != 0); // shouldn't be empty
            assertTrue(nextPlayer.getSeenCards().size() != 0); // shouldn't be empty
            assertTrue(Math.abs(currentPlayer.getSeenCards().size() - nextPlayer.getSeenCards().size()) < 2); 
        }
        
        // make sure all the players cards are unique
        for (int i = 0; i < board.getPlayers().size() - 1; i++) {
            Player currentPlayer = board.getPlayers().get(i);
            for (int j = 0; j < currentPlayer.getSeenCards().size(); j++) {
                Card currentCard = currentPlayer.getSeenCards().get(j);
                // make sure the current card isn't in the solution
                assertTrue(currentCard.getCardName() != board.getSolution().person);
                assertTrue(currentCard.getCardName() != board.getSolution().weapon);
                assertTrue(currentCard.getCardName() != board.getSolution().room);
                for (int k = 0; k < i; k++) {
                    assertTrue(!Arrays.asList(board.getPlayers().get(k).getSeenCards()).contains(currentCard)); // this mess 
                                                    // sees if the current card is in the seen array for the current player
                }
                for (int k = i; k < board.getPlayers().size() - 1; k++) {
                    assertTrue(!Arrays.asList(board.getPlayers().get(k).getSeenCards()).contains(currentCard)); // this mess 
                                                    // sees if the current card is in the seen array for the current player
                }
            }
        }
    }
}






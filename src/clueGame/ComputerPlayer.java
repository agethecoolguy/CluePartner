package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private Solution accusation;
	boolean solutionFound;
	int numberOfTimesAccused;

    private char roomLastVisited = 'Z';
    //private char secondToLastRoomVisited = 'Z';

    public ComputerPlayer(String playerName, Color color, int row, int column) {
        super(playerName, color, row, column, false);
        super.isHuman = false;
        solutionFound = false;
        numberOfTimesAccused = 0;
    }

    public BoardCell pickLocation(Set<BoardCell> targets) {
        Random rng = new Random();
        ArrayList<BoardCell> doorwayTargets = new ArrayList<BoardCell>();
        ArrayList<BoardCell> arrayTargets = new ArrayList<BoardCell>();
        BoardCell cellToReturn = null;
        for (BoardCell b : targets) { // this loop determines each target that consists of an unvisited doorway
            arrayTargets.add(b);
            if (b.isDoorway() && ((b.getRoomLetter() != roomLastVisited))) {// && (b.getRoomLetter() != secondToLastRoomVisited))) {
            	doorwayTargets.add(b);
            }
        }
        if (doorwayTargets.size() >= 1) {
            // one or more rooms, possibly with one visited
            cellToReturn = doorwayTargets.get(rng.nextInt(doorwayTargets.size()));
        } else {
            // no rooms, or rooms we've already visited
            cellToReturn = arrayTargets.get(rng.nextInt(targets.size()));
        }
        
        if (cellToReturn.isDoorway()){ // this ensures that walkways are never considered as "rooms"
        	roomLastVisited = cellToReturn.getRoomLetter();
        }
        return cellToReturn;
    }

    public void makeAccusation(Solution accusation, Board board) {
    	numberOfTimesAccused++;
    	solutionFound = board.checkAccusation(accusation, playerName, false);
    }

    public void makeSuggestion(Board board) {
    	Solution suggestion = generateSolution(board);
    	suggestion.room = board.getRooms().get(currentCell.getRoomLetter());
    	Card returnedCard = board.handleSuggestion(suggestion, this, currentCell, false);
    	if (returnedCard != null) {
    		myCards.add(returnedCard);
    	}
    	else {
    		accusation = suggestion;
    		solutionFound = true;
    	}
    }
    
    public Solution generateSolution(Board board) {
    	String person = null;
    	String weapon = null;
    	String room = null;
    	
    	Random rng = new Random();
    	ArrayList<String> tempList;
    	    	
    	//The following for loops simply ensure that the computer tries REALLY hard not to suggest a card it knows is wrong
    	for(int i = 0; i < 100; i++) { // Select a person to suggest
    		tempList = new ArrayList<String>(board.getPlayerNames());
    		person = tempList.get(rng.nextInt(tempList.size()));
    		// if myCards and seenCards don't contain the card, or if the final iteration of the for loop...
    		if ((!super.myCards.contains(new Card(person, CardType.PERSON)) && !super.seenCards.contains(new Card(person, CardType.PERSON))) || i == 100) {
    			break;
    		}
    	}
    	
    	for(int i = 0; i < 100; i++) { // Select a weapon to suggest
    		tempList = new ArrayList<String>(board.getWeaponNames());
    		weapon = tempList.get(rng.nextInt(tempList.size()));
    		if ((!super.myCards.contains(new Card(weapon, CardType.WEAPON)) && !super.seenCards.contains(new Card(weapon, CardType.WEAPON))) || i == 100) {
    			break;
    		}
    	}
    	
    	for(int i = 0; i < 100; i++) { // Select a room to suggest
    		tempList = new ArrayList<String>(board.getRoomNames());
    		room = tempList.get(rng.nextInt(tempList.size()));
    		if ((!super.myCards.contains(new Card(room, CardType.ROOM)) && !super.seenCards.contains(new Card(room, CardType.ROOM))) || i == 100) {
    			break;
    		}
    	}
    	
		return new Solution(person, weapon, room);
    }

    public Character getRoomLastVisited() {
        return roomLastVisited;
    }

    // for testing only:
    public void setRoomLastVisited(Character room) {
        roomLastVisited = room;
    }
    
    public boolean determineIfSolutionViable(Board board) {
    	if (solutionFound) { // no need to continue if a solution has already been found
    		return false;
    	}
    	
    	Random rng = new Random();
    	int amountOfCards = myCards.size() + seenCards.size();
    	// The following function converges to a 20% likelihood as (amountOfCards + numberOfTimesAcused) 
    	// becomes large. As such, the greater this number, the more likely the computer will begin making
    	// random accusations with an ever increasing probability of success.
    	int oddsOfAccusing = (int) (100 * (1 - Math.exp((amountOfCards + numberOfTimesAccused) * -0.05)) / 5);
    	if (rng.nextInt(100) <= oddsOfAccusing) {
    		makeAccusation(generateSolution(board), board);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
	public void makeMove(Set<BoardCell> targets, Board board) {
		if (solutionFound) {
			makeAccusation(accusation, board);
			return;
		}
		if (determineIfSolutionViable(board)) {
			return;
		}
		BoardCell moveCell = this.pickLocation(targets); 
		super.move(moveCell);
		if (moveCell.isWalkway()) {
			return;
		}
		makeSuggestion(board);
	}
}

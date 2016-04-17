package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JDialog;

public class HumanPlayer extends Player {
	public static boolean isHumanTurn;	
	private Solution suggestion;
	String currentRoom;

	public HumanPlayer(String playerName, Color color, int row, int column) {
		super(playerName, color, row, column, true);
		super.isHuman = true;
	}
	
	/*
	public HumanPlayer(Player player) {
		super(player.getPlayerName(), player.getColor(), player.getRow(), player.getColumn(), true);
		super.isHuman = true;
	}
	*/
	
	public void makeMove(BoardCell moveCell, Board board) {
		super.move(moveCell);
		if (moveCell.isWalkway()) {
			return;
		}
		else{
			makeSuggestion(board);
		}
	
	}
	
	public void makeSuggestion(Board board){		
		currentRoom = board.getRooms().get(currentCell.getRoomLetter());		
    	suggestion = new Solution("unknown", "unknown", currentRoom);
    	JDialog suggestionDialog = new HumanPlayerSuggestionGUI(board.getPlayerNames(), board.getWeaponNames(), this);
    	suggestionDialog.setVisible(true);
    	Card returnedCard = board.handleSuggestion(suggestion, this, currentCell);
    	if (returnedCard != null) {
    		myCards.add(returnedCard);
    	}	
	}

	public void setSuggestion(Solution suggestion) {
		this.suggestion = suggestion;
	}

	public String getCurrentRoom() {
		return currentRoom;
	}


}

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class HumanPlayer extends Player {

	public static boolean isHumanTurn;	
	private Solution suggestion;
	private String currentRoom;
	private Boolean suggestionCanceled = false;
	private Solution accusation;
	public Boolean humansSuggestionDisproved;

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
		board.repaint();
		currentRoom = board.getRooms().get(currentCell.getRoomLetter());
    	JDialog suggestionDialog = new HumanPlayerSuggestionGUI(board.getPlayerNames(), board.getWeaponNames(), this);
    	suggestionDialog.setVisible(true);
    	if (suggestionCanceled){
    		suggestionCanceled = false;
    		return;
    	}
    	isHumanTurn = false;
    	suggestionCanceled = false;
    	Card returnedCard = board.handleSuggestion(suggestion, this, currentCell, false);   	
    	if (returnedCard != null) {
    		humansSuggestionDisproved = true;
    	}
    	else{
    		humansSuggestionDisproved = false;    		
    	}
	}
	
    public void makeAccusation(Solution accusation, Board board) {
    	isHumanTurn = false;
    	board.checkAccusation(accusation, this.playerName, false);
    	board.unhighlightTargets();
    }

	public void setSuggestion(Solution suggestion) {
		this.suggestion = suggestion;
	}

	public String getCurrentRoom() {
		return currentRoom;
	}

	public void setSuggestionCanceled(Boolean suggestionCanceled) {
		this.suggestionCanceled = suggestionCanceled;
	}
		
	public void setAccusation(Solution accusation) {
		this.accusation = accusation;
	}
	public Solution getAccusation() {
		return accusation;
	}
}

package clueGame;

import java.awt.Color;
import java.util.Set;

public class HumanPlayer extends Player {

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
	
	public void makeMove(BoardCell moveCell) {
		super.move(moveCell);
	}

}

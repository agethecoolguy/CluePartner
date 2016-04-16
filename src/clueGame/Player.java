package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Player extends JPanel {
	public static final int MAX_CARDS = 18;
	private String playerName;
	private int row;
	private int column;
	protected BoardCell currentCell;
	private int xPixelCoordinate;
	private int yPixelCoordinate;
	int sideLength;
	private Color color;
	protected ArrayList<Card> myCards = new ArrayList<Card>();
	protected ArrayList<Card> seenCards = new ArrayList<Card>();
	protected boolean isHuman;
	
	public Player(String playerName, Color color, int row, int column, boolean isHuman) {
		this.playerName = playerName;
		this.row = row;
		this.column = column;
		this.isHuman = isHuman;
		xPixelCoordinate = column * BoardCell.SIDE_LENGTH;
		yPixelCoordinate = row * BoardCell.SIDE_LENGTH;
		sideLength = (int) (BoardCell.SIDE_LENGTH/1.5);
		this.color = color;
	}

	public Card disproveSuggestion(Solution suggestion) {
		Card suggestedPerson = new Card(suggestion.person, CardType.PERSON);
		Card suggestedWeapon = new Card(suggestion.weapon, CardType.WEAPON);
		Card suggestedRoom = new Card(suggestion.room, CardType.ROOM);
		
		ArrayList<Card> possibleCardsToReturn = new ArrayList<Card>();
		if (myCards.contains(suggestedPerson)) {
			possibleCardsToReturn.add(suggestedPerson);
		}
		if (myCards.contains(suggestedWeapon)) {
			possibleCardsToReturn.add(suggestedWeapon);
		}
		if (myCards.contains(suggestedRoom)) {
			possibleCardsToReturn.add(suggestedRoom);
		}
		
		Random rng = new Random();
		
		if (possibleCardsToReturn.size() == 0) {
			return null;
		}
		return possibleCardsToReturn.get(rng.nextInt(possibleCardsToReturn.size()));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D.Double circle = new Ellipse2D.Double(xPixelCoordinate, yPixelCoordinate, sideLength, sideLength);
		g2.setColor(color);
		g2.fill(circle);
		g2.setColor(Color.BLACK);
		g2.drawOval(xPixelCoordinate, yPixelCoordinate, sideLength, sideLength);
	}
	
	public void move(BoardCell targetCell) {
		Random rng = new Random();
		int maxOffset = BoardCell.SIDE_LENGTH - sideLength;
		row = targetCell.getRow();
		yPixelCoordinate = row * BoardCell.SIDE_LENGTH;
		yPixelCoordinate = yPixelCoordinate + rng.nextInt(maxOffset);
		column = targetCell.getCol();
		xPixelCoordinate = column * BoardCell.SIDE_LENGTH;
		xPixelCoordinate= xPixelCoordinate + rng.nextInt(maxOffset);
		currentCell = targetCell;
	}
	
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", myCards=" + myCards + "]";
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Point getLocation() {
		return new Point(row, column);
	}

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public ArrayList<Card> getSeenCards() {
        return seenCards;
    }
    
	//for the sake of testing:
    public void setMyCards(ArrayList<Card> myCards) {
		this.myCards = myCards;
	}

	public boolean isHuman() {
		return isHuman;
	}
	
	public void setHuman(boolean isHuman) {
		this.isHuman = isHuman;
	}
	
	public BoardCell getCurrentCell() {
		return currentCell;
	}
}

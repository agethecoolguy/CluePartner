package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class BoardCell extends JPanel {
	public static final int SIDE_LENGTH = 30;
	private int row;
	private int column;
	private int x;
	private int y;
	private Boolean isNameCell;
	public DoorDirection doorDirection;
	private char roomLetter;
	private String roomName;
	public static final int STROKE_SIZE = 3;

	public BoardCell(DoorDirection doorDirection, char roomLetter) {
		this.doorDirection = doorDirection;
		this.roomLetter = roomLetter;
		setIsNameCell(false);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// There are TWO drawn components here. The last one drawn will be superimposed over the first
		// This may cause some visual bugs where elements appear to disappear if they are drawn in the wrong order.
		
		if (roomLetter == 'W') {
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, SIDE_LENGTH, SIDE_LENGTH);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, SIDE_LENGTH, SIDE_LENGTH);
		}
		else if (isDoorway()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(STROKE_SIZE));
			switch (doorDirection) {
			case UP:
				g2.draw(new Line2D.Float(x, y + STROKE_SIZE, x + SIDE_LENGTH, y + STROKE_SIZE));
				// Increase y direction by STROKE_SIZE to translate the door stroke up
				break;
			case LEFT:
				g2.draw(new Line2D.Float(x + STROKE_SIZE, y, x + STROKE_SIZE, y + SIDE_LENGTH));
				// Increase x direction by STROKE_SIZE to translate the door stroke right
				break;
			case RIGHT:
				g2.draw(new Line2D.Float(x + SIDE_LENGTH - STROKE_SIZE, y, x + SIDE_LENGTH - STROKE_SIZE, y + SIDE_LENGTH));
				// Reduce x direction by STROKE_SIZE to translate the door stroke left
				break;
			case DOWN:
				g2.draw(new Line2D.Float(x, y + SIDE_LENGTH - STROKE_SIZE, x + SIDE_LENGTH, y + SIDE_LENGTH - STROKE_SIZE));
				// Reduce y direction by STROKE_SIZE to translate the door stroke down
				break;
			case NONE:
				break;
			default:
				break;
			}
			g2.setStroke(new BasicStroke(1));
		}
		
		if (isNameCell) {
			g.setColor(Color.BLACK);
			g.drawString(roomName, x, y);
		}
		
	}

	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE && doorDirection != null);
	}

	public boolean isWalkway() {
		if (roomLetter == 'W')
			return true;
		return false;
	}

	public boolean isRoom() {
		if (roomLetter != 'W' || roomLetter != 'X')
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "BoardCell [row=" + row + ", col=" + column + ", doorDirection=" + doorDirection + ", roomLetter="
				+ roomLetter + "]\n";
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
		y = row * SIDE_LENGTH;
	}

	public int getCol() {
		return column;
	}

	public void setCol(int col) {
		this.column = col;
		x = col * SIDE_LENGTH;
	}

	public char getRoomLetter() {
		return roomLetter;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public Boolean getIsNameCell() {
		return isNameCell;
	}

	public void setIsNameCell(Boolean isNameCell) {
		this.isNameCell = isNameCell;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}

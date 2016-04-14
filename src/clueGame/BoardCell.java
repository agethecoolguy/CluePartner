package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Line2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BoardCell extends JPanel {
	public static final int SIDE_LENGTH = 30;
	private int row;
	private int column;
	private int xPixelCoordinate;
	private int yPixelCoordinate;
	
	private Boolean isNameCell;
	public DoorDirection doorDirection;
	private char roomLetter;
	private String roomName;
	public static final int STROKE_SIZE = 3;
	public Color walkwayColor;
	public Color roomColor;

	public BoardCell(DoorDirection doorDirection, char roomLetter) {
		this.doorDirection = doorDirection;
		this.roomLetter = roomLetter;
		setIsNameCell(false);
		setFocusable(true);
		walkwayColor = Color.YELLOW;
		roomColor = new Color(238, 238, 238);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// There are TWO drawn components here. The last one drawn will be superimposed over the first
		// This may cause some visual bugs where elements appear to disappear if they are drawn in the wrong order.
		
		if (roomLetter == 'W') {
			g.setColor(walkwayColor);
			g.fillRect(xPixelCoordinate, yPixelCoordinate, SIDE_LENGTH, SIDE_LENGTH);
			g.setColor(Color.BLACK);
			g.drawRect(xPixelCoordinate, yPixelCoordinate, SIDE_LENGTH, SIDE_LENGTH);
		}
		else if (isDoorway()) {
			g.setColor(roomColor);
			g.fillRect(xPixelCoordinate, yPixelCoordinate, SIDE_LENGTH, SIDE_LENGTH);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(STROKE_SIZE));
			switch (doorDirection) {
			case UP:
				g2.draw(new Line2D.Float(xPixelCoordinate, yPixelCoordinate + STROKE_SIZE, xPixelCoordinate + SIDE_LENGTH, yPixelCoordinate + STROKE_SIZE));
				// Increase y direction by STROKE_SIZE to translate the door stroke up
				break;
			case LEFT:
				g2.draw(new Line2D.Float(xPixelCoordinate + STROKE_SIZE, yPixelCoordinate, xPixelCoordinate + STROKE_SIZE, yPixelCoordinate + SIDE_LENGTH));
				// Increase x direction by STROKE_SIZE to translate the door stroke right
				break;
			case RIGHT:
				g2.draw(new Line2D.Float(xPixelCoordinate + SIDE_LENGTH - STROKE_SIZE, yPixelCoordinate, xPixelCoordinate + SIDE_LENGTH - STROKE_SIZE, yPixelCoordinate + SIDE_LENGTH));
				// Reduce x direction by STROKE_SIZE to translate the door stroke left
				break;
			case DOWN:
				g2.draw(new Line2D.Float(xPixelCoordinate, yPixelCoordinate + SIDE_LENGTH - STROKE_SIZE, xPixelCoordinate + SIDE_LENGTH, yPixelCoordinate + SIDE_LENGTH - STROKE_SIZE));
				// Reduce y direction by STROKE_SIZE to translate the door stroke down
				break;
			case NONE:
				break;
			default:
				break;
			}
			g2.setStroke(new BasicStroke(1));
		}
		else {
			g.setColor(roomColor);
			g.fillRect(xPixelCoordinate, yPixelCoordinate, SIDE_LENGTH, SIDE_LENGTH);
		}
		
		if (isNameCell) {
			g.setColor(Color.BLACK);
			g.drawString(roomName, xPixelCoordinate, yPixelCoordinate);
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
		yPixelCoordinate = row * SIDE_LENGTH;
	}

	public int getCol() {
		return column;
	}

	public void setCol(int col) {
		this.column = col;
		xPixelCoordinate = col * SIDE_LENGTH;
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
	
	public void setWalkwayColor(Color color) {
		this.walkwayColor = color;
	}

	public boolean containsMouse(int mouseX, int mouseY) {
		Rectangle rectangle = new Rectangle(xPixelCoordinate, yPixelCoordinate, SIDE_LENGTH, SIDE_LENGTH);
		if (rectangle.contains(new Point(mouseX, mouseY))) {
			return true;
		}
		return false;
	}
	
	public void setRoomColor(Color roomColor) {
		this.roomColor = roomColor;
	}
}

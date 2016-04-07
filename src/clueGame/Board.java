package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

public class Board extends JPanel {
	public final int NUM_PLAYERS = 6;
	public final int NUM_WEAPONS = 6;
	private int numColumns, numRows;
	private BoardCell[][] board;
	private static Map<Character, String> rooms; //this maps room characters to room names
	private Set<String> roomNames;
	private String boardFile;
	private String legendFile;
	private String playersFile;
	private String weaponsFile;
	private Set<BoardCell> visitedCells;
	private Set<BoardCell> targetCells;
	private Map<BoardCell, LinkedList<BoardCell>> adjacencyMatrix;
	private ArrayList<Player> players;
	private ArrayList<String> weaponNames;
    private Set<Card> deckOfCards;
	private Solution solution;

	public Board() {
		instatiateDataMembers();
		boardFile = "Clue_LayoutTeacher.csv";
		legendFile = "Clue_LegendTeacher.txt";
		playersFile = "CluePlayersTeacher.txt";
		weaponsFile = "ClueWeaponsTeacher.txt"; 
	}
	
	public Board(String boardFile, String legendFile) {
		this(); //call regular constructor
		this.boardFile = boardFile;
		this.legendFile = legendFile;
	}
	
	public Board(String boardFile, String legendFile, String playersFile, String weaponsFile) {
		this(boardFile, legendFile); //call two-String constructor
		this.playersFile = playersFile;
		this.weaponsFile = weaponsFile;
	}
	
	private void instatiateDataMembers() {
		adjacencyMatrix = new HashMap<BoardCell, LinkedList<BoardCell>>();
		rooms = new HashMap<Character, String>();
		players = new ArrayList<Player>();
		roomNames = new HashSet<String>();
		weaponNames = new ArrayList<String>();
		deckOfCards = new HashSet<Card>();
	}

	public void initialize() {
		try {
			loadConfigFiles();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		setUpCards();
		//dealCards();
		calcAdjacencies();
	}
	
	public void loadConfigFiles() throws BadConfigFormatException{
		loadRoomConfig();
		loadBoardConfig();
		loadPlayersConfig();
		loadWeaponsConfig();		
	}

	@SuppressWarnings("resource")
	public void loadRoomConfig() throws BadConfigFormatException {
		BufferedReader legendReader;
		String line = "";
		String delimiter = ",";
		try {
			legendReader = new BufferedReader(new FileReader(legendFile));
			rooms = new HashMap<Character, String>();
			while ((line = legendReader.readLine()) != null) {
				// use comma as separator
				String[] data = line.split(delimiter);

				if (data.length != 3) {
					throw new BadConfigFormatException("Invalid format in legend file.");
				}

				char roomID = data[0].toCharArray()[0];
				String roomName = data[1].trim();
				
				String type = data[2].trim();
				if (type.equals("Card")) {
					roomNames.add(roomName);
				} else if (type.equals("Other")) {
					//do nothing
				} else {
					throw new BadConfigFormatException("Invalid format in legend file.");
				}
				rooms.put(roomID, roomName);
			}
		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException(legendFile + " not found");
		} catch (IOException e) {
			throw new BadConfigFormatException(e.getMessage());
		}	
	}

	@SuppressWarnings("resource")
	public void loadBoardConfig() throws BadConfigFormatException {
		ArrayList<ArrayList<BoardCell>> tempBoard = new ArrayList<ArrayList<BoardCell>>();
		String line = "";
		String delimiter = ",";
		BufferedReader boardReader;
		try {
			int tempBoardRow = 0;
			int tempBoardCol = 0;
			boardReader = new BufferedReader(new FileReader(boardFile));
			while((line = boardReader.readLine())  != null) {
				String[] data = line.split(delimiter);
				tempBoard.add(new ArrayList<BoardCell>());
				for (String s : data) {
					BoardCell boardCell = stringToBoardCell(s);
					boardCell.setCol(tempBoardCol);
					boardCell.setRow(tempBoardRow);
					boardCell.setRoomName(rooms.get(boardCell.getRoomLetter()));
					tempBoard.get(tempBoardRow).add(boardCell);
					tempBoardCol++;

				}
				tempBoardRow++;
				tempBoardCol = 0;
			}

		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("CSV File not found");
		}
		catch (IOException e) {
			throw new BadConfigFormatException(e.getMessage());
		}

		numRows = tempBoard.size();
		numColumns = tempBoard.get(0).size();
		board = new BoardCell[numRows][numColumns];

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				// if the config file has uneven columns or rows, 
				// the following statement will throw an IndexOutOfBoundsException
				try {
					board[i][j] = tempBoard.get(i).get(j);
				}
				// We convert it here to a BadConfigFormatException
				catch (Exception e) {
					throw new BadConfigFormatException(". Rows or columns uneven. Error in loadBoardConfig()");
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
    public void loadPlayersConfig() throws BadConfigFormatException {
		// SET UP LEGEND
        FileReader reader;
        try {
            reader = new FileReader(playersFile);
        } catch (FileNotFoundException e) {
            throw new BadConfigFormatException(playersFile + " not found");
        }

		Scanner in = new Scanner(reader);
        for (int i = 0; i < NUM_PLAYERS; i++) {
        	if (!in.hasNextLine()) {
        		throw new BadConfigFormatException("Not enough players in " + playersFile);
        	}
        	
        	String playerLine = in.nextLine();
        	String[] data = playerLine.split(",");
        	
        	if (data.length != 4) { //If there are not four elements in data
				throw new BadConfigFormatException("Invalid format in " + playersFile);
			}
        	
        	//get player name
        	String playerName = data[0];
        	//get player color: invalid if color code does not exist
        	Color playerColor;
			try {
				playerColor = convertColor(data[1]);
			} catch (Exception e) {
				throw new BadConfigFormatException("Invalid color in " + playersFile);
			}
			//get player row: invalid if out of bounds
        	int playerRow = Integer.parseInt(data[2]);
        	if (playerRow >= numRows || playerRow < 0) {
        		throw new BadConfigFormatException("Invalid player row in " + playersFile);
        	}
        	//get player column: invalid if out of bounds
        	int playerColumn = Integer.parseInt(data[3]);
        	if (playerColumn >= numColumns || playerColumn < 0) {
        		throw new BadConfigFormatException("Invalid player column in " + playersFile);
        	}
        	
        	players.add(new Player(playerName, playerColor, playerRow, playerColumn));
        }
        
        if (in.hasNextLine()) {
        	throw new BadConfigFormatException("Too many players in " + playersFile);
        }
        in.close();
	}

	@SuppressWarnings("resource")
    public void loadWeaponsConfig() throws BadConfigFormatException {
		// SET UP WEAPONS
        FileReader reader;
        try {
            reader = new FileReader(weaponsFile);
        } catch (FileNotFoundException e) {
        	throw new BadConfigFormatException("Could not find " + weaponsFile);
        }

		Scanner in = new Scanner(reader);
        for(int i = 0; i < NUM_WEAPONS; i++) {
        	if (!in.hasNextLine()) {
        		throw new BadConfigFormatException("Not enough weapons in " + weaponsFile);
        	}
        	String weaponName = in.nextLine();
        	weaponNames.add(weaponName);
        }
        
        if (in.hasNextLine()) {
        	throw new BadConfigFormatException("Too many weapons in " + weaponsFile);
        }
        in.close();
	}
	
	public Color convertColor(String strColor) throws Exception {
		Color color;
		Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
		color = (Color)field.get(null);
		return color;
	}
	
	public void selectAnswer() {
		Random rng = new Random();
		// put cards into solution, removing them from the list
        String solutionPerson = players.get(rng.nextInt(players.size())).getPlayerName();
        deckOfCards.remove(new Card(solutionPerson, CardType.PERSON));
        String solutionWeapon = weaponNames.get(rng.nextInt(weaponNames.size()));
        deckOfCards.remove(new Card(solutionWeapon, CardType.WEAPON));
        String solutionRoom = (new ArrayList<String>(roomNames)).get(rng.nextInt(roomNames.size()));
        deckOfCards.remove(new Card(solutionRoom, CardType.ROOM));
        setSolution(new Solution(solutionPerson, solutionWeapon, solutionRoom));
	}
	
	public Card handleSuggestion(Solution suggestion, Player accusingPlayer, BoardCell clicked) {
		int indexOfAccuser = -1; //This is an error code: index of accuser should never be -1
		
		indexOfAccuser = players.indexOf(accusingPlayer);
		if (indexOfAccuser == -1) {
			throw new RuntimeException("Index of accusing player not found.");
		}
		Card result = null;
		
		for (int i = 0; i < NUM_PLAYERS - 1; i++) {
			Player currentPlayer = players.get((indexOfAccuser + i + 1) % NUM_PLAYERS);
			// If indexOfaccuser + i + 1 exceeds NUM_PLAYERS, the modulo operator causes a "wraparound" effect.
			// This ensures that each player is queried.
			result = currentPlayer.disproveSuggestion(suggestion);
			if (result == null) { // if the player does not have a matching card, continue to next player
				continue;
			}
			else { // break for the first player that has a matching card
				break;
			}
		}
		return result;
	}
	
	public boolean checkAccusation(Solution accusation) {
		if (accusation.person.equals(solution.person) &&
			accusation.room.equals(solution.room) &&
			accusation.weapon.equals(solution.weapon)) {
			return true;
		}
		return false;
	}

	public BoardCell stringToBoardCell(String data) throws BadConfigFormatException {
		DoorDirection direction = DoorDirection.NONE;
		Boolean isNameCell = false;
		if (data.length() != 1) {
			if(data.endsWith("U")) {
			    direction = DoorDirection.UP;
			} else if(data.endsWith("D")) {
			    direction = DoorDirection.DOWN;
			} else if(data.endsWith("L")) {
			    direction = DoorDirection.LEFT;
			} else if(data.endsWith("R")) {
			    direction = DoorDirection.RIGHT;
			} else if(data.endsWith("N")) {
				isNameCell = true;
			    direction = DoorDirection.NONE;
			} else {
			    throw new BadConfigFormatException(". Invalid characters on board. Error in convertToBoardCell()" );
			}
		}
		
		char doorLetter = data.charAt(0);
		BoardCell tempCell = new BoardCell(direction, doorLetter);
		tempCell.setIsNameCell(isNameCell);
		
		if (!rooms.containsKey(doorLetter)) {
			throw new BadConfigFormatException("Invalid room character on the board.");
		} else {
			return tempCell;
		}
		
	}
	
	private void setUpCards() {
		for (Player person : players) {
			deckOfCards.add(new Card(person.getPlayerName(), CardType.PERSON));
		}
		for (String weapon : weaponNames) {
			deckOfCards.add(new Card(weapon, CardType.WEAPON));
		}
		for (String roomName : roomNames) {
			deckOfCards.add(new Card(roomName, CardType.ROOM));
		}
	}
	
    public void dealCards() {
        Random rng = new Random();
        selectAnswer();
        // distribute remaining cards to the players
        int playerNumber = 0;
        int originalSize = deckOfCards.size();
        for (int i = 0; i < originalSize; i++) {
            Player nextPlayer = players.get(playerNumber++ % players.size());
            int randIndex = rng.nextInt(deckOfCards.size());
            Card cardToAdd = (Card) deckOfCards.toArray()[randIndex];
            nextPlayer.getSeenCards().add(cardToAdd);
            nextPlayer.getMyCards().add(cardToAdd);
            deckOfCards.remove(cardToAdd); // remove the card from the deck once its in someone's hand
        }
    }
	
	public void calcAdjacencies() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				LinkedList<BoardCell> adjacencyList = new LinkedList<BoardCell>();

				if (board[i][j].isRoom() && board[i][j].isDoorway()) {
					switch ((board[i][j]).getDoorDirection()) {
					case UP:
						adjacencyList.add(board[i - 1][j]);
						break;
					case DOWN:
						adjacencyList.add(board[i + 1][j]);
						break;
					case LEFT:
						adjacencyList.add(board[i][j - 1]);
						break;
					case RIGHT:
						adjacencyList.add(board[i][j + 1]);
						break;
					case NONE:
						break;
					default:
						break;
					}
				}

				else if (board[i][j].isWalkway()) {
					if (i - 1 >= 0) {
						if (board[i - 1][j].isWalkway()) {
							adjacencyList.add(board[i - 1][j]);
						} else if (board[i - 1][j].isDoorway() && (board[i - 1][j]).getDoorDirection() == DoorDirection.DOWN) {
							adjacencyList.add(board[i - 1][j]);
						}
					} if (j - 1 >= 0) {
						if (board[i][j - 1].isWalkway()) { 
							adjacencyList.add(board[i][j - 1]);
						} else if (board[i][j - 1].isDoorway() && (board[i][j - 1]).getDoorDirection() == DoorDirection.RIGHT) { 
							adjacencyList.add(board[i][j - 1]);
						}
					} if (i + 1 < numRows) {
						if (board[i + 1][j].isWalkway()) { 
							adjacencyList.add(board[i + 1][j]);
						} else if (board[i + 1][j].isDoorway() && (board[i + 1][j]).getDoorDirection() == DoorDirection.UP) { 
							adjacencyList.add(board[i + 1][j]);
						}
					} if (j + 1 < numColumns) {
						if (board[i][j + 1].isWalkway()) { 
							adjacencyList.add(board[i][j + 1]);
						} else if (board[i][j + 1].isDoorway() && (board[i][j + 1]).getDoorDirection() == DoorDirection.LEFT) { 
							adjacencyList.add(board[i][j + 1]);
						}
					}
				}

				adjacencyMatrix.put(board[i][j], adjacencyList);
			}
		}	
	}

	
	public LinkedList<BoardCell> getAdjList(int i, int j) {
		return adjacencyMatrix.get(board[i][j]);
	}

	public void calcTargets(int i, int j, int steps) {
		calcTargets(board[i][j], steps);
	}

	private void calcTargets(BoardCell boardCell, int steps) {
		visitedCells = new HashSet<BoardCell>();
		visitedCells.add(boardCell);
		targetCells = new HashSet<BoardCell>();

		findAllTargets(boardCell, steps);
	}

	private void findAllTargets(BoardCell boardCell, int steps) {
		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();
		calcAdjacencies();
		adjacentCells = adjacencyMatrix.get(boardCell);
		for (BoardCell cell : visitedCells) {
			if (adjacentCells.contains(cell)) adjacentCells.remove(cell);
		}

		for (BoardCell adjCell : adjacentCells) {
			visitedCells.add(adjCell);
			if (steps == 1 || adjCell.isDoorway()){
				targetCells.add(adjCell);
			} else {
			    findAllTargets(adjCell, steps - 1);
			}
			visitedCells.remove(adjCell);
		}		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintCells(g);
		paintPlayers(g);
	}
	
	public void paintCells(Graphics g) {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				board[i][j].paintComponent(g);
			}
		}
	}
	
	public void paintPlayers(Graphics g) {
		for (Player p : players) {
			p.paintComponent(g);
		}
	}

	public Set<BoardCell> getTargets() {
		return targetCells;
	}
	
	public static Map<Character, String> getRooms() {
		return rooms;
	}

	public Set<String> getCardRooms() {
        return roomNames;
    }

    public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}
	
	public Point getDimensions() { // returns pixel dimensions of board
		int sideLength = BoardCell.SIDE_LENGTH;
		return new Point((numColumns + 1)* sideLength, (numRows + 2) * sideLength);
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}

	public ArrayList<Player> getPlayers() {
		return players;
    }

    public ArrayList<String> getWeapons() {
        return weaponNames;
    }
	   
	//for the sake of testing:
    public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Set<Card> getCards() {
		return deckOfCards;
	}

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
    
    public void findDoorways() {
    	int counter = 0;
    	for (int i = 0; i < numRows; i++) {
    		for (int j = 0; j < numColumns; j++){
    			if (board[i][j].isDoorway()) {
    				System.out.println(counter + ": Found doorway.");
    				counter++;
    			}
    		}
    	}
    }
}

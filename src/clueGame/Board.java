package clueGame;

import java.awt.Color;
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

public class Board {
	public final int NUM_PLAYERS = 6;
	public final int NUM_WEAPONS = 6;
	private int numColumns, numRows;
	private BoardCell[][] board;
	private static Map<Character, String> rooms;
	private Set<String> cardRooms;
	private String boardFile;
	private String legendFile;
	private String playersFile;
	private String weaponsFile;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private Map<BoardCell, LinkedList<BoardCell>> adjacencyMatrix;
	private ArrayList<Player> players;
	private ArrayList<String> weapons;
	private Set<Card> cards;
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
		cardRooms = new HashSet<String>();
		weapons = new ArrayList<String>();
		cards = new HashSet<Card>();
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
					cardRooms.add(roomName);
				}
				else if (type.equals("Other")) {
					//do nothing
				}
				else {
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
				for (String s : data){

					BoardCell boardCell = stringToBoardCell(s);

					boardCell.setCol(tempBoardCol);
					boardCell.setRow(tempBoardRow);
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
	
	public Color convertColor(String strColor) throws Exception {
		Color color;
		Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
		color = (Color)field.get(null);
		return color;
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
        	weapons.add(weaponName);
        }
        
        if (in.hasNextLine()) {
        	throw new BadConfigFormatException("Too many weapons in " + weaponsFile);
        }
        
        in.close();
	}
	
	public void selectAnswer() {
		
	}
	
	public Card handleSuggestion(Solution suggestion, String accusingPlayer, BoardCell clicked) {
		return null;
	}
	
	public boolean checkAccusation(Solution accusation) {
		if (accusation.person == solution.person &&
			accusation.room == solution.room &&
			accusation.weapon == solution.weapon) {
			return true;
		}
		return false;
	}

	public BoardCell stringToBoardCell(String data) throws BadConfigFormatException {

		DoorDirection direction = DoorDirection.NONE;

		if (data.length() != 1) {
			if(data.endsWith("U")) direction = DoorDirection.UP;
			else if(data.endsWith("D")) direction = DoorDirection.DOWN;
			else if(data.endsWith("L")) direction = DoorDirection.LEFT;
			else if(data.endsWith("R")) direction = DoorDirection.RIGHT;
			else if(data.endsWith("N")) direction = DoorDirection.NONE;
			else throw new BadConfigFormatException(". Invalid characters on board. Error in convertToBoardCell()" );
		}
		
		char doorLetter = data.charAt(0);
		if (!rooms.containsKey(doorLetter)) {
			throw new BadConfigFormatException("Invalid room character on the board.");
		} else {
			return new BoardCell(direction, doorLetter);
		}
		
	}
	
	private void setUpCards() {
		for (Player person : players) {
			cards.add(new Card(person.getPlayerName(), CardType.PERSON));
		}
		for (String weapon : weapons) {
			cards.add(new Card(weapon, CardType.WEAPON));
		}
		for (String roomName : cardRooms) {
			cards.add(new Card(roomName, CardType.ROOM));
		}
	}
	
    public void dealCards() {
        Random rng = new Random();
        // put cards into solution, removing them from the list
        String solutionPerson = players.get(rng.nextInt(players.size())).getPlayerName();
        cards.remove(new Card(solutionPerson, CardType.PERSON));
        String solutionWeapon = weapons.get(rng.nextInt(weapons.size()));
        cards.remove(new Card(solutionWeapon, CardType.WEAPON));
        String solutionRoom = (new ArrayList<String>(cardRooms)).get(rng.nextInt(cardRooms.size()));
        cards.remove(new Card(solutionRoom, CardType.ROOM));
        setSolution(new Solution(solutionPerson, solutionRoom, solutionWeapon));
        // distribute remaining cards to the players
        int playerNumber = 0;
        int originalSize = cards.size();
        for (int i = 0; i < originalSize; i++) {
            Player nextPlayer = players.get(playerNumber++ % players.size());
            int randIndex = rng.nextInt(cards.size());
            Card cardToAdd = (Card) cards.toArray()[randIndex];
            nextPlayer.getSeenCards().add(cardToAdd);
            nextPlayer.getMyCards().add(cardToAdd);
            cards.remove(cardToAdd); // remove the card from the deck once its in someone's hand
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
						if (board[i - 1][j].isWalkway()) 
							adjacencyList.add(board[i - 1][j]);
						else if (board[i - 1][j].isDoorway() && (board[i - 1][j]).getDoorDirection() == DoorDirection.DOWN)
							adjacencyList.add(board[i - 1][j]);
					}
					if (j - 1 >= 0) {
						if (board[i][j - 1].isWalkway()) 
							adjacencyList.add(board[i][j - 1]);
						else if (board[i][j - 1].isDoorway() && (board[i][j - 1]).getDoorDirection() == DoorDirection.RIGHT) 
							adjacencyList.add(board[i][j - 1]);
					}
					if (i + 1 < numRows) {
						if (board[i + 1][j].isWalkway()) 
							adjacencyList.add(board[i + 1][j]);
						else if (board[i + 1][j].isDoorway() && (board[i + 1][j]).getDoorDirection() == DoorDirection.UP) 
							adjacencyList.add(board[i + 1][j]);
					}
					if (j + 1 < numColumns) {
						if (board[i][j + 1].isWalkway()) 
							adjacencyList.add(board[i][j + 1]);
						else if (board[i][j + 1].isDoorway() && (board[i][j + 1]).getDoorDirection() == DoorDirection.LEFT) 
							adjacencyList.add(board[i][j + 1]);
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

		visited = new HashSet<BoardCell>();
		visited.add(boardCell);
		targets = new HashSet<BoardCell>();

		findAllTargets(boardCell, steps);
	}

	private void findAllTargets(BoardCell boardCell, int steps) {

		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();
		calcAdjacencies();
		adjacentCells = adjacencyMatrix.get(boardCell);
		for (BoardCell cell : visited) {
			if (adjacentCells.contains(cell)) adjacentCells.remove(cell);
		}

		for (BoardCell adjCell : adjacentCells) {
			visited.add(adjCell);

			if (steps == 1 || adjCell.isDoorway()){
				targets.add(adjCell);
			}

			else findAllTargets(adjCell, steps - 1);

			visited.remove(adjCell);
		}		
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public static Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Set<Card> getCards() {
		return cards;
	}

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

}

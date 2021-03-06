package clueGame;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private Board board;
	private GameControlGUI gameControl;
	private PlayerCardDisplay playerCardDisplay;
	private DetectiveNotesDialog notesDialog;
	//private static boolean humanPlayerTurnFinished;
	private int indexOfCurrentPlayer;
	private int numPlayers;
	
	public ClueGame() throws HeadlessException {
		super();
		numPlayers = 6; //TEMPORARY--------------------------------------------------<<<<
		indexOfCurrentPlayer = -1;
		//HumanPlayer.isHumanTurn = true;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clue");
        
        //gameControl = new GameControlGUI();
        //add(gameControl, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(createFileMenu());
        
        board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent.txt", "ClueWeaponsStudent.txt");
        board.initialize(numPlayers);
        board.dealCards();
        board.setClueGame(this);
        setSize(910, 900);
		add(board, BorderLayout.CENTER);
		HumanPlayer.isHumanTurn = false;
		
		notesDialog = new DetectiveNotesDialog(board.getPlayers(), board.getCardRooms(), board.getWeaponNames());
		
		gameControl = new GameControlGUI(this);
		add(gameControl, BorderLayout.SOUTH);
		
		ArrayList<Card> exampleCards = new ArrayList<Card>();
		exampleCards.add(new Card("Bob the Uncontrolable Psycho", CardType.PERSON));
		exampleCards.add(new Card("Joe", CardType.PERSON));
		exampleCards.add(new Card("Chainsaw", CardType.WEAPON));
		exampleCards.add(new Card("Texas", CardType.ROOM));
		playerCardDisplay = new PlayerCardDisplay(board.getHumanPlayerCards());
		add(playerCardDisplay, BorderLayout.EAST);
		
		String splashScreenMessage = "You are the " + board.getHumanPlayerName() + ", press Next Player to begin play";
		JOptionPane.showMessageDialog(this, splashScreenMessage, "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	public void checkForMatchCompletion() {
		if (board.isGameOver()) {
			String winnerMessage = "The winner is " + board.getWinner().getPlayerName() + "!\n GAME OVER";
			JOptionPane.showMessageDialog(this, winnerMessage, "A WINNER HAS RISEN", JOptionPane.INFORMATION_MESSAGE);
			System.exit(NORMAL);
		}
		return;
	}
	
	private JMenu createFileMenu(){
	    JMenu menu = new JMenu("File");
	    menu.add(createDetectiveNotesItem());
	    menu.add(createFileExitItem());
	    return menu;
	}
	
	private JMenuItem createDetectiveNotesItem() {
	    JMenuItem detectiveNotesItem = new JMenuItem("Notes");
	    class MenuItemListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                notesDialog.setVisible(true);
            }
	    }
	    detectiveNotesItem.addActionListener(new MenuItemListener());
        return detectiveNotesItem;
    }

    private JMenuItem createFileExitItem(){
	    JMenuItem exitItem = new JMenuItem("Exit");
	    class MenuItemListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
	    }
	    exitItem.addActionListener(new MenuItemListener());
	    return exitItem;
	}

	public Player nextPlayer() { // progresses to next player and returns the new current player
		indexOfCurrentPlayer = (indexOfCurrentPlayer + 1) % numPlayers; // modulo operator provides "circular behavior"
		if (board.getPlayers().get(indexOfCurrentPlayer).isHuman()) {
			HumanPlayer.isHumanTurn = true;
		}
		
		return board.getPlayers().get(indexOfCurrentPlayer);
	}
	
	public Player getCurrentPlayer() {
		return board.getPlayers().get(indexOfCurrentPlayer);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
        game.setVisible(true);
    }
	
	public GameControlGUI getGameControl() {
		return gameControl;
	}


}

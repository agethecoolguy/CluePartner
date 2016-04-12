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
	Board board;
	GameControlGUI gameControl;
	PlayerCardDisplay playerCardDisplay;
	DetectiveNotesDialog notesDialog;
	private boolean humanPlayerTurnFinished;
	
	public ClueGame() throws HeadlessException {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clue");
        
        //gameControl = new GameControlGUI();
        //add(gameControl, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(createFileMenu());
        
        board = new Board("Clue_LayoutStudent.csv", "Clue_LegendStudent.txt", "CluePlayersStudent.txt", "ClueWeaponsStudent.txt");
        board.initialize();
        board.dealCards();
        //int pixelWiseWidth = (int) board.getDimensions().getX();
        //int pixelWiseHeight = (int) board.getDimensions().getY();
        setSize(910, 900);
		add(board, BorderLayout.CENTER);
		
		notesDialog = new DetectiveNotesDialog(board.getPlayers(), board.getCardRooms(), board.getWeapons());
		
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

	public static void main(String[] args) {
		ClueGame game = new ClueGame();
        game.setVisible(true);
    }

	public boolean isHumanPlayerTurnFinished() {
		return humanPlayerTurnFinished;
	}
	
	public Player getNextPlayer() {
		// FINISH AT SOME TIME
		
		
		return null;
	}
}

package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlGUI extends JPanel {
    private JTextField currentPlayerTextField;
    private JTextField dieTextField;
    private JTextField guessTextField;
    private JTextField resultTextField;
    private ClueGame clueGame;
    private int dieValue;

    public GameControlGUI(ClueGame clueGame) {
    	this.clueGame = clueGame;
        setLayout(new GridLayout(2, 1));
        JPanel newPanel = new JPanel();
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        newPanel = createCurrentPlayerPanel();
        topPanel.add(newPanel);
        newPanel = createNextPlayerButton();
        topPanel.add(newPanel);
        newPanel = createAccusationButton();
        topPanel.add(newPanel);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        newPanel = createDiePanel();
        lowerPanel.add(newPanel);
        newPanel = createGuessPanel();
        lowerPanel.add(newPanel);
        newPanel = createResultPanel();
        lowerPanel.add(newPanel);

        this.add(topPanel);
        this.add(lowerPanel);

    }

    private JPanel createNextPlayerButton() {
        JButton nextPlayerButton = new JButton("Next player");
        nextPlayerButton.addActionListener(new NextPlayerListener());
        JPanel nextPlayerPanel = new JPanel();
        nextPlayerPanel.add(nextPlayerButton);
        return nextPlayerPanel;
    }
    
    public class NextPlayerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clueGame.getBoard().clearSuggestionFields();
			if (HumanPlayer.isHumanTurn) {
				String errorMessage = "You need to finish your turn!";
				JOptionPane.showMessageDialog(clueGame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Player currentPlayer = clueGame.nextPlayer();
			currentPlayerTextField.setText(currentPlayer.getPlayerName());
			if (currentPlayer.isHuman) {
				HumanPlayer human = (HumanPlayer) currentPlayer;
				clueGame.getBoard().calcTargets(human.getRow(), human.getColumn(), rollDie());
				HumanPlayer.isHumanTurn = true;
				clueGame.getBoard().highlightTargets();
			}
			else {
				ComputerPlayer cpu = (ComputerPlayer) currentPlayer;
				clueGame.getBoard().calcTargets(cpu.getRow(), cpu.getColumn(), rollDie());
				cpu.makeMove(clueGame.getBoard().getTargets(), clueGame.getBoard());
				resultTextField.setText(clueGame.getBoard().getSuggestionResultString());
				guessTextField.setText(clueGame.getBoard().getGuessString());
			}
			clueGame.repaint();
			clueGame.checkForMatchCompletion();
		}
	}

    private JPanel createAccusationButton() {
        JButton nextPlayerButton = new JButton("Make accusation");
        JPanel nextPlayerPanel = new JPanel();
        nextPlayerPanel.add(nextPlayerButton);
        return nextPlayerPanel;
    }

    private JPanel createCurrentPlayerPanel() {
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setLayout(new GridLayout(1, 2));

        JLabel currentPlayerLabel = new JLabel("Current player:");
        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        currentPlayerPanel.add(currentPlayerLabel);

        currentPlayerTextField = new JTextField(20);
        //currentPlayerTextField.setText(clueGame.getCurrentPlayer().getPlayerName()); // temporary, should grab from board
        //currentPlayer.setHorizontalAlignment(JLabel.CENTER);
        currentPlayerTextField.setEditable(false);
        currentPlayerPanel.add(currentPlayerTextField);
        
        currentPlayerPanel.setBorder(new TitledBorder(new EtchedBorder(), "Whose turn?"));

        return currentPlayerPanel;
    }

    private JPanel createDiePanel() {
        JPanel diePanel = new JPanel();
        diePanel.setLayout(new GridLayout(1, 2));

        JLabel dieLabel = new JLabel("Roll:");
        diePanel.add(dieLabel);

        dieTextField = new JTextField(3);
        dieTextField.setEditable(false);
        diePanel.add(dieTextField);

        diePanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));

        return diePanel;
    }

    private JPanel createGuessPanel() {
        JPanel guessPanel = new JPanel();
        guessPanel.setLayout(new GridLayout(2, 1));

        JLabel guessLabel = new JLabel("Guess: ");
        guessPanel.add(guessLabel);

        guessTextField = new JTextField(50);
        guessTextField.setEditable(false);
        guessPanel.add(guessTextField);

        guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

        return guessPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(2, 1));

        JLabel resultLabel = new JLabel("Response: ");
        resultPanel.add(resultLabel);

        resultTextField = new JTextField(15);
        resultTextField.setEditable(false);
        resultPanel.add(resultTextField);

        resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

        return resultPanel;
    }
    
    public int rollDie(){
    	Random rng = new Random();
    	dieValue = rng.nextInt(6) + 1;
    	dieTextField.setText(String.valueOf(dieValue));
    	
    	return dieValue; // + 1 ensures that zero is never rolled
    }
    
    /*public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clue");
        frame.setSize(1000, 250);
        GameControlGUI GUI = new GameControlGUI();
        frame.add(GUI, BorderLayout.CENTER);
        frame.setVisible(true);
    }*/
}

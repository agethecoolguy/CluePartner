package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlGUI extends JPanel {
    private JTextField currentPlayer;
    private JTextField die;
    private JTextField guess;
    private JTextField result;

    public GameControlGUI() {
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
        JPanel nextPlayerPanel = new JPanel();
        nextPlayerPanel.add(nextPlayerButton);
        return nextPlayerPanel;
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

        currentPlayer = new JTextField(20);
        currentPlayer.setText("PlayerName"); // temporary, should grab from board
        //currentPlayer.setHorizontalAlignment(JLabel.CENTER);
        currentPlayer.setEditable(false);
        currentPlayerPanel.add(currentPlayer);
        
        currentPlayerPanel.setBorder(new TitledBorder(new EtchedBorder(), "Whose turn?"));

        return currentPlayerPanel;
    }

    private JPanel createDiePanel() {
        JPanel diePanel = new JPanel();
        diePanel.setLayout(new GridLayout(1, 2));

        JLabel dieLabel = new JLabel("Roll:");
        diePanel.add(dieLabel);

        die = new JTextField(5);
        die.setText("Roll"); // temporary, should grab from board
        die.setEditable(false);
        diePanel.add(die);

        diePanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));

        return diePanel;
    }

    private JPanel createGuessPanel() {
        JPanel guessPanel = new JPanel();
        guessPanel.setLayout(new GridLayout(2, 1));

        JLabel guessLabel = new JLabel("Guess: ");
        guessPanel.add(guessLabel);

        guess = new JTextField(25);
        guessPanel.add(guess);

        guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

        return guessPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(1, 2));

        JLabel resultLabel = new JLabel("Response: ");
        resultPanel.add(resultLabel);

        result = new JTextField(10);
        result.setText("Response"); // temporary, should grab from board
        result.setEditable(false);
        resultPanel.add(result);

        resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

        return resultPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clue");
        frame.setSize(1000, 250);
        GameControlGUI GUI = new GameControlGUI();
        frame.add(GUI, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

package clueGame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PlayerCardDisplay extends JPanel {
	ArrayList<Card> playerCards;
	ArrayList<Card> peopleCards;
	ArrayList<Card> weaponCards;
	ArrayList<Card> roomCards;
	
	public PlayerCardDisplay(ArrayList<Card> playerCards) {
		super();
		this.playerCards = playerCards;
		setLayout(new FlowLayout());
		setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		
		peopleCards = new ArrayList<Card>();
		weaponCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();
		for (Card c : playerCards) {
			if (c.getCardType() == CardType.PERSON) {
				peopleCards.add(c);
			}
			else if (c.getCardType() == CardType.WEAPON) {
				weaponCards.add(c);
			}
			else if (c.getCardType() == CardType.ROOM){
				roomCards.add(c);
			}
			else {
				throw new RuntimeException("Unexpected card type.");
			}
		}
		
		add(createPeoplePanel());
		add(createWeaponPanel());
		add(createRoomPanel());
		
		setLayout(new GridLayout(3,1));
	}

	/*public void updatePlayerCards(ArrayList<Card> playerCards) {
		playerCards = playerCards;
	}*/
	
	public JPanel createPeoplePanel() {
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(peopleCards.size(), 1));
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		for (Card c : peopleCards) {
			JTextField cardToDisplay = new JTextField(15);
			cardToDisplay.setText(c.getCardName());
			cardToDisplay.setEditable(false);
			peoplePanel.add(cardToDisplay);
		}
		
		return peoplePanel;
	}
	
	public JPanel createWeaponPanel() {
		JPanel weaponPanel = new JPanel();
		weaponPanel.setLayout(new GridLayout(weaponCards.size(), 1));
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapon"));
		for (Card c : weaponCards) {
			JTextField cardToDisplay = new JTextField(15);
			cardToDisplay.setText(c.getCardName());
			cardToDisplay.setEditable(false);
			weaponPanel.add(cardToDisplay);
		}
		
		return weaponPanel;
	}
	
	public JPanel createRoomPanel() {
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new GridLayout(roomCards.size(), 1));
		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Room"));
		for (Card c : roomCards) {
			JTextField cardToDisplay = new JTextField(15);
			cardToDisplay.setText(c.getCardName());
			cardToDisplay.setEditable(false);
			roomPanel.add(cardToDisplay);
		}
		
		return roomPanel;
	}
	
	
}

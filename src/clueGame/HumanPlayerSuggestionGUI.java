package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class HumanPlayerSuggestionGUI extends JDialog{
	private Set<String> playerList;
	private Set<String> weaponList;
	private HumanPlayer humanPlayer;
	private String suggestedPerson;
	private String suggestedWeapon;
	private JComboBox<String> personCombo, weaponCombo;
	private JButton submit, cancel;

	public HumanPlayerSuggestionGUI(Set<String> players, Set<String> weapons, HumanPlayer human){
	    playerList = players;
	    weaponList = weapons;
	    humanPlayer = human;
	    
		setSize(new Dimension(400, 250));
		setLayout(new GridLayout(4, 2));
		setTitle("Make a Guess");
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		JLabel roomLabel = new JLabel("Your room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
				
		JTextField currentRoom = new JTextField(15);
		currentRoom.setEditable(false);
		currentRoom.setText(humanPlayer.getCurrentRoom());
		personCombo = createPersonCombo();
		weaponCombo = createWeaponCombo();	
		
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		
		this.add(roomLabel);
		this.add(currentRoom);
		this.add(personLabel);
		this.add(personCombo);
		this.add(weaponLabel);
		this.add(weaponCombo);
		this.add(submit);
		this.add(cancel);
		
		ComboListener listener = new ComboListener();
		personCombo.addActionListener(listener);
		weaponCombo.addActionListener(listener);
		submit.addActionListener(new SubmitListener());
		cancel.addActionListener(new CancelListener());

	}
	
	private JComboBox<String> createPersonCombo() {
		boolean personInit = false;
	    JComboBox<String> personDropdown = new JComboBox<String>();
	    for (String person : playerList){
	    	if (!personInit) {
	    		suggestedPerson = person;
	    		personInit = true;
	    	}
	        personDropdown.addItem(person);
	    }
		return personDropdown;
	}
	
	private JComboBox<String> createWeaponCombo() {	 
		boolean weaponInit = false;
	    JComboBox<String> weaponDropdown = new JComboBox<String>();
	    for (String weapon : weaponList){
	    	if (!weaponInit) {
	    		suggestedWeapon = weapon;
	    		weaponInit = true;
	    	}
	        weaponDropdown.addItem(weapon);
	    }
		return weaponDropdown;
	}
	
	private class ComboListener implements ActionListener {
		  public void actionPerformed(ActionEvent e)
		  {
		    if (e.getSource() == personCombo){
		    	suggestedPerson = personCombo.getSelectedItem().toString();
		    }
		    else
		      suggestedWeapon = weaponCombo.getSelectedItem().toString();
		  }
		}
	class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			humanPlayer.setSuggestion(new Solution(suggestedPerson, suggestedWeapon, humanPlayer.getCurrentRoom()));
			setVisible(false);
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			humanPlayer.setSuggestionCanceled(true);
			setVisible(false);
		}
	}
	
}

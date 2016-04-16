package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotesDialog extends JDialog {
	ArrayList<Player> playerList;
	Set<String> roomSet;
	Set<String> weaponList;
	public DetectiveNotesDialog(ArrayList<Player> players, Set<String> rooms, Set<String> weapons) {
	    playerList = players;
	    roomSet = rooms;
	    weaponList = weapons;
	    
	    setTitle("Detective Notes");
	    setSize(700, 550);
		setLayout(new GridLayout(3, 2));
		
		JPanel personPanel = createPersonPanel();
		JPanel guessPersonPanel = createGuessPersonPanel();
		JPanel roomPanel = createRoomPanel();
		JPanel guessRoomPanel = createGuessRoomPanel();
		JPanel weaponPanel = createWeaponPanel();
		JPanel guessWeaponPanel = createGuessWeaponPanel();
		
		this.add(personPanel);
		this.add(guessPersonPanel);
		this.add(roomPanel);
		this.add(guessRoomPanel);
		this.add(weaponPanel);
		this.add(guessWeaponPanel);
		
	}
	
	private JPanel createPersonPanel() {
	    JPanel peoplePanel = new JPanel();
        peoplePanel.setLayout(new GridLayout(0, 2));
        peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        
        for (Player person : playerList) {
            JCheckBox playerCheckbox = new JCheckBox(person.getPlayerName());
            peoplePanel.add(playerCheckbox);
        }
        return peoplePanel;
	}
	
	private JPanel createGuessPersonPanel() {
	    JPanel bestPersonGuess = new JPanel();
	    bestPersonGuess.setLayout(new GridLayout(1, 0));
	    bestPersonGuess.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
	    
	    JComboBox<String> personDropdown = new JComboBox<String>();
	    personDropdown.addItem("Unsure");
	    for (Player person : playerList){
	        personDropdown.addItem(person.getPlayerName());
	    }
	    bestPersonGuess.add(personDropdown);
		return bestPersonGuess;
	}
	
	private JPanel createRoomPanel() {
	    JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new GridLayout(0, 2));
        roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        
        for (String room : roomSet) {
            JCheckBox roomCheckbox = new JCheckBox(room);
            roomPanel.add(roomCheckbox);
        }
        return roomPanel;
	}
	
	private JPanel createGuessRoomPanel() {
	    JPanel bestRoomGuess = new JPanel();
	    bestRoomGuess.setLayout(new GridLayout(1, 0));
	    bestRoomGuess.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
	    
	    JComboBox<String> roomDropdown = new JComboBox<String>();
	    roomDropdown.addItem("Unsure");
	    for (String room : roomSet){
	        roomDropdown.addItem(room);
	    }
	    bestRoomGuess.add(roomDropdown);
		return bestRoomGuess;
	}
	
	private JPanel createWeaponPanel() {
	    JPanel weaponPanel = new JPanel();
        weaponPanel.setLayout(new GridLayout(0, 2));
        weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        
        for (String weapon : weaponList) {
            JCheckBox weaponCheckbox = new JCheckBox(weapon);
            weaponPanel.add(weaponCheckbox);
        }
        return weaponPanel;
	}
	
	private JPanel createGuessWeaponPanel() {
	    JPanel bestWeaponGuess = new JPanel();
	    bestWeaponGuess.setLayout(new GridLayout(1, 0));
	    bestWeaponGuess.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
	    
	    JComboBox<String> weaponDropdown = new JComboBox<String>();
	    weaponDropdown.addItem("Unsure");
	    for (String weapon : weaponList){
	        weaponDropdown.addItem(weapon);
	    }
	    bestWeaponGuess.add(weaponDropdown);
		return bestWeaponGuess;
	}
}

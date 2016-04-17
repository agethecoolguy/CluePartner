package clueGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class HumanPlayerAccusationGUI extends JDialog{
	private Set<String> playerList;
	private Set<String> weaponList;
	private Set<String> roomList;
	private HumanPlayer humanPlayer;
	private String accusedPerson;
	private String accusedWeapon;
	private String accusedRoom;
	private JComboBox<String> personCombo, weaponCombo, roomCombo;
	private JButton submit, cancel;
	private Boolean submitted; 
	
	public HumanPlayerAccusationGUI(Set<String> players, Set<String> weapons, Set<String> rooms, HumanPlayer human){
	    playerList = players;
	    weaponList = weapons;
	    roomList = rooms;
	    humanPlayer = human;
	    
		setSize(new Dimension(400, 250));
		setLayout(new GridLayout(4, 2));
		setTitle("Make an Accusation");
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		JLabel roomLabel = new JLabel("Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		roomCombo = createRoomCombo();
		personCombo = createPersonCombo();
		weaponCombo = createWeaponCombo();
		
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		
		this.add(roomLabel);
		this.add(roomCombo);
		this.add(personLabel);
		this.add(personCombo);
		this.add(weaponLabel);
		this.add(weaponCombo);
		this.add(submit);
		this.add(cancel);
		
		ComboListener listener = new ComboListener();
		personCombo.addActionListener(listener);
		weaponCombo.addActionListener(listener);
		roomCombo.addActionListener(listener);
		submit.addActionListener(new SubmitListener());
		cancel.addActionListener(new CancelListener());		
	}
	
	private JComboBox<String> createPersonCombo() {	    
	    JComboBox<String> personDropdown = new JComboBox<String>();
	    for (String person : playerList){
	        personDropdown.addItem(person);
	    }
		return personDropdown;
	}
	
	private JComboBox<String> createWeaponCombo() {	    
	    JComboBox<String> weaponDropdown = new JComboBox<String>();
	    for (String weapon : weaponList){
	        weaponDropdown.addItem(weapon);
	    }
		return weaponDropdown;
	}
	private JComboBox<String> createRoomCombo() {	    
	    JComboBox<String> roomDropdown = new JComboBox<String>();
	    for (String room : roomList){
	        roomDropdown.addItem(room);
	    }
		return roomDropdown;
	}
	private class ComboListener implements ActionListener {
		  public void actionPerformed(ActionEvent e)
		  {
		    if (e.getSource() == personCombo){
		    	accusedPerson = personCombo.getSelectedItem().toString();
		    }
		    else if (e.getSource() == weaponCombo){
		    	accusedWeapon = weaponCombo.getSelectedItem().toString();
		    }
		    else {
		    	accusedRoom = roomCombo.getSelectedItem().toString();
		    }
		    	
		  }
		}
	class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			submitted = true;
			humanPlayer.setAccusation(new Solution(accusedPerson, accusedWeapon, accusedRoom));
			setVisible(false);
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			submitted = false;
			setVisible(false);
		}
	}
	public Boolean getSubmitted() {
		return submitted;
	}

}

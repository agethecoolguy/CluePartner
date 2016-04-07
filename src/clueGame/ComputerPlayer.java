package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

    private Character roomLastVisited = 'Z';

    public ComputerPlayer(String playerName, Color color, int row, int column) {
        super(playerName, color, row, column);
        super.isHuman = false;
    }

    public BoardCell pickLocation(Set<BoardCell> targets) {
        Random rng = new Random();
        ArrayList<BoardCell> doorwayTargets = new ArrayList<BoardCell>();
        ArrayList<BoardCell> arrayTargets = new ArrayList<BoardCell>();
        BoardCell cellToReturn = null;
        for (BoardCell b : targets) {
            arrayTargets.add(b);
            if (b.isDoorway() && (b.getRoomLetter() != roomLastVisited)) {
                doorwayTargets.add(b);
            }
        }
        if (doorwayTargets.size() == 1) {
            // only one unvisited room
            cellToReturn = doorwayTargets.get(0); 
        } else if (doorwayTargets.size() > 1) {
            // more than one room, possibly with one visited
            cellToReturn = doorwayTargets.get(rng.nextInt(doorwayTargets.size()));
        } else {
            // no rooms, or rooms weve already visited
            cellToReturn = arrayTargets.get(rng.nextInt(targets.size()));
        }
        
        if (cellToReturn.isDoorway()){
            roomLastVisited = cellToReturn.getRoomLetter();
        }
        return cellToReturn;
    }

    public void makeAccusation() {

    }

    public void makeSuggestion(Board board, BoardCell location) {

    }

    public Character getRoomLastVisited() {
        return roomLastVisited;
    }

    // for testing only:
    public void setRoomLastVisited(Character room) {
        roomLastVisited = room;
    }
}

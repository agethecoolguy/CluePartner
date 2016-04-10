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
        for (BoardCell b : targets) { // this loop determines each target that consists of an unvisited doorway
            arrayTargets.add(b);
            if (b.isDoorway() && (b.getRoomLetter() != roomLastVisited)) {
                doorwayTargets.add(b);
            }
        }
        if (doorwayTargets.size() >= 1) {
            // one or more rooms, possibly with one visited
            cellToReturn = doorwayTargets.get(rng.nextInt(doorwayTargets.size()));
        } else {
            // no rooms, or rooms we've already visited
            cellToReturn = arrayTargets.get(rng.nextInt(targets.size()));
        }
        
        if (cellToReturn.isDoorway()){ // this ensures that walkways are never considered as "rooms"
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

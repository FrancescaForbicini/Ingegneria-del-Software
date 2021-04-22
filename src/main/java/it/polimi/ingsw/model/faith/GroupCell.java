package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;
import java.util.Map;

public class GroupCell {
    private Collection<Cell> cells;
    private int tileVictoryPoints;

    public GroupCell(Collection<Cell> cells, int tileVictoryPoints) {
        this.cells = cells;
        this.tileVictoryPoints = tileVictoryPoints;
    }

    public Collection<Cell> getCells() {
        return cells;
    }

    public void assignTileVictoryPoints() {
        FaithTrack faithTrack = FaithTrack.getInstance();
        Map<Player, Integer> markers = faithTrack.getMarkers();
        for (Map.Entry<Player, Integer> playerPosition:
             markers.entrySet()) {
            Cell cell = faithTrack.getCell(playerPosition.getValue());
            if(cells.contains(cell)) {
                playerPosition.getKey().addPersonalVictoryPoints(tileVictoryPoints);
            }
        }
    }
}

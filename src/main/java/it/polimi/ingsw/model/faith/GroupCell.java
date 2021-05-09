package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GroupCell {
    private final ArrayList<Cell> cells;
    private final int tileVictoryPoints;

    public GroupCell(ArrayList<Cell> cells, int tileVictoryPoints) {
        this.cells = cells;
        this.tileVictoryPoints = tileVictoryPoints;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void assignTileVictoryPoints(FaithTrack faithTrack) {
        Map<TurnTaker, Integer> markers = faithTrack.getMarkers();
        for (Map.Entry<TurnTaker, Integer> playerPosition:
                markers.entrySet()) {
            Cell cell = faithTrack.getCell(playerPosition.getValue());
            if(cells.contains(cell)) {
                playerPosition.getKey().addPersonalVictoryPoints(tileVictoryPoints);
            }
        }
        cells.stream().filter(Cell::isPopeCell).forEach(Cell::disablePopeCell);
    }
}
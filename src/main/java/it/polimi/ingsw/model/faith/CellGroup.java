package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.ArrayList;
import java.util.Map;

public class CellGroup {
    private final ArrayList<Integer> cellIDs;
    private final int tileVictoryPoints;

    public CellGroup(ArrayList<Integer> cellIDs, int tileVictoryPoints) {
        this.cellIDs = cellIDs;
        this.tileVictoryPoints = tileVictoryPoints;
    }

    public ArrayList<Integer> getCellIDs() {
        return cellIDs;
    }

    public boolean contains(int cellID){
        return cellIDs.contains(cellID);
    }

    public int getTileVictoryPoints() {
        return tileVictoryPoints;
    }
}
package it.polimi.ingsw.model.faith;

import java.util.ArrayList;

/**
 * Group of the cells in the faith track
 */
public class CellGroup {
    private final ArrayList<Integer> cellIDs;
    private final int tileVictoryPoints;

    public CellGroup(ArrayList<Integer> cellIDs, int tileVictoryPoints) {
        this.cellIDs = cellIDs;
        this.tileVictoryPoints = tileVictoryPoints;
    }

    public boolean contains(int cellID){
        return cellIDs.contains(cellID);
    }

    /**
     * Gets the victory points of each cell
     *
     * @return victory points of the cell
     */
    public int getTileVictoryPoints() {
        return tileVictoryPoints;
    }
}
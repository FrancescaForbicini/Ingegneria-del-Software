package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.view.gui.custom_gui.Modifiable;

import java.util.ArrayList;

/**
 * Group of the cells in the faith track
 */
public class CellGroup implements Modifiable {
    private final ArrayList<Integer> cellIDs;
    private final int tileVictoryPoints;

    /**
     * @param cellIDs cells contained in this
     * @param tileVictoryPoints points given to all player inside this when the first pass over the Pope cell at the end of this
     */
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

    public ArrayList<Integer> getCellIDs() {
        return cellIDs;
    }
}
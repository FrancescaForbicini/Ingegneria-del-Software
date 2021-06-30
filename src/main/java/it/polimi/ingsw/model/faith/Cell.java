package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.view.gui.custom_gui.Modifiable;

/**
 * Cells of the faith track
 */
public class Cell implements Modifiable {
    private final int cellID;
    private final int cellVictoryPoints;
    private boolean popeCell;

    /**
     * @param cellID to identify the cell
     * @param cellVictoryPoints points given when the player pass over this
     * @param popeCell true iff represents the end of a CellGroup
     */
    public Cell(int cellID, int cellVictoryPoints, boolean popeCell) {
        this.cellID = cellID;
        this.cellVictoryPoints = cellVictoryPoints;
        this.popeCell = popeCell;
    }

    public int getCellID() { return cellID; }

    public int getCellVictoryPoints() {
        return cellVictoryPoints;
    }

    public boolean isPopeCell() {
        return popeCell;
    }

    public void disablePopeCell(){
        popeCell = false;
    }

    /**
     * Converts the color of a specific cell
     * @return the cell colored
     */
    public StringBuilder convertColor(){
        if(isPopeCell())
            return new StringBuilder().append(Color.ANSI_YELLOW).append(getCellID()).append(Color.RESET);
        if (cellID % 3 == 0)
            return new StringBuilder().append(Color.ANSI_YELLOW).append("+").append(getCellVictoryPoints());
        return new StringBuilder().append(getCellID());
    }
}

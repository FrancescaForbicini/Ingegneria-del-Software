package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.view.cli.Color;

public class Cell {
    private final int cellID;
    private final int cellVictoryPoints;
    private boolean popeCell;

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
        else
            return new StringBuilder().append(getCellID());
    }
}

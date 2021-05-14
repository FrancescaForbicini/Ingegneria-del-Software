package it.polimi.ingsw.model.faith;

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
}

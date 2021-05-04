package it.polimi.ingsw.model.faith;

public class Cell {
    private final int cellVictoryPoints;
    private boolean popeCell;

    public Cell(int cellVictoryPoints, boolean popeCell) {
        this.cellVictoryPoints = cellVictoryPoints;
        this.popeCell = popeCell;
    }

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

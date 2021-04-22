package it.polimi.ingsw.model.faith;

public class Cell {
    private int cellVictoryPoints;
    private boolean popeSpace;

    public Cell(int cellVictoryPoints, boolean popeSpace) {
        this.cellVictoryPoints = cellVictoryPoints;
        this.popeSpace = popeSpace;
    }

    public int getCellVictoryPoints() {
        return cellVictoryPoints;
    }

    public boolean isPopeSpace() {
        return popeSpace;
    }

    public void disablePopeCell(){
        popeSpace = false;
    }
}

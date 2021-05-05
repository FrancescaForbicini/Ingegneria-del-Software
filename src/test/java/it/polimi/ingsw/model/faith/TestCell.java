package it.polimi.ingsw.model.faith;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCell {

    @Test
    public void getCellVictoryPoints() {
        Cell normalCell = new Cell(3, false);
        assertEquals(3, normalCell.getCellVictoryPoints());
        Cell pope_cell = new Cell(3, true);
        assertEquals(3, pope_cell.getCellVictoryPoints());
    }

    @Test
    public void isPopeSpace() {
        Cell normalCell = new Cell(3, false);
        assertFalse(normalCell.isPopeCell());
        Cell pope_cell = new Cell(3, true);
        assertFalse(pope_cell.isPopeCell());
    }

    @Test
    public void disablePopeCell() {
        Cell cell = new Cell(3, false);
        assertFalse(cell.isPopeCell());
        cell.disablePopeCell();
        assertTrue(cell.isPopeCell());
    }
}
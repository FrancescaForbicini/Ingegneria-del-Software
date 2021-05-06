package it.polimi.ingsw.model.faith;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCell {


    @Test
    public void isPopeSpace() {
        Cell normalCell = new Cell(3, false);
        assertFalse(normalCell.isPopeCell());
        Cell pope_cell = new Cell(3, true);
        assertTrue(pope_cell.isPopeCell());
    }

    @Test
    public void disablePopeCell() {
        Cell cell = new Cell(3, true);
        assertTrue(cell.isPopeCell());
        cell.disablePopeCell();
        assertFalse(cell.isPopeCell());
    }
}
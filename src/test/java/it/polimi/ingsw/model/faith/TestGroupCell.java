package it.polimi.ingsw.model.faith;

import org.junit.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestGroupCell {

    @Test
    public void getCells() {
        List<Cell> cells = Arrays.asList(new Cell(1, false), new Cell(10, true));
        GroupCell gc = new GroupCell(cells, 10);
        assertEquals(cells, gc.getCells());
    }

    @Test
    public void testAssignTileVictoryPoint() {
        fail();
    }
}
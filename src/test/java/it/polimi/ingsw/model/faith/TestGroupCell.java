package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.*;

import static org.junit.Assert.*;

public class TestGroupCell {

    private Player player;
    private FaithTrack faithTrack;
    private Map<TurnTaker,Integer> markers;
    private ArrayList<Cell> cells;
    private ArrayList<GroupCell> groupCells;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        cells = new ArrayList<>();
        groupCells = new ArrayList<>();
        markers = new HashMap<>();
        cells.add( new Cell(1,false));
        cells.add( new Cell(1,false));
        groupCells.add( new GroupCell(cells,0));
        faithTrack = new FaithTrack(cells,groupCells);
        markers.put(player,0);
        faithTrack.setMarkers(markers);
        faithTrack.addNewPlayer(player);
    }


    @Test
    public void testAssignTileVictoryPointZeroPosition(){
        groupCells.get(0).assignTileVictoryPoints(faithTrack);
        assertEquals(faithTrack.getPosition(player),0);
    }
    @Test
    public void testAssignTileVictoryPointPosition(){
        markers.replace(player,1);
        faithTrack.setMarkers(markers);
        groupCells.get(0).assignTileVictoryPoints(faithTrack);
        assertEquals(faithTrack.getPosition(player),1);
    }
}
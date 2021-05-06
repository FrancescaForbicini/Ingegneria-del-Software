package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestFaithTrack {
    private Player player;
    private FaithTrack faithTrack;
    private Map<Player,Integer> markers;
    private ArrayList<Cell> cells;
    private ArrayList<GroupCell> groupCells;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        cells = new ArrayList<>();
        groupCells = new ArrayList<>();
        markers = new HashMap<>();
        faithTrack = new FaithTrack(cells,groupCells);
        markers.put(player,0);
        faithTrack.setMarkers(markers);
        faithTrack.addNewPlayer(player);
    }


    @Test
    public void move() {
        cells.add( new Cell(1,false));
        cells.add( new Cell(1,false));
        groupCells.add( new GroupCell(cells,5));
        faithTrack.move(player,1);
        assertEquals(faithTrack.getPosition(player),2);
    }

    @Test
    public void assignVictoryPointsNoPopeCell(){
        cells.add( new Cell(1,false));
        cells.add( new Cell(1,false));
        groupCells.add( new GroupCell(cells,5));
        faithTrack.assignVictoryPoints(player,0,1);
        assertEquals(player.getPersonalVictoryPoints(),2);
    }

    @Test
    public void assignVictoryPointsPopeCell(){
        cells.add( new Cell(1,true));
        cells.add( new Cell(1,true));
        cells.add( new Cell(1,true));
        groupCells.add( new GroupCell(cells,5));
        faithTrack.assignVictoryPoints(player,0,1);
        assertEquals(player.getPersonalVictoryPoints(),7);
    }

}
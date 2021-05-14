package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestFaithTrack {
    private Player player;
    private FaithTrack faithTrack;
    private Map<TurnTaker,Integer> markers;
    private ArrayList<Cell> cells;
    private ArrayList<Integer> cellIDs;
    private ArrayList<CellGroup> cellGroups;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        cells = new ArrayList<>();
        cellGroups = new ArrayList<>();
        markers = new HashMap<>();
        faithTrack = new FaithTrack(cells,cellGroups);
        markers.put(player,0);
        faithTrack.setMarkers(markers);
        faithTrack.addNewPlayer(player);
    }


    @Test
    public void move() {
        cells.add( new Cell(0,1,false));
        cellIDs.add(0);
        cells.add( new Cell(1,1,false));
        cellIDs.add(1);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.move(player,1);
        assertEquals(faithTrack.getPosition(player),2);
    }

    @Test
    public void assignVictoryPointsNoPopeCell(){
        cells.add( new Cell(0,1,false));
        cellIDs.add(0);
        cells.add( new Cell(1,1,false));
        cellIDs.add(1);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.assignVictoryPoints(player,0,1);
        assertEquals(player.getPersonalVictoryPoints(),2);
    }

    @Test
    public void assignVictoryPointsPopeCell(){
        cells.add( new Cell(0,1,true));
        cellIDs.add(0);
        cells.add( new Cell(1,1,true));
        cellIDs.add(1);
        cells.add( new Cell(2,1,true));
        cellIDs.add(2);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.assignVictoryPoints(player,0,1);
        assertEquals(player.getPersonalVictoryPoints(),7);
    }

}
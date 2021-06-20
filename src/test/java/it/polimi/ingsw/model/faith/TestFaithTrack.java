package it.polimi.ingsw.model.faith;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestFaithTrack {
    private Player player;
    private FaithTrack faithTrack;
    private Map<String,Integer> markers;
    private ArrayList<Cell> cells;
    private ArrayList<Integer> cellIDs;
    private ArrayList<CellGroup> cellGroups;
    private Game game;

    @Before
    public void setUp() throws Exception {
//        ArrayList<Player> players = new ArrayList<>();
//        player = new Player("username");
//        players.add(player);
//        game = mock(Game.class);
//        when(game.getPlayers()).thenReturn(players);
        cells = new ArrayList<>();
        cellGroups = new ArrayList<>();
        cellIDs = new ArrayList<>();
        markers = new HashMap<>();
        faithTrack = new FaithTrack(cells,cellGroups);
        game = Game.getInstance();
        game.addPlayer("username");
        player = game.getTurnTakers().get(0);
        markers.put(player.getUsername(),0);
        faithTrack.setMarkers(markers);
        faithTrack.addNewPlayer(player);
    }

    @After
    public void tearDown() {
        Game.getInstance().clean();
    }

    @Test
    public void move() {
        cells.add( new Cell(0,1,false));
        cellIDs.add(0);
        cells.add( new Cell(1,1,true));
        cellIDs.add(1);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.move(player,1);
        assertEquals(faithTrack.getPosition(player),1);
        assertEquals(7, player.getPersonalVictoryPoints());
    }

    @Test
    public void assignVictoryPointsNoPopeCell(){
        cells.add( new Cell(0,1,false));
        cellIDs.add(0);
        cells.add( new Cell(1,0,false));
        cells.add( new Cell(2,2,false));
        cellIDs.add(1);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.assignVictoryPoints(player,0,2);
        assertEquals(3, player.getPersonalVictoryPoints());
    }

    @Test
    public void assignVictoryPointsPopeCell(){
        cells.add( new Cell(0,1,false));
        cellIDs.add(0);
        cells.add( new Cell(1,1,false));
        cellIDs.add(1);
        cells.add( new Cell(2,1,true));
        cellIDs.add(2);
        cellGroups.add( new CellGroup(cellIDs,5));
        faithTrack.assignVictoryPoints(player,0,2);
        assertEquals(8, player.getPersonalVictoryPoints());
    }

}
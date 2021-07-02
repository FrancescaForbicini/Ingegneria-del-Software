package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortWarehouseTest {
    private Player player;
    private SortWarehouse sortWarehouse;
    private int oldDepot;
    private int newDepot;

    @Before
    public void setUp(){
        player = new Player("username");
        player.createPersonalBoard();
    }

    @Test
    public void switchResources(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Servants,2,3);
        oldDepot = 2;
        newDepot = 3;
        sortWarehouse = new SortWarehouse(oldDepot,newDepot);
        sortWarehouse.play(player);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Any);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Servants);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Shields);
    }

    @Test
    public void failToSwitchResources(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Servants,3,3);
        oldDepot = 2;
        newDepot = 3;
        sortWarehouse = new SortWarehouse(oldDepot,newDepot);
        sortWarehouse.play(player);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Any);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Shields);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Servants);
    }

}
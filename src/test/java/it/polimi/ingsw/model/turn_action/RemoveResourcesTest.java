package it.polimi.ingsw.model.turn_action;


import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RemoveResourcesTest {

    private Player player;
    private Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private Map<Integer,Integer> depotIDQuantity;
    private ResourceType resourceToRemove;

    @Before
    public void setUp(){
        player = new Player("username");
        player.loadFromSettings();
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox = new HashMap<>();
        depotIDQuantity = new HashMap<>();
    }

    @Test
    public void removeResources(){
        resourceToRemove = ResourceType.Shields;
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        RemoveResources.removeResources(resourceToRemove,player,inputFromWarehouse,inputFromStrongbox);
        assertEquals(player.getPersonalBoard().getResourceQuantity(ResourceType.Shields),1);
    }
    @Test
    public void failToRemoveResources(){
        //the player has only Shields, but has to remove Coins
        resourceToRemove = ResourceType.Coins;
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        RemoveResources.removeResources(resourceToRemove,player,inputFromWarehouse,inputFromStrongbox);
        assertEquals(player.getPersonalBoard().getResourceQuantity(ResourceType.Shields),3);
    }

    @Test
    public void areDepotsRight(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        assertTrue(RemoveResources.areDepotsRight(player,ResourceType.Shields,inputFromWarehouse));
    }

    @Test
    public void failAreDepotsRight(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(3,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        assertFalse(RemoveResources.areDepotsRight(player,ResourceType.Shields,inputFromWarehouse));
    }

    @Test
    public void arePlacedRight(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        inputFromStrongbox.put(ResourceType.Shields,1);
        assertTrue(RemoveResources.arePlacesRight(ResourceType.Shields,player,inputFromWarehouse,inputFromStrongbox));
    }

    @Test
    public void failArePlacedRight(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDQuantity.put(3,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        assertFalse(RemoveResources.arePlacesRight(ResourceType.Shields,player,inputFromWarehouse,inputFromStrongbox));
    }

    @Test
    public void isInputQuantityRight(){
        int totalAmountToTake = 3;
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        inputFromStrongbox.put(ResourceType.Shields,1);
        assertTrue(RemoveResources.isInputQuantityRight(ResourceType.Shields,totalAmountToTake,inputFromWarehouse,inputFromStrongbox));
    }

    @Test
    public void failIsInputQuantityRight(){
        int totalAmountToTake = 3;
        resourceToRemove = ResourceType.Shields;
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        assertFalse(RemoveResources.isInputQuantityRight(ResourceType.Shields,totalAmountToTake,inputFromWarehouse,inputFromStrongbox));
    }

}
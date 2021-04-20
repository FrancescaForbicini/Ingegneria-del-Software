package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequirementResourceTest {
    private RequirementResource requirementResource;
    private Player player;
    @Before
    public void setUp() throws Exception {
        requirementResource=new RequirementResource(3, ResourceType.Coins);
        player= new Player ("username");
    }

    @Test
    public void isSatisfied() throws NotEnoughResourcesException {
        //Player is empty
        //Quantity is not satisfied
        assertEquals((player.getResourceAmount(requirementResource.getResource())), 0);
        assertEquals(player.applyDiscount(ResourceType.Coins), 0);
        assertFalse(requirementResource.isSatisfied(player));

        //Player is not empty
        //Only Warehouse
        //Quantity is not satisfied
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins, 1);
        assertFalse(requirementResource.isSatisfied(player));
        //Resource from Warehouse is not satisfied
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields, 1);
        assertFalse(requirementResource.isSatisfied(player));
        //Quantity and Resource are satisfied without Discount and Strongbox
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins, 2);
        System.out.println(""+player.getPersonalBoard().getResourceAmount(ResourceType.Coins));
        assertTrue(requirementResource.isSatisfied(player));
        //Warehouse and Discount
        //Discount for the resource not required
        player.getPersonalBoard().removeResourceFromWarehouse(ResourceType.Coins, 1);
        player.addDiscount(ResourceType.Servants);
        assertFalse(requirementResource.isSatisfied(player));
        //Discount for the resource required, but quantity is not satisfied
        player.addDiscount(ResourceType.Coins);
        assertFalse(requirementResource.isSatisfied(player));
        //Discount for the resource required and quantity is satisfied
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins, 1);
        assertTrue(requirementResource.isSatisfied(player));
    }
}
package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.board.NotEnoughSpaceException;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequirementResourceTest {
    private RequirementResource requirementResource;
    private Player player;
    private ArrayList<WarehouseDepot> warehouseDepots;
    @Before
    public void setUp() throws Exception {
        requirementResource=new RequirementResource(2, ResourceType.Coins);
        player= new Player ("username");
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void isSatisfied() throws NotEnoughResourcesException {
        //Player is empty
        //Quantity is not satisfied
        assertEquals((player.getResourceAmount(requirementResource.getResource())), 0);
        assertFalse(requirementResource.isSatisfied(player));
        //Player is not empty
        //Only Warehouse
        //Quantity is not satisfied
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1, 2);
        System.out.println(""+player.getResourceAmount(ResourceType.Coins));
        assertFalse(requirementResource.isSatisfied(player));
        //Resource from Warehouse is not satisfied
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1, 1);
        System.out.println(""+player.getResourceAmount(ResourceType.Shields));
        assertFalse(requirementResource.isSatisfied(player));
        //Quantity and Resource are satisfied without Discount and Strongbox
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1, 2);
        System.out.println(""+player.getResourceAmount(ResourceType.Coins));
        assertTrue(requirementResource.isSatisfied(player));
        //Warehouse and Discount
        //Discount for the resource not required
        player.getPersonalBoard().getWarehouse().removeResource(2, 2);
        player.addDiscount(ResourceType.Servants,1);
        assertFalse(requirementResource.isSatisfied(player));
        //Discount for the resource required, but quantity is not satisfied
        player.addDiscount(ResourceType.Coins,1);
        assertFalse(requirementResource.isSatisfied(player));
        //Discount for the resource required and quantity is satisfied
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1,2);
        System.out.println(""+player.getResourceAmount(ResourceType.Coins));
        assertTrue(requirementResource.isSatisfied(player));
    }
}
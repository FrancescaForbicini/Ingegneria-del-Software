package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;
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
        player.loadFromSettings();
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void isSatisfiedPlayerIsEmpty() {
        //Player is empty
        assertEquals((player.getResourceQuantity(requirementResource.getResourceType())), 0);
        assertFalse(requirementResource.isSatisfied(player));
    }
    //Only Warehouse
    @Test
    public void isSatisfiedNotRightQuantity() {
        //Quantity is not satisfied
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1, 2);
        assertFalse(requirementResource.isSatisfied(player));
    }

    @Test
    public void isSatisfiedNotRightType() {
        //Resource from Warehouse is not satisfied
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields, 1, 1);
        assertFalse(requirementResource.isSatisfied(player));
    }

    @Test
    public void isSatisfiedWithoutDiscountStrongbox() {
        //Quantity and Resource are satisfied without Discount and Strongbox
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins, 2, 2);
        assertTrue(requirementResource.isSatisfied(player));
    }

    @Test
    public void isSatisfiedNotRightDiscount() {
        //Warehouse and Discount
        //Discount for the resource not required
        player.addDiscount(ResourceType.Servants, 1);
        assertFalse(requirementResource.isSatisfied(player));
    }
    @Test
    public void isSatisfiedRightDiscountNotRightQuantity() {
        //Discount for the resource required, but quantity is not satisfied
        player.addDiscount(ResourceType.Coins, 1);
        assertFalse(requirementResource.isSatisfied(player));
    }
    @Test
    public void isSatisfiedWithDiscount(){
        //Discount for the resource required and quantity is satisfied
        player.addDiscount(ResourceType.Coins, 1);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1,2);
        assertTrue(requirementResource.isSatisfied(player));
    }
}
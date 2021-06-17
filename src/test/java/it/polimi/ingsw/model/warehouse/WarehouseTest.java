package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.requirement.ResourceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseTest {
    private Warehouse warehouse;

    @Before
    public void setUp() {
        warehouse = new Warehouse();
    }
    @Test
    public void testAddAdditionalDepot() {
        //adding additional depot
        assertTrue(warehouse.getAdditionalDepots().isEmpty());
        warehouse.addAdditionalDepot(ResourceType.Coins,2);
        assertEquals(1,warehouse.getAdditionalDepots().size());
        assertEquals(ResourceType.Coins,warehouse.getDepot(4).get().getResourceType());
    }

    @Test
    public void testAddResourceToAdditionalDepot() {
        //adding to additional depot
        warehouse.addAdditionalDepot(ResourceType.Coins, 2);
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 4));
        assertEquals(1, warehouse.getQuantity(ResourceType.Coins));
    }

    @Test
    public void testAddResourceOfNewType() {
        //adding to normal w/out type already in
        assertTrue(warehouse.addResource(ResourceType.Stones, 1, 2));
        assertEquals(1, warehouse.getQuantity(ResourceType.Stones));
    }

    @Test
    public void testAddResourceOfAlreadyInTypeInDifferentDepot() {
        //adding to normal, but w/ type already in another
        assertTrue(warehouse.addResource(ResourceType.Stones, 1, 2));
        assertFalse(warehouse.addResource(ResourceType.Stones, 1, 1));
        assertEquals(1, warehouse.getQuantity(ResourceType.Stones));
    }

    @Test
    public void testAddResourceOfAlreadyInTypeInSameDepot() {
        //adding to normal w/ type already in that
        assertTrue(warehouse.addResource(ResourceType.Stones, 1, 2));
        assertTrue(warehouse.addResource(ResourceType.Stones,1,2));
        assertEquals(2,warehouse.getQuantity(ResourceType.Stones));
    }

    @Test
    public void testRemoveResource() {
        //adding to remove
        assertTrue(warehouse.addResource(ResourceType.Shields,1,2));
        assertEquals(1,warehouse.getQuantity(ResourceType.Shields));
        //removing
        assertTrue(warehouse.removeResource(1,2));
        assertEquals(0,warehouse.getQuantity(ResourceType.Shields));
        assertEquals(ResourceType.Any,warehouse.getDepot(2).get().getResourceType());
    }

    @Test
    public void getWarehouseDepots() {
        WarehouseDepot[] depotArray = warehouse.getWarehouseDepots().toArray(WarehouseDepot[]::new);
        assertEquals(warehouse.getDepot(1).get(),depotArray[0]);
        assertEquals(warehouse.getDepot(2).get(),depotArray[1]);
        assertEquals(warehouse.getDepot(3).get(),depotArray[2]);
    }

    @Test
    public void getAdditionalDepotsWithoutAny() {
        //w/out any additional depot
        assertTrue(warehouse.getAdditionalDepots().isEmpty());
    }

    @Test
    public void getAdditionalDepots() {
        //after adding one
        warehouse.addAdditionalDepot(ResourceType.Stones,2);
        assertEquals(warehouse.getDepot(4).get(),warehouse.getAdditionalDepots().get(0));
    }

    @Test
    public void switchResourceTooMuchQuantityIntoTooSmallDepot() {
        //switching too much quantity into small depot
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 1));
        assertTrue(warehouse.addResource(ResourceType.Shields, 2, 3));
        assertFalse(warehouse.switchResource(1, 3));
    }

    @Test////
    public void switchResourceWithEmptyDepot() {
        //switching into empty depot
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 2));
        assertTrue(warehouse.switchResource(2, 3));
        assertEquals(ResourceType.Coins,warehouse.getDepot(3).get().getResourceType());
    }

    @Test
    public void switchResourceRightQuantity() {
        //switching right quantity
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 1));
        assertTrue(warehouse.addResource(ResourceType.Shields, 1, 3));
        assertTrue(warehouse.switchResource(1, 3));
    }

    @Test
    public void switchResourceWithWrongAdditional() {
        //switching w/ wrong additional
        warehouse.addAdditionalDepot(ResourceType.Shields, 2);
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 2));
        assertTrue(warehouse.addResource(ResourceType.Shields, 2, 4));
        assertFalse(warehouse.switchResource(2,4));
    }

    @Test
    public void switchResourceWithRightAdditional() {
        //switching w/ right additional
        warehouse.addAdditionalDepot(ResourceType.Coins, 2);
        assertTrue(warehouse.addResource(ResourceType.Coins, 1, 2));
        assertTrue(warehouse.addResource(ResourceType.Coins, 2, 4));
        assertTrue(warehouse.switchResource(2,4));
    }

    @Test
    public void testGetQuantity(){
    }

    @Test
    public void testGetDepot(){
    }
}
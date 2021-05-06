package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.requirement.ResourceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseDepotTest {
    private WarehouseDepot depot;
    @Before
    public void setUp(){
        this.depot = new WarehouseDepot( ResourceType.Any,2,false, 1);
        assertEquals(depot.getLevel(), 2);
        assertEquals(depot.getResourceType(),ResourceType.Any);
    }

    @Test
    public void testAddResourceTooMuch() {
        //adding too much resource
        assertTrue(depot.isEmpty());
        int add = depot.getLevel() + 2;
        assertFalse(depot.addResource(ResourceType.Shields, add));
        assertTrue(depot.isEmpty());
    }

    @Test
    public void testAddResourceRightAmount() {
        //adding right amount
        int add = 1;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        assertFalse(depot.isEmpty());
    }

    @Test
    public void testAddResourceZero() {
        //adding 0
        int quantity = depot.getQuantity();
        int add = 0;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        assertEquals(depot.getQuantity(), quantity);
    }

    @Test
    public void testAddResourceWrongType() {
        //adding wrong type of resource
        int add = 1;
        depot.addResource(ResourceType.Stones, add);
        int quantity = depot.getQuantity();
        assertFalse(depot.addResource(ResourceType.Coins, add));
        assertEquals(depot.getQuantity(), quantity);
        assertEquals(depot.getResourceType(), ResourceType.Stones);
    }

    @Test
    public void testAddResourceFullDepot() {
        //adding to a full depot
        int add = depot.getLevel();
        depot.addResource(ResourceType.Stones, add);
        assertFalse(depot.addResource(depot.getResourceType(), 1));
        assertTrue(depot.isFull());
    }

    @Test
    public void testAddResourceNegativeAmount() {
        //adding negative amount
        int add = -2;
        assertFalse(depot.addResource(depot.getResourceType(), add));
    }

    @Test
    public void testRemoveResourceEmptyDepot() {
        //removing from empty depot
        assertTrue(depot.isEmpty());
        int rem = 1;
        assertFalse(depot.removeResource(rem));
        assertTrue(depot.isEmpty());
        assertEquals(depot.getResourceType(), ResourceType.Any);
    }

    @Test
    public void testRemoveResourceTooMuchResource() {
        //adding 1 and removing 2 (too much)
        int add = 1;
        int rem = 2;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        int quantity = depot.getQuantity();
        assertFalse(depot.isEmpty());
        assertFalse(depot.removeResource(rem));
        assertEquals(quantity, depot.getQuantity());
    }

    @Test
    public void testRemoveResourceNegativeAmount() {
        //removing negative quantity
        int rem = -1;
        assertFalse(depot.removeResource(rem));
    }

    @Test
    public void testRemoveResourceRightAmount() {
        //removing right amount
        int add = 1;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        int rem = 1;
        assertTrue(depot.removeResource(rem));
        assertTrue(depot.isEmpty());
        assertEquals(depot.getResourceType(), ResourceType.Any);
    }
}
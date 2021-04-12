package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.requirement.ResourceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseDepotTest {
    private WarehouseDepot depot;
    @Before
    public void setUp(){
        this.depot = new WarehouseDepot(2, ResourceType.Any,false);
        assertEquals(depot.getLevel(), 2);
        assertEquals(depot.getResourceType(),ResourceType.Any);
    }

    @Test
    public void testAddResource() {
        assertTrue(depot.isEmpty());
        assertTrue(depot.isSpaceAvailable());
        int add;
        int quantity = depot.getQuantity();
        //adding too much quantity
        add = depot.getLevel() + 2;
        assertFalse(depot.addResource(ResourceType.Shields, add));
        assertTrue(depot.isEmpty());
        //adding right amount
        add = 1;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        assertFalse(depot.isEmpty());
        //adding 0
        add = 0;
        assertTrue(depot.addResource(depot.getResourceType(), add));
        assertEquals(depot.getQuantity(), quantity);
        //adding wrong type of resource
        add = 1;
        assertFalse(depot.addResource(ResourceType.Stones, add));
        assertEquals(depot.getQuantity(), quantity);
        assertEquals(depot.getResourceType(), ResourceType.Coins);
        //filling the depot
        add = depot.getAvailableSpace();
        assertTrue(depot.addResource(depot.getResourceType(), add));
        assertFalse(depot.isSpaceAvailable());
        //adding to a full depot
        add = 2;
        assertFalse(depot.addResource(depot.getResourceType(), add));
        assertFalse(depot.isSpaceAvailable());
        //adding negative quantity
        add = -2;
        assertFalse(depot.addResource(depot.getResourceType(), add));
    }

    @Test
    public void testRemoveResource() {
        assertTrue(depot.isEmpty());
        assertTrue(depot.isSpaceAvailable());
        int add;
        int rem;
        int quantity = depot.getQuantity();
        //removing from empty depot
        rem = 1;
        assertFalse(depot.removeResource(rem));
        assertTrue(depot.isEmpty());
        assertEquals(depot.getResourceType(), ResourceType.Any);
        //adding 1 and removing 2 (too much)
        add = 1;
        assertTrue(depot.addResource(ResourceType.Coins, add));
        assertFalse(depot.isEmpty());
        rem = 2;
        assertFalse(depot.removeResource(rem));
        assertEquals(quantity + add, depot.getQuantity());
        //removing negative quantity
        rem = -1;
        assertFalse(depot.removeResource(rem));
        //removing right amount
        rem = 1;
        assertTrue(depot.removeResource(rem));
        assertTrue(depot.isEmpty());
        assertEquals(depot.getResourceType(), ResourceType.Any);
    }
}
package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.requirement.ResourceType;
import org.junit.Test;

import static org.junit.Assert.*;

public class WarehouseTest {
    private final Warehouse warehouse = new Warehouse();
    private final WarehouseDepot additional = new WarehouseDepot(2, ResourceType.Coins,true);
    private WarehouseDepot normal;

    @Test
    public void testAddAdditionalDepot() {
        //adding additional depot
        assertTrue(warehouse.getAdditionalDepots().isEmpty());
        assertTrue(warehouse.addAdditionalDepot(additional));
        assertEquals(warehouse.getAdditionalDepots().size(),1);
        //adding normal depot
        normal = new WarehouseDepot(2,ResourceType.Servants,false);
        assertFalse(warehouse.addAdditionalDepot(normal));
        assertEquals(warehouse.getAdditionalDepots().size(),1);
    }

    @Test
    public void testAddResource() {
        //adding to additional depot
        assertTrue(warehouse.addResource(ResourceType.Coins,1,additional));
        assertEquals(warehouse.getQuantity(ResourceType.Coins),1);
        //adding to normal w/out type already in
        normal = warehouse.getDepot(2);
        assertTrue(warehouse.addResource(ResourceType.Stones,1,normal));
        assertEquals(warehouse.getQuantity(ResourceType.Stones),1);
        //adding to another normal, but w/ type already in
        normal = warehouse.getDepot(1);
        assertFalse(warehouse.addResource(ResourceType.Stones,1,normal));
        assertEquals(warehouse.getQuantity(ResourceType.Stones),1);
        //adding to first one w/ type already in
        normal = warehouse.getDepot(2);
        assertTrue(warehouse.addResource(ResourceType.Stones,1,normal));
        assertEquals(warehouse.getQuantity(ResourceType.Stones),2);
    }

    @Test
    public void testRemoveResource() {
        //adding to remove
        normal = warehouse.getDepot(2);
        assertTrue(warehouse.addResource(ResourceType.Shields,1,normal));
        assertEquals(warehouse.getQuantity(ResourceType.Shields),1);
        //removing
        normal = warehouse.getDepot(2);
        assertTrue(warehouse.removeResource(1,normal));
        assertEquals(warehouse.getQuantity(ResourceType.Shields),0);
        assertEquals(warehouse.getDepot(2).getResourceType(),ResourceType.Any);
    }

    @Test
    public void getWarehouseDepots() {
        WarehouseDepot[] depotArray = warehouse.getWarehouseDepots().toArray(WarehouseDepot[]::new);
        assertEquals(depotArray[0],new WarehouseDepot(1,ResourceType.Any,false));
        assertEquals(depotArray[1],new WarehouseDepot(2,ResourceType.Any,false));
        assertEquals(depotArray[2],new WarehouseDepot(3,ResourceType.Any,false));
    }

    @Test
    public void getAdditionalDepots() {
        //w/out any additional depot
        assertTrue(warehouse.getAdditionalDepots().isEmpty());
        //after adding one
        assertTrue(warehouse.addAdditionalDepot(additional));
        for(int i=0;i<warehouse.getAdditionalDepots().size();i++) {
            assertEquals(warehouse.getAdditionalDepots().get(i),additional);
        }
    }

    @Test
    public void switchResource() {
        WarehouseDepot depot1 = warehouse.getDepot(1);
        WarehouseDepot depot2 = warehouse.getDepot(2);
        assertTrue(warehouse.addResource(ResourceType.Coins,1,depot2));
        WarehouseDepot depot3 = warehouse.getDepot(3);
        assertTrue(warehouse.addResource(ResourceType.Shields,2,depot3));
        //switching too much quantity into small depot
        assertFalse(warehouse.switchResource(depot3,depot1));
        //switching right quantity
        assertTrue(warehouse.switchResource(depot2,depot1));
        //switching w/ additional
        assertTrue(warehouse.addResource(ResourceType.Coins,1,additional));
        assertTrue(warehouse.switchResource(depot2,additional));
    }

    @Test
    public void testGetQuantity(){
    }

    @Test
    public void testGetDepot(){
        //asking for level -1 depot
        assertEquals(warehouse.getDepot(-1),warehouse.getDepot(1));
        //asking for level 4 depot
        assertEquals(warehouse.getDepot(4),warehouse.getDepot(3));
    }
}
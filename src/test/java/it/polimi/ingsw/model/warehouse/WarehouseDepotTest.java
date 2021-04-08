package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.ResourceType;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class WarehouseDepotTest {
    private WarehouseDepot depot;
    private final Random rnd = new Random();
    private final int num = 2;
    @Before
    public void setUp(){
        this.depot = new WarehouseDepot(num,ResourceType.Any,false);
        assertEquals(depot.getLevel(), num);
        System.out.println("\n\nSetup \nLevel: " + depot.getLevel());
        assertEquals(depot.getResourceType(),ResourceType.Any);
        System.out.println("Type: " + depot.getResourceType() + "\n");
    }

    @Test
    public void testAddResource() {
        System.out.println("addResource");
        assertTrue(depot.isEmpty());
        assertTrue(depot.isSpaceAvailable());
        int add;
        for (int i=0;i<8;i++) {
            int quantity = depot.getQuantity();
            System.out.println("Initial quantity: " + quantity);
            switch (i){
                case 0:
                    add = depot.getLevel()+2;
                    System.out.println("Adding " + add);
                    assertFalse(depot.addResource(ResourceType.Shields,add));
                    System.out.println("Too much");
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertTrue(depot.isEmpty());
                    break;
                case 1:
                    add = 1;
                    System.out.println("Adding " + add);
                    assertTrue(depot.addResource(ResourceType.Coins,add));
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertFalse(depot.isEmpty());
                    break;
                case 2:
                    add = 0;
                    System.out.println("Adding " + add);
                    assertFalse(depot.addResource(depot.getResourceType(),add));
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertEquals(depot.getQuantity(),quantity);
                    break;
                case 3:
                    add = 1;
                    System.out.println("Adding " + add);
                    assertFalse(depot.addResource(ResourceType.Stones,add));
                    System.out.println("Type: " + ResourceType.Stones);
                    System.out.println("Wrong type");
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertEquals(depot.getQuantity(),quantity);
                    assertEquals(depot.getResourceType(),ResourceType.Coins);
                    break;
                case 4:
                    add = depot.getAvailableSpace();
                    System.out.println("Adding " + add);
                    assertTrue(depot.addResource(depot.getResourceType(),add));
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertFalse(depot.isSpaceAvailable());
                    break;
                case 5:
                    add = 2;
                    System.out.println("Adding " + add);
                    assertFalse(depot.addResource(depot.getResourceType(),add));
                    System.out.println("Depot full");
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertFalse(depot.isSpaceAvailable());
                    break;
                case 6:
                    add = -2;
                    System.out.println("Adding " + add);
                    System.out.println("Bad input");
                    assertFalse(depot.addResource(depot.getResourceType(),add));
                    break;
                default:
                    add = rnd.nextInt(depot.getLevel()+2);
                    System.out.println("Adding " + add);
                    ResourceType[] types = ResourceType.values();
                    ResourceType type = types[rnd.nextInt(types.length-1)];
                    System.out.println("Type: " + type);
                    if(!depot.addResource(type,add)){
                        System.out.println("Too much, depot full or wrong type");
                    }
                    System.out.println("Final quantity: " + depot.getQuantity());
                    break;
            }
            System.out.println("Type: " + depot.getResourceType() + "\n");
        }
    }

    @Test
    public void testRemoveResource() {
        System.out.println("removeResource");
        assertTrue(depot.isEmpty());
        assertTrue(depot.isSpaceAvailable());
        int add;
        int rem;
        for (int i = 0; i < 8; i++) {
            int quantity = depot.getQuantity();
            System.out.println("Initial quantity: " + quantity);
            switch (i) {
                case 0:
                    rem = 1;
                    System.out.println("Removing " + rem);
                    assertFalse(depot.removeResource(rem));
                    System.out.println("Depot empty");
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertTrue(depot.isEmpty());
                    assertEquals(depot.getResourceType(),ResourceType.Any);
                    break;
                case 1:
                    add = 1;
                    System.out.println("Adding " + add);
                    assertTrue(depot.addResource(ResourceType.Coins, add));
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertFalse(depot.isEmpty());
                    System.out.println("Type: " + depot.getResourceType() + "\n");
                    rem = 2;
                    System.out.println("Removing " + rem);
                    assertFalse(depot.removeResource(rem));
                    System.out.println("Too much");
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertEquals(quantity + add, depot.getQuantity());
                    break;
                case 2:
                    rem = -1;
                    System.out.println("Removing " + rem);
                    System.out.println("Bad input");
                    assertFalse(depot.removeResource(rem));
                    break;
                case 3:
                    rem = 1;
                    System.out.println("Removing " + rem);
                    assertTrue(depot.removeResource(rem));
                    System.out.println("Final quantity: " + depot.getQuantity());
                    assertTrue(depot.isEmpty());
                    assertEquals(depot.getResourceType(),ResourceType.Any);
                    break;
                default:
                    add = 1;
                    rem = 1;
                    if(rnd.nextBoolean()){
                        System.out.println("Adding " + add);
                        depot.addResource(ResourceType.Servants,add);
                        System.out.println("Final quantity: " + depot.getQuantity());
                    }
                    else{
                        System.out.println("Removing " + rem);
                        depot.removeResource(rem);
                        System.out.println("Final quantity: " + depot.getQuantity());
                    }
            }
            System.out.println("Type: " + depot.getResourceType() + "\n");
        }
    }
}
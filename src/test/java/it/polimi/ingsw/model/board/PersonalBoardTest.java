package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PersonalBoardTest {
    PersonalBoard personalBoard;
    @Before
    public void setUp() {
        personalBoard = new PersonalBoard();
    }

    @Test
    public void testAddAdditionalRule() {
        assertEquals(0, personalBoard.getAdditionalRules().size());
        personalBoard.addAdditionalRule(new TradingRule( null, null,1));
        assertEquals(1, personalBoard.getAdditionalRules().size());
        personalBoard.addAdditionalRule(new TradingRule( null, null,1));
        assertEquals(2, personalBoard.getAdditionalRules().size());
    }

    @Test
    public void testAddAdditionalDepot(){
        assertEquals(0,personalBoard.getWarehouse().getAdditionalDepots().size());
        WarehouseDepot additionalDepot = new WarehouseDepot(ResourceType.Coins,2, true, 0);
        personalBoard.addAdditionalDepot(ResourceType.Coins, 0);
        assertEquals(1,personalBoard.getWarehouse().getAdditionalDepots().size());
        personalBoard.addAdditionalDepot(ResourceType.Servants, 1);
        assertEquals(2,personalBoard.getWarehouse().getAdditionalDepots().size());
    }

    @Test
    public void testAddDevelopmentCard(){
        //Creation of the DevelopmentCard to add
        Map<ResourceType,Integer> input = new HashMap<>();
        Map<ResourceType,Integer> output = new HashMap<>();
        TradingRule tradingRule=new TradingRule(input,output,2);
        Collection<Requirement> requirements= new ArrayList<>();
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Any,1);
        requirements.add(new RequirementResource(2,ResourceType.Any));
        DevelopmentCard developmentCard = new DevelopmentCard(requirements,DevelopmentColor.Yellow,1,2,tradingRule);
        personalBoard.addDevelopmentCard(developmentCard,1);
        assertEquals(personalBoard.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }
    @Test
    public void testAddWrongDevelopmentCard(){
        //Creation of the development card to add
        Map<ResourceType,Integer> input = new HashMap<>();
        Map<ResourceType,Integer> output = new HashMap<>();
        TradingRule tradingRule=new TradingRule(input,output,2);
        Collection<Requirement> requirements= new ArrayList<>();
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Any,1);
        requirements.add(new RequirementResource(2,ResourceType.Any));
        DevelopmentCard developmentCard = new DevelopmentCard(requirements,DevelopmentColor.Yellow,2,2,tradingRule);
        personalBoard.addDevelopmentCard(developmentCard,1);
        assertEquals(personalBoard.getDevelopmentQuantity(DevelopmentColor.Yellow),0);

    }
    @Test
    public void testAddResourceToStrongbox() {
        assertEquals(0, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 10);
        assertEquals(10, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 20);
        assertEquals(30, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
    }

    @Test
    public void testRemoveResourceToStrongbox() throws NotEnoughResourcesException {
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 10);
        assertEquals(10, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
        personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 5);
        assertEquals(5, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
        personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 5);
        assertEquals(0, personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins));
    }

    @Test
    public void testRemoveTooManyResourcesToStrongBox(){
        //Not enough resource in the strongbox to be removed
        personalBoard.addResourceToStrongbox(ResourceType.Coins,2);
        assertEquals(personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins),2);
        try {
            personalBoard.removeResourceFromStrongbox(ResourceType.Coins,3);
        } catch (NotEnoughResourcesException ignored) {
        }
        //player can't remove the resources from the strongbox
        assertEquals(personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins),2);
    }
    @Test
    public void testRemoveResourceToStrongboxEmpty() {
        //Not enough resource in the strongbox to be removed
        try {
            personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 10);
        } catch (NotEnoughResourcesException ignored) {
        }
        assertEquals(personalBoard.getResourceQuantityFromStrongbox(ResourceType.Coins),0);
    }

    @Test
    public void testAddResourceToWarehouse() {
        personalBoard.addResourceToWarehouse(ResourceType.Shields,1,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),1);
    }
    @Test
    public void testAddTooManyResourcesToWarehouse(){
        //wants to add too many resources in the warehouse
        personalBoard.addResourceToWarehouse(ResourceType.Shields,2,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),0);
    }
    @Test
    public void testAddDifferentTypeResourceToWarehouse(){
        //wants to add different resources in the same depot
        personalBoard.addResourceToWarehouse(ResourceType.Shields,1,1);
        personalBoard.addResourceToWarehouse(ResourceType.Coins,1,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Coins),0);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),1);
    }

    @Test
    public void testRemoveRightAmountResourceToWarehouse() {
        //removes the right amount of resources from the warehouse
        personalBoard.addResourceToWarehouse(ResourceType.Shields,1,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),1);
        personalBoard.removeResourceFromWarehouse(ResourceType.Shields,1,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),0);
    }

    @Test
    public void testRemoveTooManyResourcesToWarehouse(){
        //removed too many resources from the warehouse
        personalBoard.addResourceToWarehouse(ResourceType.Shields,1,1);
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),1);
        personalBoard.removeResourceFromWarehouse(ResourceType.Shields,2,1);
        //player can't remove the resources, because he wants to remove too many resources
        assertEquals(personalBoard.getResourceQuantity(ResourceType.Shields),1);
    }

    @Test
    public void isWarehouseFull() {
        personalBoard.addResourceToWarehouse(ResourceType.Shields,1,1);
        personalBoard.addResourceToWarehouse(ResourceType.Coins,2,2);
        personalBoard.addResourceToWarehouse(ResourceType.Servants,3,3);
        assertTrue(personalBoard.isWarehouseFull());
    }

}
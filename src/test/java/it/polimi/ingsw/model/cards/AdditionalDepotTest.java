package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.board.NotEnoughSpaceException;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AdditionalDepotTest {
    private Player player;
    private ResourceType type;
    private final Collection<Requirement> requirements = new ArrayList<>();
    private WarehouseDepot warehouseDepot;
    private AdditionalDepot additionalDepot;
    int victoryPoints;
    private int count_additionalDepot;
    private ArrayList<WarehouseDepot> warehouseDepots;
    @Before
    public void setUp() {
        player = new Player("username");
        type = ResourceType.Any;
        requirements.add(new RequirementResource(2,ResourceType.Coins));
        warehouseDepot = new WarehouseDepot(2,type,false);
        additionalDepot = new AdditionalDepot(2, requirements, warehouseDepot);
        victoryPoints = 0;
        count_additionalDepot= 0;
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void testActivateEmptyPlayer() {
        //Player is empty
        victoryPoints = player.getPersonalVictoryPoints();
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().noneMatch(warehouseDepot1 -> warehouseDepot1 == warehouseDepot));
    }

    @Test
    public void testActivateNoRequirements() {
        //PLayer has not the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins, 1, 2);
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().noneMatch(warehouseDepot1 -> warehouseDepot1 == warehouseDepot));
    }

    @Test
    public void testActivatePlayerHasRequirements() throws NoEligiblePlayerException {
        //Player has the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins, 2,2);
        additionalDepot.activate(player);
        victoryPoints += additionalDepot.getVictoryPoints();
        count_additionalDepot += player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count();
        assertEquals(count_additionalDepot, player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().count());
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().anyMatch(additionalDepot1-> additionalDepot1.equals(additionalDepot)));
    }
    @Test
    public void testActivateAdditionalNoRequirements() {

        //Player wants to add the same additional depot and hasn't the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        count_additionalDepot += player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count();
        player.getPersonalBoard().getWarehouse().removeResource(2, 2);
        assertEquals(player.getPersonalVictoryPoints(), victoryPoints);
        assertEquals(count_additionalDepot, player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count());
    }
    @Test
    public void testActivateAdditionalHasRequirements(){
        //Player wants to add the same additional depot and he has the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        count_additionalDepot+=player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,2,2);
        try{
            additionalDepot.activate(player);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        victoryPoints += player.getPersonalVictoryPoints();
        count_additionalDepot+=player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count();
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(count_additionalDepot,player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().map(warehouseDepot1 -> warehouseDepot1 == warehouseDepot).count());
        assertTrue(player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().anyMatch(additionalDepot1 -> additionalDepot1.equals(additionalDepot)));
    }

}
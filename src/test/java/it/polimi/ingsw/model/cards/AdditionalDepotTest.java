package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;

public class AdditionalDepotTest {
    private Player player;
    private ResourceType type;
    private final Collection<Requirement> requirements = new ArrayList<>();
    private AdditionalDepot additionalDepot;
    private AdditionalDepot additionalDepotSecondary;
    int victoryPoints;
    private int count_additionalDepot;
    private ArrayList<WarehouseDepot> warehouseDepots;
    @Before
    public void setUp() {
        player = new Player("username");
        player.createPersonalBoard();
        type = ResourceType.Any;
        requirements.add(new RequirementResource(2,ResourceType.Coins));
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
        additionalDepot = new AdditionalDepot(requirements, 2, ResourceType.Coins, 2,"");
        additionalDepotSecondary = new AdditionalDepot(requirements, 2, ResourceType.Servants, 2,"");
        victoryPoints = 0;
        count_additionalDepot= 0;

    }
    @Test
    public void testActivateNoRequirements() {
        //PLayer has not the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins, 1, 2);
        additionalDepot.activate(player);
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getWarehouse().getAdditionalDepots().stream().noneMatch(warehouseDepot1 -> warehouseDepot1 == warehouseDepots.get(0)));
    }

    @Test
    public void testActivatePlayerHasRequirements(){
        //Player has the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        count_additionalDepot =player.getPersonalBoard().getWarehouse().getAdditionalDepots().size();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins, 2,2);
        additionalDepot.activate(player);
        victoryPoints += additionalDepot.getVictoryPoints();
        count_additionalDepot += 1;
        assertEquals(count_additionalDepot, player.getPersonalBoard().getWarehouse().getAdditionalDepots().size());
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
    }
}
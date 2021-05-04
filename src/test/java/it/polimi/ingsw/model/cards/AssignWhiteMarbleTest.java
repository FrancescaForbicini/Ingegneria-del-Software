package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.NotEnoughSpaceException;
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

public class AssignWhiteMarbleTest {
    private Player player;
    private ResourceType type;
    private final Collection<Requirement> requirement = new ArrayList<>();
    private AssignWhiteMarble assignWhiteMarble;
    int victoryPoints;
    private ArrayList<WarehouseDepot> warehouseDepots;
    @Before
    public void setUp() {
        player = new Player("username");
        type = ResourceType.Any;
        requirement.add(new RequirementResource(2,ResourceType.Shields));
        assignWhiteMarble = new AssignWhiteMarble(2,type,requirement);
        victoryPoints = 0;
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void testActivate() {
        //Player is empty
        victoryPoints=player.getPersonalVictoryPoints();
        try{
            assignWhiteMarble.activate(player);
        }catch (NoEligiblePlayerException e){
            e.printStackTrace();
        }
        assertTrue(player.getWhiteMarbleResource().stream().noneMatch(resourceType -> resourceType == type));
        assertEquals(victoryPoints,player.getPersonalVictoryPoints());

        //Player has no requirement to activate the "Assign White Marble"
        victoryPoints=player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1,2);
        try{
            assignWhiteMarble.activate(player);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        assertTrue(player.getWhiteMarbleResource().stream().noneMatch(resourceType -> resourceType == type));
        assertEquals(victoryPoints,player.getPersonalVictoryPoints());

        //Player has the requirement to activate the "Assign White Marble"
        victoryPoints=player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1,2);
        try{
            assignWhiteMarble.activate(player);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
            assertTrue(player.getWhiteMarbleResource().stream().noneMatch(resourceType -> resourceType == type));
        }
        assertTrue(player.getWhiteMarbleResource().stream().anyMatch(resourceType -> resourceType == type));
        victoryPoints+= assignWhiteMarble.getVictoryPoints();
        assertEquals(victoryPoints,player.getPersonalVictoryPoints());
    }
}
package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TakeFromMarketTest {
    private Collection<MarbleType> marbles;
    private Player player;
    private TurnAction turnAction;
    private TakeFromMarket takeFromMarket;
    private Map<ResourceType,Integer> resources;
    private ArrayList<ResourceType> resourceChosen;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        turnAction = new TakeFromMarket(null,0,null);
        takeFromMarket = (TakeFromMarket) turnAction;
        marbles = new ArrayList<>();
        resources = new HashMap<>();
        resourceChosen = new ArrayList<>();
        marbles.add(MarbleType.White);
        marbles.add(MarbleType.Blue);
        resources.put(ResourceType.Shields,1);
        takeFromMarket.setResourceToDepot(resources);

    }

    @Test
    public void testConvertMarbleNoActivateMarble() {
        //the player doesn't have white marble conversion
        takeFromMarket.convertMarble(player,marbles);
        assertEquals(player.getActiveWhiteConversions().size(),0);
        //all resource have to match with shields because white marble is null
        assertTrue(takeFromMarket.getResources().stream().allMatch(resourceType -> resourceType.equals(ResourceType.Shields)));
    }

    @Test
    public void testConvertMarbleActivateOneMarbleOneWhite(){
        player.getActiveWhiteConversions().add(ResourceType.Coins);
        takeFromMarket.convertMarble(player,marbles);
        assertTrue(takeFromMarket.getResources().stream().anyMatch(resourceType -> resourceType.equals(ResourceType.Shields)));
        assertTrue(takeFromMarket.getResources().stream().anyMatch((resourceType -> resourceType.equals(ResourceType.Coins))));
    }
    @Test
    public void testConvertMarbleActivateOneMarbleTwoWhite(){
        marbles.add(MarbleType.White);
        player.getActiveWhiteConversions().add(ResourceType.Coins);
        takeFromMarket.convertMarble(player,marbles);
        assertTrue(takeFromMarket.getResources().stream().anyMatch(resourceType -> resourceType.equals(ResourceType.Shields)));
        assertTrue(takeFromMarket.getResources().stream().anyMatch((resourceType -> resourceType.equals(ResourceType.Coins))));
        assertEquals(takeFromMarket.getResources().stream().filter(resourceType -> resourceType.equals(ResourceType.Coins)).count(),2);
    }

    @Test
    public void testConvertMarbleActivateTwoMarble(){
        marbles.add(MarbleType.White);
        resourceChosen.add(ResourceType.Coins);
        resourceChosen.add(ResourceType.Servants);
        takeFromMarket.setResourceAny(resourceChosen);
        player.getActiveWhiteConversions().add(ResourceType.Coins);
        player.getActiveWhiteConversions().add(ResourceType.Servants);
        takeFromMarket.convertMarble(player,marbles);
        assertTrue(takeFromMarket.getResources().stream().anyMatch(resourceType -> resourceType.equals(ResourceType.Shields)));
        assertTrue(takeFromMarket.getResources().stream().anyMatch((resourceType -> resourceType.equals(ResourceType.Coins))));
        assertTrue(takeFromMarket.getResources().stream().anyMatch((resourceType -> resourceType.equals(ResourceType.Servants))));
    }
}
package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        turnAction = new TakeFromMarket(null,0,null,null);
        takeFromMarket = (TakeFromMarket) turnAction;
        marbles = new ArrayList<>();
        resources = new HashMap<>();
        resourceChosen = new ArrayList<>();
        marbles.add(MarbleType.White);
        marbles.add(MarbleType.Blue);
        resources.put(ResourceType.Shields,1);

    }


    /*
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
    }*/
}
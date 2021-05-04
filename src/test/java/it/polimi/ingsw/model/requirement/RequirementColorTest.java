package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.RequirementColor;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequirementColorTest {
    private RequirementColor requirementColor;
    private Player player;
    private Player playerSecond;
    private final int points=1;
    private final Map<ResourceType,Integer> input = new HashMap<>();
    private final Map<ResourceType,Integer> output = new HashMap<>();
    private final TradingRule tradingRule=new TradingRule(points,input,output);
    private Collection<Requirement> requirements= new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        player= new Player("username");
        playerSecond = new Player ("password");
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Any,1);
        requirements.add(new RequirementResource(1,ResourceType.Any));
    }

    @Test
    public void testIsSatisfied() {
        //First Case: test the quantity
        requirementColor= new RequirementColor(0,2, DevelopmentColor.Yellow);
        //Player is empty
        //Quantity is not satisfied
        assertEquals(player.getDevelopmentQuantity(requirementColor.getColor()), 0);
        assertFalse(requirementColor.isSatisfied(player));
        //Player is not empty
        //Color is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        //Level is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 1, points, tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        //Quantity is satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 2, points, tradingRule));
        assertTrue(requirementColor.isSatisfied(player));

        //Second Case: test the level
        requirementColor = new RequirementColor(2,2, DevelopmentColor.Yellow);
        playerSecond.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule));
        playerSecond.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule));
        playerSecond.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 2, points, tradingRule));
        //Quantity ko, color ko, level ok
        playerSecond.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule));
        assertFalse(requirementColor.isSatisfied(playerSecond));
        // Quantity ok, level ko
        playerSecond.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 2, points, tradingRule));
        assertTrue(requirementColor.isSatisfied(playerSecond));
    }

}
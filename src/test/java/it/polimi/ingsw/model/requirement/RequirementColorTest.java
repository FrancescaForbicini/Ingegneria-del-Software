package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.cards.DevelopmentCard;
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
    private final int points=1;
    private final Map<ResourceType,Integer> input = new HashMap<>();
    private final Map<ResourceType,Integer> output = new HashMap<>();
    private final TradingRule tradingRule=new TradingRule(input,output,2);
    private final Collection<Requirement> requirements= new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        player= new Player("username");
        player.loadFromSettings();
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Any,1);
        requirements.add(new RequirementResource(2,ResourceType.Any));
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Any,2,2);
    }

    @Test
    public void testIsSatisfiedNotRightColor(){
        //Color is not satisfied
        requirementColor = new RequirementColor(2,1,DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),1);
        assertFalse(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedNotRightLevel() {
        //Level is not satisfied
        requirementColor = new RequirementColor(2,1,DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 1, points, tradingRule),1);
        assertFalse(requirementColor.isSatisfied(player));
    }
    @Test
    public void testIsSatisfiedRightLevelColorQuantity() {
        //Quantity is satisfied
        requirementColor = new RequirementColor(2,1,DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow,1, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow,2, points, tradingRule),1);
        assertTrue(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedNotRightLevelColorQuantity() {
        requirementColor = new RequirementColor(2, 2, DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),2);
        assertFalse(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedRightLevelNotRightQuantityColor() {
        //Quantity ko, color ko, level ok
        requirementColor = new RequirementColor(2, 2, DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 2, points, tradingRule),1);
        assertFalse(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedRightColorNotRightLevelQuantity() {
        // Quantity ok, level ko
        requirementColor = new RequirementColor(2, 2, DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 1, points, tradingRule),1);
        assertFalse(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedRightQuantityNotRightLevelColor() {
        // Quantity ok, level ko, color ko
        requirementColor = new RequirementColor(2, 2, DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),2);
        assertFalse(requirementColor.isSatisfied(player));
    }

    @Test
    public void testIsSatisfiedRightQuantityLevelColor() {
        // Quantity ok, level ok, color ok
        requirementColor = new RequirementColor(2, 2, DevelopmentColor.Yellow);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Blue, 1, points, tradingRule),2);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 2, points, tradingRule),1);
        player.addDevelopmentCard(new DevelopmentCard(requirements, DevelopmentColor.Yellow, 2, points, tradingRule),2);
        assertTrue(requirementColor.isSatisfied(player));
    }
}

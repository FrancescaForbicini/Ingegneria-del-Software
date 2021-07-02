package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
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

public class AdditionalTradingRuleTest {
        private Player player;
        private ResourceType type;
        private final Map<ResourceType,Integer> input = new HashMap<>();
        private final Map<ResourceType,Integer> output = new HashMap<>();
        private final Collection<Requirement> requirements = new ArrayList<>();
        private TradingRule tradingRule;
        private AdditionalTradingRule additionalTradingRule;
        int victoryPoints;

        @Before
        public void setUp() {
            player = new Player("username");
            player.createPersonalBoard();
            type = ResourceType.Any;
            input.put(ResourceType.Shields,2);
            output.put(ResourceType.Any,1);
            requirements.add(new RequirementResource(2,ResourceType.Coins));
            tradingRule= new TradingRule(input,output,2);
            additionalTradingRule = new AdditionalTradingRule(2,requirements, tradingRule,"");
            victoryPoints = 0;
        }

    @Test
    public void testActivateNoRequirements() {
        //PLayer has not the requirements to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins, 1, 2);
        additionalTradingRule.activate(player);
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().noneMatch(tradingRule1 -> tradingRule1 == tradingRule));
    }

    @Test
    public void testActivateRightRequirements(){
        //Player has the requirements to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,2,2);
        additionalTradingRule.activate(player);
        victoryPoints+= additionalTradingRule.getVictoryPoints();
        assertEquals(victoryPoints,player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule1 -> tradingRule1 == tradingRule));
    }
}
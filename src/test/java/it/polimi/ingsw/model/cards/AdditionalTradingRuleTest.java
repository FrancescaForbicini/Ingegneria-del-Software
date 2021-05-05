package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.NotEnoughSpaceException;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AdditionalTradingRuleTest {
        private Player player;
        private ResourceType type;
        private final Map<ResourceType,Integer> input = new HashMap<>();
        private final Map<ResourceType,Integer> output = new HashMap<>();
        private final Collection<Requirement> requirements = new ArrayList<>();
        private TradingRule tradingRule;
        private AdditionalTradingRule additionalTradingRule;
        int victoryPoints;
        private ArrayList<WarehouseDepot> warehouseDepots;
        @Before
        public void setUp() {
            player = new Player("username");
            type = ResourceType.Any;
            input.put(ResourceType.Shields,2);
            output.put(ResourceType.Any,1);
            requirements.add(new RequirementResource(2,ResourceType.Coins));
            tradingRule= new TradingRule(2,input,output,2);
            additionalTradingRule = new AdditionalTradingRule(2,requirements, tradingRule);
            victoryPoints = 0;
            warehouseDepots = new ArrayList<>();
            warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
            warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
        }

    @Test
    public void testActivate() {
        //Player is empty
        victoryPoints = player.getPersonalVictoryPoints();
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().noneMatch(tradingRule1 -> tradingRule1 == tradingRule));

        //PLayer has not the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1,2);
        try {
            additionalTradingRule.activate(player);
        } catch (NoEligiblePlayerException e) {
            e.printStackTrace();
        }
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().noneMatch(tradingRule1 -> tradingRule1 == tradingRule));

        //Player has the requirement to activate the additional trading rule
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Coins,1,2);
        try {
            additionalTradingRule.activate(player);
        } catch (NoEligiblePlayerException e) {
            e.printStackTrace();
            assertEquals(victoryPoints, player.getPersonalVictoryPoints());
            assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().noneMatch(tradingRule1 -> tradingRule1 == tradingRule));
        }
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule1 -> tradingRule1 == tradingRule));
        victoryPoints+= additionalTradingRule.getVictoryPoints();
        assertEquals(victoryPoints,player.getPersonalVictoryPoints());
    }
}
package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ActivateProductionTest {
    private Map<ResourceType,Integer> inputFromWarehouse;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private ActivateProduction activateProduction;
    private ArrayList<TradingRule> tradingRules;
    private Map<ResourceType,Integer> input;
    private Map<ResourceType,Integer> output;
    private Map<ResourceType,Integer> outputAny;
    private Map<ResourceType,Integer> inputAny;
    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        input = new HashMap<>();
        output = new HashMap<>();
        outputAny = new HashMap<>();
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox = new HashMap<>();
        tradingRules = new ArrayList<>();
        activateProduction = new ActivateProduction();
        input.put(ResourceType.Shields,2);
        output.put(ResourceType.Servants,2);
        tradingRules.add( new TradingRule(input,output,2));
        tradingRules.add( new TradingRule(input,output,2));
        inputAny = new HashMap<>();
    }

    @Test
    public void testConvertsInputToOutputOneProductionFromWarehouse(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.addAdditionalRule(tradingRules.get(0));
        activateProduction.setChosenTradingRules(tradingRules);
        activateProduction.convertInputToOutput(player);
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule -> tradingRule == tradingRules.get(0)));
    }

    @Test
    public void testConvertsInputToOutputOneProductionFromStrongBox(){
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        player.addAdditionalRule(tradingRules.get(0));
        activateProduction.setChosenTradingRules(tradingRules);
        activateProduction.convertInputToOutput(player);
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule -> tradingRule == tradingRules.get(0)));
    }

    @Test
    public void testConvertsInputToOutputOneProductionFromStrongBoxAndWarehouse(){
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,1);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,1);
        player.addAdditionalRule(tradingRules.get(0));
        activateProduction.setChosenTradingRules(tradingRules);
        activateProduction.convertInputToOutput(player);
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule -> tradingRule == tradingRules.get(0)));
    }
    @Test
    public void testConvertsInputToOutputTwoProductions(){
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins,2,2);
        player.addAdditionalRule(tradingRules.get(0));
        player.addAdditionalRule(tradingRules.get(1));
        activateProduction.setChosenTradingRules(tradingRules);
        activateProduction.convertInputToOutput(player);
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule -> tradingRule == tradingRules.get(0)));
        assertTrue(player.getPersonalBoard().getActiveTradingRules().stream().anyMatch(tradingRule -> tradingRule == tradingRules.get(1)));
        assertEquals(player.getPersonalBoard().getResourceAmountFromStrongbox(ResourceType.Servants),4);
    }

    @Test
    public void testConvertsInputToOutputWithOutputAny(){
        ArrayList<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(ResourceType.Coins);
        outputAny.put(ResourceType.Any,1);
        outputAny.put(ResourceType.Servants,2);
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        ArrayList<TradingRule> tradingRuleAny = new ArrayList<>();
        tradingRuleAny.add(new TradingRule(input,outputAny,2));
        player.addAdditionalRule(tradingRuleAny.get(0));
        activateProduction.setOutputAny(resourceTypes);
        activateProduction.setChosenTradingRules(tradingRuleAny);
        activateProduction.convertInputToOutput(player);
        assertEquals(player.getPersonalBoard().getResourceAmountFromStrongbox(ResourceType.Servants),2);
        assertEquals(player.getPersonalBoard().getResourceAmountFromStrongbox(ResourceType.Coins),1);
    }

    @Test
    public void testConvertsInputToOutputWithInputAny(){
        ArrayList<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(ResourceType.Coins);
        inputAny.put(ResourceType.Any,1);
        inputAny.put(ResourceType.Shields,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Coins,1,1);
        ArrayList<TradingRule> tradingRuleAny = new ArrayList<>();
        tradingRuleAny.add(new TradingRule(inputAny,output,2));
        player.addAdditionalRule(tradingRuleAny.get(0));
        //player chooses that wants take the resource from the warehouse and set Any to Coins
        activateProduction.setInputAny(resourceTypes);
        inputFromWarehouse.put(resourceTypes.get(0),1);
        inputFromWarehouse.put(ResourceType.Shields,2);
        activateProduction.setInputFrom(inputFromWarehouse,inputFromStrongbox);
        activateProduction.setChosenTradingRules(tradingRuleAny);
        activateProduction.convertInputToOutput(player);
        assertEquals(player.getPersonalBoard().getResourceAmountFromWarehouse(ResourceType.Coins),0);
        assertEquals(player.getPersonalBoard().getResourceAmountFromWarehouse(ResourceType.Shields),0);
        assertEquals(player.getPersonalBoard().getResourceAmountFromStrongbox(ResourceType.Servants),2);
    }
}
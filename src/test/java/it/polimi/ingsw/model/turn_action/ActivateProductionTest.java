package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.leader_cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ActivateProductionTest {
    private Collection<Requirement> requirement;
    private Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private Map<Integer,Integer> depotIDQuantity;
    private ActivateProduction activateProduction;
    private TradingRule tradingRule;
    private Map<ResourceType,Integer> input;
    private Map<ResourceType,Integer> output;
    private ArrayList<ResourceType> outputAny;
    private ArrayList<ResourceType> inputAny;
    private ArrayList<DevelopmentCard> chosenDevelopmentCards;
    private ArrayList<AdditionalTradingRule> additionalTradingRulesChosen;
    private Player player;
    private Game game;

    @Before
    public void setUp() throws Exception {
        requirement = new ArrayList<>();
        requirement.add(new RequirementResource(2,ResourceType.Shields));
        player = new Player("username");
        game = Game.getInstance();
        game.addPlayer("username");
        player.loadFromSettings();
        input = new HashMap<>();
        output = new HashMap<>();
        input.put(ResourceType.Shields,2);
        output.put(ResourceType.Servants,2);
        tradingRule = new TradingRule(input,output,0);
        inputAny = new ArrayList<>();
        outputAny = new ArrayList<>();
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox = new HashMap<>();
        depotIDQuantity = new HashMap<>();
        chosenDevelopmentCards = new ArrayList<>();
        chosenDevelopmentCards.add(new DevelopmentCard(requirement, DevelopmentColor.Yellow,1,2,tradingRule,""));
        player.addDevelopmentCard(chosenDevelopmentCards.get(0),2);
        additionalTradingRulesChosen = new ArrayList<>();
    }

   @Test
    public void activatesAProductionWithResourcesFromWarehouse(){
        depotIDQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDQuantity);
        activateProduction = new ActivateProduction(chosenDevelopmentCards, additionalTradingRulesChosen,inputFromWarehouse,inputFromStrongbox,inputAny,outputAny);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        activateProduction.play(player);
        assertEquals(player.getStrongbox().get(ResourceType.Servants),output.get(ResourceType.Servants));
        assertEquals(player.getResourceQuantity(ResourceType.Shields),0);
    }

    @Test
    public void activatesAProductionWithResourcesFromStrongbox(){
        //player has the resources from warehouse and strongbox, but wants to use
        //those only from strongbox
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox.put(ResourceType.Shields,2);
        activateProduction = new ActivateProduction(chosenDevelopmentCards, additionalTradingRulesChosen,inputFromWarehouse,inputFromStrongbox,inputAny,outputAny);
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        activateProduction.play(player);
        assertEquals(player.getStrongbox().get(ResourceType.Servants),output.get(ResourceType.Servants));
        assertEquals(player.getResourceQuantity(ResourceType.Shields),2);
    }

    @Test
    public void activatesTwoProductions(){
        //creates another production
        Map<ResourceType,Integer> secondInput = new HashMap<>();
        Map<ResourceType,Integer> secondOutput = new HashMap<>();
        secondInput.put(ResourceType.Coins,1);
        secondOutput.put(ResourceType.Servants,1);
        TradingRule secondTradingRule = new TradingRule(secondInput,secondOutput,0);
        DevelopmentCard developmentCard = new DevelopmentCard(requirement,DevelopmentColor.Blue,1,2,secondTradingRule,"");
        chosenDevelopmentCards.add(developmentCard);
        player.addDevelopmentCard(developmentCard,0);
        //inserts the resource required
        player.getWarehouse().addResource(ResourceType.Coins,1,1);
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        //chooses where to take the resources
        inputFromWarehouse = new HashMap<>();
        depotIDQuantity.put(1,1);
        inputFromWarehouse.put(ResourceType.Coins,depotIDQuantity);
        inputFromStrongbox.put(ResourceType.Shields,2);
        activateProduction = new ActivateProduction(chosenDevelopmentCards, additionalTradingRulesChosen,inputFromWarehouse,inputFromStrongbox,inputAny,outputAny);
        activateProduction.play(player);
        //checks
        assertEquals(player.getResourceQuantity(ResourceType.Servants),3);
        assertEquals(player.getResourceQuantity(ResourceType.Coins),0);
        assertEquals(player.getResourceQuantity(ResourceType.Shields),2);
    }


  @Test
    public void activateAdditionalTradingRule(){
        FaithTrack faithTrack = game.getFaithTrack();
        Map<ResourceType,Integer> additionalTradingRuleInput = new HashMap<>();
        Map<ResourceType,Integer> additionalTradingRuleOutput = new HashMap<>();
        additionalTradingRuleInput.put(ResourceType.Coins,1);
        additionalTradingRuleOutput.put(ResourceType.Servants,1);
        additionalTradingRuleOutput.put(ResourceType.Any,1);
        TradingRule tradingRule = new TradingRule(additionalTradingRuleInput,additionalTradingRuleOutput,1);
        AdditionalTradingRule additionalTradingRule = new AdditionalTradingRule(2,requirement,tradingRule,"");
        additionalTradingRulesChosen.add(additionalTradingRule);
        player.getActiveLeaderCards().add(additionalTradingRule);
        outputAny.add(ResourceType.Coins);
        player.getPersonalBoard().addResourceToStrongbox(ResourceType.Coins,1);
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox.put(ResourceType.Coins,1);
        activateProduction = new ActivateProduction(new ArrayList<>(),additionalTradingRulesChosen,inputFromWarehouse,inputFromStrongbox,inputAny,outputAny);
        activateProduction.play(player);
        faithTrack.move(player,additionalTradingRule.getVictoryPoints());
        assertEquals(player.getResourceQuantity(ResourceType.Servants),1);
        assertEquals(player.getResourceQuantity(ResourceType.Coins),1);
        assertEquals(game.getFaithTrack().getPosition(player),faithTrack.getPosition(player));
    }

    @Test
    public void activateBasicProduction(){
        //chooses which resources has to be taken
        Collection<Requirement> requirementsBasicProduction = new ArrayList<>();
        requirementsBasicProduction.add(new RequirementResource(0,ResourceType.Any));
        inputAny.add(ResourceType.Coins);
        inputAny.add(ResourceType.Servants);
        outputAny.add(ResourceType.Stones);
        inputFromStrongbox.put(ResourceType.Coins,1);
        inputFromStrongbox.put(ResourceType.Servants,1);
        player.getStrongbox().put(ResourceType.Coins,1);
        player.getStrongbox().put(ResourceType.Servants,1);
        inputFromWarehouse = new HashMap<>();
        TradingRule basicProduction = player.getPersonalBoard().getBasicProduction();
        DevelopmentCard developmentCardBasicProduction = new DevelopmentCard(requirementsBasicProduction,DevelopmentColor.Any,0,0,basicProduction,"");
        chosenDevelopmentCards = new ArrayList<>();
        chosenDevelopmentCards.add(developmentCardBasicProduction);
        activateProduction = new ActivateProduction(chosenDevelopmentCards,additionalTradingRulesChosen,inputFromWarehouse,inputFromStrongbox,inputAny,outputAny);
        activateProduction.play(player);
        assertEquals(player.getResourceQuantity(ResourceType.Coins),0);
        assertEquals(player.getResourceQuantity(ResourceType.Servants),0);
        assertEquals(player.getResourceQuantity(ResourceType.Stones),1);
    }
}
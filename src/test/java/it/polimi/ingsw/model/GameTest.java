package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.DevelopmentCardColumn;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_action.BuyDevelopmentCard;
import it.polimi.ingsw.model.turn_action.SortWarehouse;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = Game.getInstance();
    }

    @After
    public void tearDown() {
        game.clean();
    }

    @Test
    public void testRemoveDevelopmentCards() {
        DevelopmentCardColumn firstColumn = game.getDevelopmentCardColumns()[0];
        int initialDimension = firstColumn.size();
        game.removeDevelopmentCards(firstColumn.getColor(), 2);
        assertEquals(initialDimension-2, firstColumn.size());
        assertFalse(game.isEnded());
        game.removeDevelopmentCards(firstColumn.getColor(), initialDimension - 2);
        assertEquals(0, firstColumn.size());
        assertTrue(game.isEnded());
    }
    @Test
    public void testComputeScoreWith7DevelopmentCards(){
        Collection<Requirement> requirements = new ArrayList<>();
        Map<ResourceType,Integer> input = new HashMap<>();
        Map<ResourceType,Integer> output = new HashMap<>();
        Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse = new HashMap<>();
        Map<ResourceType,Integer> inputFromStrongbox = new HashMap<>();
        requirements.add(new RequirementResource(2, ResourceType.Shields));
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Shields,2);
        TradingRule tradingRule = new TradingRule(input,output,2);
        DevelopmentColor developmentColor = DevelopmentColor.Yellow;
        Player first = new Player("first");
        Player second = new Player("second");
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.setMaxPlayers(2);
        game.setupPlayers();
        for (int i = 0; i < 2; i++){
            for (int j = 1; j < 4 ; j++ ) {
                DevelopmentCard developmentCard = new DevelopmentCard(requirements, developmentColor, j, 0, tradingRule, "");
                game.getPlayers().get(1).addDevelopmentCard(developmentCard,i);
            }
        }
        game.getPlayers().get(0).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,2);
        DevelopmentCard developmentCard = new DevelopmentCard(requirements,developmentColor,1,0,tradingRule,"");
        inputFromStrongbox.put(ResourceType.Shields,2);
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,2,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(game.getPlayers().get(0));
        assertEquals(game.computeWinner().get().getUsername(),"first");
    }

    @Test
    public void testTieVictoryPointsAndResources(){
        Player first = new Player("first");
        Player second = new Player("second");
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.setMaxPlayers(2);
        game.setupPlayers();
        game.getPlayers().get(0).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).addPersonalVictoryPoints(50);
        game.setEnded();
        assertEquals(game.computeWinner().get().getUsername(),"first");
    }

    @Test
    public void testTieButDifferentResources(){
        Player first = new Player("first");
        Player second = new Player("second");
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.setMaxPlayers(2);
        game.setupPlayers();
        game.getPlayers().get(0).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).getWarehouse().addResource(ResourceType.Shields,2,2);
        game.getPlayers().get(0).getWarehouse().addResource(ResourceType.Coins,1,2);
        game.setEnded();
        assertEquals(game.computeWinner().get().getUsername(),"second");
    }

    @Test
    public void testTieVictoryPointsAndTwoPlayersWithSameAmountResources(){
        Player first = new Player("first");
        Player second = new Player("second");
        Player third = new Player("third");
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.addPlayer(third.getUsername());
        game.setMaxPlayers(3);
        game.setupPlayers();
        game.getPlayers().get(0).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).addPersonalVictoryPoints(50);
        game.getPlayers().get(2).addPersonalVictoryPoints(50);
        game.getPlayers().get(2).getWarehouse().addResource(ResourceType.Shields,2,2);
        game.getPlayers().get(1).getWarehouse().addResource(ResourceType.Shields,2,2);
        game.getPlayers().get(0).getWarehouse().addResource(ResourceType.Coins,1,2);
        game.setEnded();
        assertEquals(game.computeWinner().get().getUsername(),"second");
    }

    @Test
    public void testErrorGame(){
        //end game because it is corrupted or someone is cheating
        Player first = new Player("first");
        Player second = new Player("second");
        Player third = new Player("third");
        SortWarehouse sortWarehouse = new SortWarehouse(2,1);
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.addPlayer(third.getUsername());
        game.setMaxPlayers(3);
        game.setupPlayers();
        game.getPlayers().get(0).getPersonalBoard().addResourceToWarehouse(ResourceType.Coins,2,1);
        sortWarehouse.play(game.getPlayers().get(0));
        assertEquals(game.computeWinner(), Optional.empty());
    }
    @Test
    public void testVictoryWithMoreThanFiveResources(){
        Player first = new Player("first");
        Player second = new Player("second");
        Player third = new Player("third");
        game.addPlayer(first.getUsername());
        game.addPlayer(second.getUsername());
        game.addPlayer(third.getUsername());
        game.setMaxPlayers(3);
        game.setupPlayers();
        game.getPlayers().get(0).addPersonalVictoryPoints(50);
        game.getPlayers().get(1).addPersonalVictoryPoints(50);
        game.getPlayers().get(2).addPersonalVictoryPoints(51);
        game.getPlayers().get(0).getWarehouse().addResource(ResourceType.Coins,1,2);
        game.getPlayers().get(1).getWarehouse().addResource(ResourceType.Shields,2,2);
        game.getPlayers().get(2).getWarehouse().addResource(ResourceType.Shields,2,2);
        game.getPlayers().get(1).getPersonalBoard().addResourceToStrongbox(ResourceType.Shields,3);
        game.setEnded();
        assertEquals(game.computeWinner().get().getUsername(),"second");

    }
}
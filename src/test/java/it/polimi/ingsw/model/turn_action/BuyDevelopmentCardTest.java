package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BuyDevelopmentCardTest {
    private Player player;
    private BuyDevelopmentCard buyDevelopmentCard;
    private DevelopmentCard developmentCard;
    private Collection<Requirement> requirements = new ArrayList<>();
    private TradingRule tradingRule;
    private Map<ResourceType,Integer> input = new HashMap<>();
    private Map<ResourceType,Integer> output = new HashMap<>();
    private int victoryPoints;
    private int amountColor;
    private DevelopmentColor developmentColor;

    @Before
    public void setUp() throws Exception {
        player = new Player("username");
        player = new Player("username");
        requirements.add(new RequirementResource(2,ResourceType.Shields));
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Shields,2);
        tradingRule = new TradingRule(input,output,2);
        developmentColor = DevelopmentColor.Yellow;
        developmentCard = new DevelopmentCard(requirements,developmentColor,1,3, tradingRule);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1);
        amountColor = 0;
    }

    @Test
    public void testBuyDevelopmentCard() {
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyDevelopmentCardNotBuy(){
        //player is not eligible
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),0);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),0);
    }

    @Test
    public void testBuyTwoCardsWrongSlot(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        //Same slot and same level, can not put the second card into the slot
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyTwoCardsRightSlot(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,2);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),2);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),2);
    }

    @Test
    public void testBuyTwoCardsWrongLevel(){
        //Try to put a card of level three on a card of level one
        DevelopmentCard developmentCardWrongLevel;
        developmentCardWrongLevel = new DevelopmentCard(requirements,DevelopmentColor.Yellow,3,2,tradingRule);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCardWrongLevel,1);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyDevelopmentCardRightLevel(){
        DevelopmentCard developmentCardRightLevel;
        developmentCardRightLevel = new DevelopmentCard(requirements,DevelopmentColor.Yellow,2,2,tradingRule);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCardRightLevel,1);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),2);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),2);
    }
}
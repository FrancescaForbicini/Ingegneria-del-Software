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

import static org.junit.Assert.assertEquals;

public class BuyDevelopmentCardTest {
    private Player player;
    private BuyDevelopmentCard buyDevelopmentCard;
    private DevelopmentCard developmentCard;
    private Collection<Requirement> requirements;
    private TradingRule tradingRule;
    private Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    Map<Integer,Integer> depotIDToQuantity;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private DevelopmentColor developmentColor;

    @Before
    public void setUp() throws Exception {
        Map<ResourceType,Integer> input;
        Map<ResourceType,Integer> output;
        player = new Player("username");
        player.loadFromSettings();
        inputFromWarehouse = new HashMap<>();
        inputFromStrongbox = new HashMap<>();
        requirements = new ArrayList<>();
        requirements.add(new RequirementResource(2,ResourceType.Shields));
        input = new HashMap<>();
        output = new HashMap<>();
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Shields,2);
        tradingRule = new TradingRule(input,output,2);
        developmentColor = DevelopmentColor.Yellow;
        developmentCard = new DevelopmentCard(requirements,developmentColor,1,3, tradingRule,"");
        depotIDToQuantity = new HashMap<>();
    }

    @Test
    public void testBuyDevelopmentCard() {
        player.getWarehouse().addResource(ResourceType.Shields,1,1);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDToQuantity.put(1,1);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        inputFromStrongbox.put(ResourceType.Shields,1);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyDevelopmentCardNotBuy(){
        //player is not eligible
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),0);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),0);
    }

    @Test
    public void testBuyTwoCardsWrongSlot(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        depotIDToQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        //Same slot and same level, can not put the second card into the slot
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,null);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyTwoCardsRightSlot(){
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        depotIDToQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,2,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),2);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),2);
    }

    @Test
    public void testBuyTwoCardsWrongLevel(){
        //Try to put a card of level three on a card of level one
        DevelopmentCard developmentCardWrongLevel;
        developmentCardWrongLevel = new DevelopmentCard(requirements,DevelopmentColor.Yellow,3,2,tradingRule,"");
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        depotIDToQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        BuyDevelopmentCard buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        depotIDToQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        BuyDevelopmentCard buyDevelopmentCardWrongLevel = new BuyDevelopmentCard(developmentCardWrongLevel,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCardWrongLevel.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),1);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),1);
    }

    @Test
    public void testBuyDevelopmentCardRightLevel(){
        DevelopmentCard developmentCardRightLevel;
        developmentColor = DevelopmentColor.Yellow;
        developmentCard = new DevelopmentCard(requirements,DevelopmentColor.Yellow,1,2,tradingRule,"");
        developmentCardRightLevel = new DevelopmentCard(requirements,DevelopmentColor.Yellow,2,2,tradingRule,"");
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,2);
        player.getStrongbox().put(ResourceType.Shields,1);
        depotIDToQuantity.put(2,1);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        inputFromStrongbox.put(ResourceType.Shields,1);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        inputFromStrongbox = new HashMap<>();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,2,2);
        depotIDToQuantity.put(2,2);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCardRightLevel,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        assertEquals(player.getDevelopmentQuantity(developmentColor),2);
        assertEquals(player.getDevelopmentQuantity(DevelopmentColor.Yellow),2);
    }

    @Test
    public void testBuyDevelopmentCardWithDiscount(){
        developmentCard = new DevelopmentCard(requirements,DevelopmentColor.Yellow,1,2,tradingRule,"");
        player.addDiscount(ResourceType.Shields,1);
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,1);
        depotIDToQuantity.put(1,1);
        inputFromWarehouse.put(ResourceType.Shields,depotIDToQuantity);
        inputFromStrongbox = new HashMap<>();
        buyDevelopmentCard = new BuyDevelopmentCard(developmentCard,1,inputFromWarehouse,inputFromStrongbox);
        buyDevelopmentCard.play(player);
        assertEquals(player.getResourceQuantity(ResourceType.Shields),0);
    }
}
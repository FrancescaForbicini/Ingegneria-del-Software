package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DevelopmentCardTest {
    private Player player;
    private DevelopmentCard developmentCard;
    private Collection<Requirement> requirements = new ArrayList<>();
    private TradingRule tradingRule;
    private Map<ResourceType,Integer> input = new HashMap<>();
    private Map<ResourceType,Integer> output = new HashMap<>();
    private int victoryPoints;
    private int amountColor;
    private DevelopmentColor developmentColor;
    private ArrayList<WarehouseDepot> warehouseDepots;

    @Before
    public void setUp(){
        player = new Player("username");
        requirements.add(new RequirementResource(2,ResourceType.Shields));
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Shields,2);
        tradingRule = new TradingRule(input,output,2);
        developmentColor = DevelopmentColor.Yellow;
        developmentCard = new DevelopmentCard(requirements,developmentColor,1,3, tradingRule);
        victoryPoints = 0;
        amountColor = 0;
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void testBuyNoRequirements() {
        //Player hasn't the requirements to buy the development card
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields, 1, 2);
        developmentCard.buy(player, 1);
        assertEquals(player.getPersonalVictoryPoints(), victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor), amountColor);
    }

    @Test
    public void testBuyRequirements() {
        //Player has the requirements to buy the development card
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields, 2, 2);
        developmentCard.buy(player, 1);
        victoryPoints += player.getPersonalVictoryPoints();
        amountColor += 1;
        assertEquals(player.getPersonalVictoryPoints(), victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor), amountColor);
    }
    @Test
    public void testBuyAddSameCardButNoRequirements() {
        //Player wants to add the same development card but hasn't the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1,2);
        developmentCard.buy(player, 1);
        assertEquals(player.getPersonalVictoryPoints(), victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor), amountColor);
    }
    @Test
    public void testBuyAddSameCardRightRequirements(){
        //Player wants to add the same development card and has the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,2,2);
        developmentCard.buy(player, 1);
        victoryPoints += player.getPersonalVictoryPoints();
        amountColor += 1;
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);
    }
}

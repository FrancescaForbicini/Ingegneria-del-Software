package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.board.NotEnoughSpaceException;
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
        tradingRule = new TradingRule(2,input,output);
        developmentColor = DevelopmentColor.Yellow;
        developmentCard = new DevelopmentCard(requirements,developmentColor,1,3, tradingRule);
        victoryPoints = 0;
        amountColor = 0;
        warehouseDepots = new ArrayList<>();
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(0));
        warehouseDepots.add(player.getPersonalBoard().getWarehouse().getWarehouseDepots().get(1));
    }

    @Test
    public void testBuy(){
        //Player is empty
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);

        //Player hasn't the requirements to buy the development card
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1, 2);
        try{
            developmentCard.buy(player,1);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);

        //Player has the requirements to buy the development card
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,1, 2);
        try{
            developmentCard.buy(player,1);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        player.addDevelopmentCard(developmentCard,1);
        victoryPoints += player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor) + 1;
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);

        //Player wants to add the same development card but hasn't the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().removeResource(2,2);
        try{
            developmentCard.buy(player,1);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);

        //Player wants to add the same development card and has the right requirements
        victoryPoints = player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor);
        player.getPersonalBoard().getWarehouse().addResource(ResourceType.Shields,2,2);
        try{
            developmentCard.buy(player,1);
        }catch(NoEligiblePlayerException e){
            e.printStackTrace();
        }
        player.addDevelopmentCard(developmentCard,1);
        victoryPoints += player.getPersonalVictoryPoints();
        amountColor = player.getDevelopmentQuantity(developmentColor) + 1;
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
        assertEquals(player.getDevelopmentQuantity(developmentColor),amountColor);
    }
}

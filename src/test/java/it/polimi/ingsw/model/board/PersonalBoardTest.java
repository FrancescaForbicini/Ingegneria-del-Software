package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class PersonalBoardTest {
    PersonalBoard personalBoard;
    @Before
    public void setUp() {
        personalBoard = new PersonalBoard();
    }

    @Test
    public void testAddAdditionalRule() {
        assertEquals(0, personalBoard.getAdditionalRules().size());
        personalBoard.addAdditionalRule(new TradingRule(1, null, null));
        assertEquals(1, personalBoard.getAdditionalRules().size());
        personalBoard.addAdditionalRule(new TradingRule(1, null, null));
        assertEquals(2, personalBoard.getAdditionalRules().size());
    }

    @Test
    public void testAddResourceToStrongbox() {
        assertEquals(0, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 10);
        assertEquals(10, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 20);
        assertEquals(30, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
    }

    @Test
    public void testRemoveResourceToStrongbox() throws NotEnoughResourcesException {
        personalBoard.addResourceToStrongbox(ResourceType.Coins, 10);
        assertEquals(10, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
        personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 5);
        assertEquals(5, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
        personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 5);
        assertEquals(0, personalBoard.getResourceFromStrongbox(ResourceType.Coins));
    }

    @Test(expected = NotEnoughResourcesException.class)
    public void testRemoveResourceToStrongboxEmpty() throws NotEnoughResourcesException {
        personalBoard.removeResourceFromStrongbox(ResourceType.Coins, 10);
    }

    @Test
    public void testAddResourceToWarehouse() {
        fail();
    }

    @Test
    public void testRemoveResourceToWarehouse() {
        fail();
    }

    @Test
    public void isWarehouseFull() {
        fail();
    }

    @Test
    public void getValidDevelopmentCardLevels() {
        assertEquals(new HashSet<>(Arrays.asList(1)), personalBoard.getValidDevelopmentCardLevels());
        personalBoard.addDevelopmentCard(new DevelopmentCard(null, DevelopmentColor.Blue,1, 0, null));
        assertEquals(new HashSet<>(Arrays.asList(1,2)), personalBoard.getValidDevelopmentCardLevels());
        personalBoard.addDevelopmentCard(new DevelopmentCard(null, DevelopmentColor.Blue,2, 0, null));
        assertEquals(new HashSet<>(Arrays.asList(1,3)), personalBoard.getValidDevelopmentCardLevels());
        personalBoard.addDevelopmentCard(new DevelopmentCard(null, DevelopmentColor.Blue,1, 0, null));
        assertEquals(new HashSet<>(Arrays.asList(1, 2,3)), personalBoard.getValidDevelopmentCardLevels());
        personalBoard.addDevelopmentCard(new DevelopmentCard(null, DevelopmentColor.Blue,1, 0, null));
        assertEquals(new HashSet<>(Arrays.asList(2,3)), personalBoard.getValidDevelopmentCardLevels());
    }

    @Test
    public void testGetActiveTradingRules() {
        TradingRule tradingRule1 = new TradingRule(1, null, null);
        DevelopmentCard developmentCard1 = new DevelopmentCard(null, DevelopmentColor.Blue, 1, 10, tradingRule1);
        TradingRule tradingRule2 = new TradingRule(1, null, null);
        DevelopmentCard developmentCard2 = new DevelopmentCard(null, DevelopmentColor.Purple, 2, 10, tradingRule2);
        TradingRule tradingRule3 = new TradingRule(1, null, null);
        assertEquals(1, personalBoard.getActiveTradingRules().size());
        assertTrue(personalBoard.getActiveTradingRules().contains(personalBoard.getBasicProduction()));

        personalBoard.addDevelopmentCard(developmentCard1);
        assertEquals(2, personalBoard.getActiveTradingRules().size());
        assertTrue(personalBoard.getActiveTradingRules().contains(personalBoard.getBasicProduction()));
        assertTrue(personalBoard.getActiveTradingRules().contains(tradingRule1));

        personalBoard.addDevelopmentCard(developmentCard2);
        assertEquals(2, personalBoard.getActiveTradingRules().size());
        assertTrue(personalBoard.getActiveTradingRules().contains(personalBoard.getBasicProduction()));
        assertTrue(personalBoard.getActiveTradingRules().contains(tradingRule2));

        personalBoard.addAdditionalRule(tradingRule3);
        assertEquals(3, personalBoard.getActiveTradingRules().size());
        assertTrue(personalBoard.getActiveTradingRules().contains(personalBoard.getBasicProduction()));
        assertTrue(personalBoard.getActiveTradingRules().contains(tradingRule2));
        assertTrue(personalBoard.getActiveTradingRules().contains(tradingRule3));


    }
}
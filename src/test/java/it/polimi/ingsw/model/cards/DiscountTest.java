package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class DiscountTest {
    private Player player;
    private ResourceType type;
    private final Collection<Requirement> requirement = new ArrayList<>();
    private Discount discount;
    int victoryPoints;
    @Before
    public void setUp() {
        player = new Player("username");
        player.loadFromSettings();
        type = ResourceType.Any;
        requirement.add(new RequirementResource(2,ResourceType.Shields));
        discount = new Discount(2,type,1,requirement,"");
        victoryPoints = 0;
    }

    @Test
    public void testActivateNoRequirements() {
        //Player with no requirements to obtain the discount
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields, 1, 2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException ignored) {
        }
        assertEquals(player.getActiveDiscounts().size(), 0);
        assertEquals(victoryPoints, player.getPersonalVictoryPoints());
    }
    @Test
    public void testActivateRightRequirements() {
        //Player with requirement, obtain the discount
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields, 2, 2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException ignored) {
        }
        victoryPoints+=discount.getVictoryPoints();
        assertEquals(player.applyDiscount(type), -1);
        assertEquals(player.getPersonalVictoryPoints(), victoryPoints);
    }
    @Test
    public void testDiscountInsertSameDiscount(){
        //Insert the first discount
        victoryPoints = player.getPersonalVictoryPoints();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields, 2, 2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException ignored) {
        }
        victoryPoints+=discount.getVictoryPoints();
        //Insert a discount for the same resource
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException ignored) {
        }
        assertEquals(player.applyDiscount(type),-2);
        victoryPoints+=discount.getVictoryPoints();
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
    }
    @Test
    public void testToString(){
        System.out.println("First line");
        System.out.println(discount.toString());
        System.out.println("End line");
    }
}
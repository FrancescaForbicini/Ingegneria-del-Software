package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.board.NotEnoughSpaceException;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class DiscountTest {
    private Player player;
    private ResourceType type;
    private final Collection<Requirement> requirement = new ArrayList<>();
    private Discount discount;
    int victoryPoints;
    @Before
    public void setUp() {
        player = new Player("username");
        type = ResourceType.Any;
        requirement.add(new RequirementResource(2,ResourceType.Shields));
        discount = new Discount(2,type,1,requirement);
        victoryPoints = 0;
    }

    @Test
    public void testActivate() {
        //Player is empty
        victoryPoints = player.getPersonalVictoryPoints();
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException e) {
            assertEquals(player.applyDiscount(type),0);
            assertEquals(victoryPoints,player.getPersonalVictoryPoints());
        }
        //Player with no requirements to obtain the discount
        victoryPoints = player.getPersonalVictoryPoints();
            player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException e) {
            assertEquals(player.applyDiscount(type),0);
            assertEquals(victoryPoints,player.getPersonalVictoryPoints());
        }

        //Player with requirement, obtain the discount
        victoryPoints = player.getPersonalVictoryPoints() + discount.getVictoryPoints();
        player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException e) {
            assertEquals(player.applyDiscount(type),0);
        }
        assertEquals(player.applyDiscount(type),-1);
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);

        //Insert the same discount
        victoryPoints = player.getPersonalVictoryPoints() + discount.getVictoryPoints();
            player.getPersonalBoard().addResourceToWarehouse(ResourceType.Shields,1,2);
        try {
            discount.activate(player);
        } catch (NoEligiblePlayerException e) {
            assertEquals(player.applyDiscount(type),0);
        }
        assertEquals(player.applyDiscount(type),-2);
        victoryPoints+=discount.getVictoryPoints();
        assertEquals(player.getPersonalVictoryPoints(),victoryPoints);
    }
}
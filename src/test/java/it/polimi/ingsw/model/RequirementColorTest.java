package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RequirementColorTest {
    private RequirementColor requirementColor;
    private Player player;
    private final int points=1;
    private final HashMap<ResourceType,Integer> input = new HashMap<ResourceType, Integer>();
    private final HashMap<ResourceType,Integer> output = new HashMap<ResourceType, Integer>();
    private final TradingRule tradingRule=new TradingRule(input,output, points);
    private Collection<Requirement> requirements= new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        requirementColor= new RequirementColor(2,2, DevelopmentColor.Yellow);
        player= new Player("username");
        input.put(ResourceType.Any,1);
        output.put(ResourceType.Any,1);
        requirements.add(new RequirementResource(1,ResourceType.Any));
    }

    @Test
    public void testIsSatisfied() {
        //Player is empty
        //Quantity is not satisfied
        assertEquals(player.getDevelopmentQuantity(requirementColor.getColor(), requirementColor.getLevel()),0);
        assertFalse(requirementColor.isSatisfied(player));

        //Player is not empty
        //Color is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements,DevelopmentColor.Blue,1,points,tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        //Level is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements,DevelopmentColor.Yellow,1,points,tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        //Quantity is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements,DevelopmentColor.Yellow,2,points,tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        //Quantity ok, but Level is not satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements,DevelopmentColor.Yellow,3,points,tradingRule));
        assertFalse(requirementColor.isSatisfied(player));
        // Everything is satisfied
        player.addDevelopmentCard(new DevelopmentCard(requirements,DevelopmentColor.Yellow,2,points,tradingRule));
        assertTrue(requirementColor.isSatisfied(player));
    }
}
package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.RequirementColor;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequirementColorTest {
    private RequirementColor requirementColor;
    private Player player;

    @Before
    public void setUp() throws Exception {
        requirementColor= new RequirementColor(2,1, DevelopmentColor.Yellow);
    }

    @Test
    public void testIsSatisfied() {
        int quantity;
        int level;
        DevelopmentColor color;

        //Quantity are not satisfied
        quantity= requirementColor.getQuantity()+1;
        assertFalse(requirementColor.isSatisfied(player));

        //Color is not satisfied
        color=DevelopmentColor.Green;
        assertFalse(requirementColor.isSatisfied(player));

        //Level is not satisfied
        level= requirementColor.getLevel()+1;
        assertFalse(requirementColor.isSatisfied(player));

    }
}
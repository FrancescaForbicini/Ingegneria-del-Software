package it.polimi.ingsw.model;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    private Game game;
    private DevelopmentColor firstColor;
    private int initialDimension;

    @Before
    public void setUp() {
        game = Game.getInstance();
        firstColor = game.getDevelopmentCards().get(0).get(0).showFirstCard().get().getColor();
        initialDimension = game.getDevelopmentCards().get(0).size();
    }

    @After
    public void tearDown() {
        game.clean();
    }

    @Test
    public void testRemoveDevelopmentCards() {
        game.removeDevelopmentCards(firstColor, 2);
        assertEquals(initialDimension-2, game.getDevelopmentCards().get(0).size());
    }
}
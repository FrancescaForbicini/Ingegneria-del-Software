package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCardColumn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = Game.getInstance();
    }

    @After
    public void tearDown() {
        game.clean();
    }

    @Test
    public void testRemoveDevelopmentCards() {
        DevelopmentCardColumn firstColumn = game.getDevelopmentCardColumns()[0];
        int initialDimension = firstColumn.size();
        game.removeDevelopmentCards(firstColumn.getColor(), 2);
        assertEquals(initialDimension-2, firstColumn.size());
        assertFalse(game.isEnded());
        game.removeDevelopmentCards(firstColumn.getColor(), initialDimension - 2);
        assertEquals(0, firstColumn.size());
        assertTrue(game.isEnded());
    }
}
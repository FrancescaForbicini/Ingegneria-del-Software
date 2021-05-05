package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopmentSlotTest {
    DevelopmentSlot developmentSlot;
    DevelopmentCard developmentCardBlueOne;
    DevelopmentCard developmentCardGreenTwo;
    DevelopmentCard developmentCardPurpleThree;
    @Before
    public void setUp() {
        developmentSlot = new DevelopmentSlot(1);
        developmentCardBlueOne = new DevelopmentCard(null, DevelopmentColor.Blue, 1, 10, null);
        developmentCardGreenTwo = new DevelopmentCard(null, DevelopmentColor.Green, 2, 20, null);
        developmentCardPurpleThree = new DevelopmentCard(null, DevelopmentColor.Purple, 3, 30, null);
    }
    @Test
    public void testAddCardAndSize() {
        assertTrue(developmentSlot.showCardOnTop().isEmpty());
        assertEquals(0,developmentSlot.size());
        developmentSlot.addCard(developmentCardBlueOne);
        assertEquals(1,developmentSlot.size());
        developmentSlot.addCard(developmentCardBlueOne);
        assertEquals(1,developmentSlot.size());
        developmentSlot.addCard(developmentCardGreenTwo);
        assertEquals(2,developmentSlot.size());
    }

    @Test
    public void testShowCardOnTop() {
        assertTrue(developmentSlot.showCardOnTop().isEmpty());
        developmentSlot.addCard(developmentCardBlueOne);
        assertEquals(developmentCardBlueOne, developmentSlot.showCardOnTop().get());
        developmentSlot.addCard(developmentCardGreenTwo);
        developmentSlot.addCard(developmentCardPurpleThree);
        assertEquals(developmentCardPurpleThree, developmentSlot.showCardOnTop().get());
    }

    @Test
    public void testGetNextLevel() {
        assertEquals(1, developmentSlot.getNextLevel());
        developmentSlot.addCard(developmentCardBlueOne);
        assertEquals(2, developmentSlot.getNextLevel());
        developmentSlot.addCard(developmentCardBlueOne);
        assertEquals(2, developmentSlot.getNextLevel());
        developmentSlot.addCard(developmentCardGreenTwo);
        assertEquals(3, developmentSlot.getNextLevel());
    }
}
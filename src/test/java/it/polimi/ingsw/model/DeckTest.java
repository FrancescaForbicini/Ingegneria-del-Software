package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DeckTest {
    private Deck<Object> deck;

    @Before
    public void setUp() {
        this.deck = new Deck<>();
    }

    @Test
    public void testShuffle() {
        assertEquals(0, this.deck.size());
        this.deck.shuffle();
        assertEquals(0, this.deck.size());
        this.deck.addAll(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, this.deck.size());
        this.deck.shuffle();
        assertEquals(5, this.deck.size());
    }

    @Test
    public void testCreateFromCollection() {
        Deck<Integer> deck = new Deck<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, deck.size());
    }

    @Test
    public void testIsEmptyAddCard() {
        assertTrue(deck.isEmpty());
        deck.addCard(42);
        assertFalse(deck.isEmpty());

    }

    @Test
    public void testDrawFirstCard() {
        Object o = new Object();
        this.deck.addCard(42);
        this.deck.addCard(o);
        assertEquals(2, this.deck.size());
        Object o1 = this.deck.drawFirstCard().get();
        assertEquals(o1, o);
        assertEquals(1, this.deck.size());
    }

    @Test
    public void testShowFirstCard() {
        Object o = new Object();
        this.deck.addCard(42);
        this.deck.addCard(o);
        assertEquals(2, this.deck.size());
        Object o1 = this.deck.showFirstCard().get();
        assertEquals(o1, o);
        assertEquals(2, this.deck.size());
    }


    @Test
    public void testToString() {
        this.deck.addCard(42);
        this.deck.addCard(43);
        assertEquals("Deck: 43, 42", this.deck.toString());
    }

    @Test
    public void testDrawAndPutBelowFirst() {
        Object o = new Object();
        this.deck.addCard(42);
        this.deck.addCard(o);
        assertEquals(2, this.deck.size());
        Object o1 = this.deck.drawAndPutBelowFirst().get();
        assertEquals(o, o1);
        assertEquals(2, this.deck.size());
        this.deck.drawFirstCard();
        assertEquals(o, this.deck.showFirstCard().get());
    }
}
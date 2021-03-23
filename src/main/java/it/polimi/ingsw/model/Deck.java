package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * It is a container of a group of cards with some specific operations
 * @param <T> the type of the card
 */
public class Deck<T>{
    private Deque<T> cards;

    /**
     * Initializes an empty Deck
     */
    public Deck() {
        // TODO not thread safe, check it later
        cards = new ArrayDeque<>();
    }

    /** Initializes a deck from a collection
     *
     * @param cards cards used to initialize the deck
     */
    public Deck(Collection<? extends T> cards){
        // TODO not thread safe, check it later
        this.cards = new ArrayDeque<>(cards);
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        List<T> cards = (List<T>)Arrays.asList(this.cards.toArray());
        Collections.shuffle(cards);
        this.cards = new ArrayDeque<>(cards);
    }

    /**
     * Checks if the deck is empty
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws a card from a deck, the card IS removed from the deck
     * @return the picked card
     * @throws NoSuchElementException is  the deck is empty
     */
    public T drawFirstCard() throws NoSuchElementException {
        return cards.removeFirst();
    }

    /**
     * Shows a card from the deck, the card IS NOT removed
     * @return the first card of the deck
     * @throws NoSuchElementException is  the deck is empty
     */
    public T showFirstCard() {
        return cards.getFirst();
    }

    /**
     * Adds a card to the deck as the first
     * @param card the card to insert in the deck, as first
     */
    public void addCard(T card) {
        cards.addFirst(card);
    }

    public void addAll(Collection<? extends T> cards) {
        cards.forEach(x -> this.addCard(x));
    }

    /**
     * Returns the length of the deck
     * @return the length of the deck
     */
    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        String s = "Deck: " + cards.stream().map(Object::toString).collect(Collectors.joining(", "));
        return s;
    }
}

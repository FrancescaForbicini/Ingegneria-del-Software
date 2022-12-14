package it.polimi.ingsw.model.cards;

import java.util.*;
import java.util.stream.Collectors;

/**
 * It is a container of a group of cards with some specific operations
 *
 * @param <T> the type of the card
 */
public class Deck<T>{
    private Deque<T> cards;

    /**
     * Initializes an empty Deck
     */
    public Deck() {
        cards = new ArrayDeque<>();
    }

    /**
     * Initializes a deck from a collection
     *
     * @param cards cards used to initialize the deck
     */
    public Deck(Collection<? extends T> cards){
        this.cards = new ArrayDeque<>(cards);
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        ArrayList<T> cards = new ArrayList<T>(this.cards);
        Collections.shuffle(cards);
        this.cards = new ArrayDeque<>(cards);
    }

    /**
     * Checks if the deck is empty
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws a card from a deck, the card IS removed from the deck
     *
     * @return Optionally, the picked first card
     */
    public Optional<T> drawFirstCard() {
        return Optional.ofNullable(cards.pollFirst());
    }

    /**
     * Shows a card from the deck, the card IS NOT removed
     *
     * @return Optionally, the first card of the deck
     */
    public Optional<T> showFirstCard() {
        return Optional.ofNullable(cards.peekFirst());
    }
    /**
     * Adds a card to the deck as the first
     *
     * @param card the card to insert in the deck, as first
     */
    public void addCard(T card) {
        cards.addFirst(card);
    }

    /**
     * Draws a card from a deck, the card IS put as the last card of the  deck
     *
     * @return Optionally, the picked first card
     */
    public Optional<T> drawAndPutBelowFirst() {
        Optional<T> ocard = drawFirstCard();
        ocard.ifPresent(t -> cards.addLast(t));
        return ocard;
    }

    public void addAll(Collection<? extends T> cards) {
        cards.forEach(this::addCard);
    }

    public ArrayList<T> toArrayList(){
        return new ArrayList<T> (cards);
    }

    /**
     * Returns the length of the deck
     *
     * @return the length of the deck
     */
    public int size() {
        return cards.size();
    }

    public void removeIfPresent(T t){
        cards.remove(t);
    }

    @Override
    public String toString() {
        return "Deck: " + cards.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public List<T> drawFourCards(){
        Optional<List<T>> pickedCards = drawFirstCard()
                .flatMap(card1 -> drawFirstCard()
                        .flatMap(card2 -> drawFirstCard()
                                .flatMap(card3 -> drawFirstCard()
                                        .map(card4 -> Arrays.asList(card1, card2, card3, card4)))));
        if (pickedCards.isEmpty())
            return null;
        return pickedCards.get();
    }
}

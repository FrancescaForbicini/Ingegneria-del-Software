package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;


/**
 * A slot containing the stack of the development cards
 */
public class DevelopmentSlot {
    private Deque<DevelopmentCard> cards;

    public DevelopmentSlot() {
        this.cards = new ArrayDeque<>();
    }

    /**
     * Adds a card on top of the stack, do nothing if the card's level is illegal
     * @param card the card to add on top
     */
    public void addCard(DevelopmentCard card) {
        if (getNextLevel() == card.getLevel())
            cards.addFirst(card);
    }

    /**
     * Shows card that can be used in the production
     * @return The card on top of the stack
     */
    public Optional<DevelopmentCard> showCardOnTop() {
        return Optional.ofNullable(cards.peek());
    }

    /**
     * Computes number of cards in the stack
     * @return the number of cards
     */
    public int size(){
        return cards.size();
    }

    /**
     * Computes the level of the card that can be added on top of the stack
     * @return The next valid level
     */
    public int getNextLevel(){
        return size() + 1;
    }

    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor){
        return cards.stream()
                .filter(card -> card.getColor() == developmentColor)
                .mapToInt(DevelopmentCard::getLevel)
                .max().orElse(0);
    }

    public int getDevelopmentQuantity(DevelopmentColor developmentColor){
        return (int) cards.stream()
                .filter(card -> card.getColor() == developmentColor)
                .count();
    }

    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
       return (int) cards.stream()
               .filter(card -> card.getColor() == developmentColor && card.getLevel() == level)
               .count();

    }
}

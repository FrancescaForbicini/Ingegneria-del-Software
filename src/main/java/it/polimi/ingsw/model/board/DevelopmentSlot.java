package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Optional;


/**
 * A slot containing the stack of the development cards
 */
public class DevelopmentSlot {
    private final Deque<DevelopmentCard> cards;
    private final int slotID;

    public DevelopmentSlot(int slotID) {
        this.slotID = slotID;
        this.cards = new ArrayDeque<>();
    }

    public int getSlotID(){return this.slotID;}

    public Deque<DevelopmentCard> getCards() {
        return cards;
    }

    /**
     * Adds a card on top of the stack, do nothing if the card's level is illegal
     * @param card the card to add on top
     */
    public boolean addCard(DevelopmentCard card) {
        if (canAddCard(card)){
            cards.addFirst(card);
            return true;
        }
        return false;
    }

    /**
     * Checks if it is possible to add a development card in this slot
     *
     * @param card development card to add
     * @return true iff the card can be added
     */
    public boolean canAddCard(DevelopmentCard card){
        return  getNextLevel() == card.getLevel();
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
     * Check if a card is present in the slot
     * @param developmentCard development card to check if it presents in this slot
     * @return true iff the passed card is present
     */
    public boolean contains (DevelopmentCard developmentCard){
        return cards.contains(developmentCard);
    }

    /**
     * Computes the level of the card that can be added on top of the stack
     * @return The next valid level
     */
    public int getNextLevel(){
        return size() + 1;
    }

    /**
     * Gets max level of a specific color of a development card
     * @param developmentColor color of the card
     * @return the max level of the card based on the color
     */
    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor){
        return cards.stream()
                .filter(card -> card.getColor() == developmentColor)
                .mapToInt(DevelopmentCard::getLevel)
                .max().orElse(0);
    }

    /**
     * Gets the quantity of a specific color of a development card
     * @param developmentColor color of the card
     * @return the quantity of the card based on the color
     */
    public int getDevelopmentQuantity(DevelopmentColor developmentColor){
        return (int) cards.stream()
                .filter(card -> card.getColor() == developmentColor)
                .count();
    }

    /**
     * Gets the quantity of a specific color and level of a development card
     * @param developmentColor color of the card
     * @return the quantity of the card based on the color and the level
     */
    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
       return (int) cards.stream()
               .filter(card -> card.getColor() == developmentColor && card.getLevel() == level)
               .count();
    }

    /**
     * Prints the slot
     * @return the string to print to show the slot
     */
    @Override
    public String toString(){
        StringBuilder print = new StringBuilder();
        print.append("Slot ").append(slotID+1).append("\n");
        ArrayList<DevelopmentCard> arrayListCards = new ArrayList<>(cards);
        for(DevelopmentCard card : arrayListCards){
            print.append(card.toString()).append("\n");
        }
        return print.toString();
    }
}

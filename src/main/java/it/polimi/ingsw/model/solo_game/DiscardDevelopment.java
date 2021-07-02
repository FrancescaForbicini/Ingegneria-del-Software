package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.requirement.DevelopmentColor;

/**
 * Discards some development cards in a `DevelopmentCard` deck
 */

public class DiscardDevelopment implements SoloToken {
    private final DevelopmentColor color;
    private final int numberToRemove;

    /**
     * @param color to identify the deck
     * @param numberToRemove amount of DevelopmentCards to be removed from the identified deck
     */
    public DiscardDevelopment(DevelopmentColor color, int numberToRemove){
        this.color = color;
        this.numberToRemove = numberToRemove;
    }

    /**
     * Discards the development card in base of the color
     */
    @Override
    public void use() {
        Game.getInstance().removeDevelopmentCards(color, numberToRemove).stream();

    }

    @Override
    public String toString() {
        return "DiscardDevelopment{" +
                "color=" + color +
                ", numberToRemove=" + numberToRemove +
                '}';
    }

}

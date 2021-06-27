package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.requirement.DevelopmentColor;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Discards some development cards in a `DevelopmentCard` deck
 */
public class DiscardDevelopment implements SoloToken {
    private final DevelopmentColor color;
    private final int numberToRemove;
    private String discardedCardsString;
    public DiscardDevelopment(DevelopmentColor color, int numberToRemove){
        this.color = color;
        this.numberToRemove = numberToRemove;
    }

    /**
     * Discards the development card in base of the color
     */
    @Override
    public void use() {
        discardedCardsString = Game.getInstance()
                .removeDevelopmentCards(color, numberToRemove).stream()
                .map(Objects::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return "DiscardDevelopment{" +
                "color=" + color +
                ", numberToRemove=" + numberToRemove +
                ", discardedCardsString='" + discardedCardsString + '\'' +
                '}';
    }
}

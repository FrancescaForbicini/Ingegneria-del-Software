package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DevelopmentCardColumn {
    private final DevelopmentColor color;
    private Deck<DevelopmentCard>[] decks;

    public DevelopmentColor getColor() {
        return color;
    }

    public DevelopmentCardColumn(DevelopmentColor color, ArrayList<DevelopmentCard> cards) {
        this.color = color;
        Map<Integer, List<DevelopmentCard>> cardsPerLevel = cards.stream()
                .collect(Collectors.groupingBy(DevelopmentCard::getLevel));
        decks = new Deck[3];
        decks[0] = new Deck<>(cardsPerLevel.get(1));
        decks[1] = new Deck<>(cardsPerLevel.get(2));
        decks[0] = new Deck<>(cardsPerLevel.get(3));
    }
}

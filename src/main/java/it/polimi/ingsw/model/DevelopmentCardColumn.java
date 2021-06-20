package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.turn_taker.Opponent;

import java.util.*;
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
        decks[2] = new Deck<>(cardsPerLevel.get(3));
    }

    public void removeIfPresent(DevelopmentCard developmentCard) {
        int level = developmentCard.getLevel();
        decks[level-1].removeIfPresent(developmentCard);
    }

    public void remove(int numberToRemove) {
        Deck<DevelopmentCard> deck;
        for (int i = 0; i < numberToRemove; i++) {
            deck = firstDeckWithAtLeastOne();
            if (deck == null)
                break;
            deck.drawFirstCard();
        }
        if (size() == 0) {
            Opponent.getInstance().setWinner();
            Game.getInstance().setEnded();
        }
    }

    private Deck<DevelopmentCard> firstDeckWithAtLeastOne() {
        for (Deck deck : decks) {
            if (deck.size() >= 1)
                return deck;
        }
        return null;
    }

    public ArrayList<DevelopmentCard> getVisibleCards() {
        return (ArrayList<DevelopmentCard>) Arrays.stream(decks)
                .map(Deck::showFirstCard)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public int size() {
        return Arrays.stream(decks).reduce(0, (partialSum, deck) -> partialSum + deck.size(), Integer::sum);
    }
}

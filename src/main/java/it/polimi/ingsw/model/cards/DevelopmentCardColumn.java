package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.turn_taker.Opponent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represent a set of 3 decks of DevelopmentCards, all with the same DevelopmentColor
 */
public class DevelopmentCardColumn {
    private final DevelopmentColor color;
    private final Deck<DevelopmentCard>[] decks;

    public DevelopmentColor getColor() {
        return color;
    }

    public DevelopmentCardColumn(DevelopmentColor color, ArrayList<DevelopmentCard> cards) {
        Random random = new Random();
        this.color = color;
        Map<Integer, List<DevelopmentCard>> cardsPerLevel = cards.stream()
                .collect(Collectors.groupingBy(DevelopmentCard::getLevel));
        if (System.getenv().containsKey("SEED"))
            random = new Random(Integer.parseInt(System.getenv().get("SEED")));
        decks = new Deck[3];
        Collections.shuffle(cardsPerLevel.get(1), random);
        decks[0] = new Deck<>(cardsPerLevel.get(1));
        Collections.shuffle(cardsPerLevel.get(2), random);
        decks[1] = new Deck<>(cardsPerLevel.get(2));
        Collections.shuffle(cardsPerLevel.get(3), random);
        decks[2] = new Deck<>(cardsPerLevel.get(3));
    }

    /**
     * Remove a certain card from the set if it is present in any deck
     *
     * @param developmentCard to be removed
     */
    public void removeIfPresent(DevelopmentCard developmentCard) {
        int level = developmentCard.getLevel();
        decks[level-1].removeIfPresent(developmentCard);
    }

    /**
     * Removes a certain number of cards from the top of the first available deck
     *
     * @param numberToRemove amount of cards to be removed
     * @return collection of removed cards
     */
    public ArrayList<DevelopmentCard> remove(int numberToRemove) {
        ArrayList<DevelopmentCard> discardedCards = new ArrayList<>();
        for (int i = 0; i < numberToRemove; i++) {
            firstDeckWithAtLeastOne()
                    .flatMap(Deck::drawFirstCard)
                    .ifPresent(discardedCards::add);
        }
        if (size() == 0) {
            Opponent.getInstance().setWinner();
            Game.getInstance().setEnded();
        }
        return discardedCards;
    }

    /**
     * Gets the first deck of the set containing at least one card
     *
     * @return an optional eventually containing the found deck
     */
    private Optional<Deck<DevelopmentCard>> firstDeckWithAtLeastOne() {
        for (Deck deck : decks) {
            if (deck.size() >= 1)
                return Optional.of(deck);
        }
        return Optional.empty();
    }

    /**
     * Displays the first card of each deck of the set
     *
     * @return collection containing the visible cards
     */
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

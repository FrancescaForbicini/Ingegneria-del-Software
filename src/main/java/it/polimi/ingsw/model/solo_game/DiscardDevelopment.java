package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.turn_taker.Opponent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Discards a card in a `DevelopmentCard` deck
 */
public class DiscardDevelopment implements SoloToken {
    private final DevelopmentColor color;
    public DiscardDevelopment(DevelopmentColor color){ this.color = color;}

    /**
     * Discards the development card in base of the color
     *
     * @param opponent the black cross of the opponent
     */
    @Override
    public void use(Opponent opponent) {
        //Deck<DevelopmentCard> deck =Game.getInstance().getDevelopmentDeck(color);
        //Game.getInstance().discardTwoDevelopmentCards(deck);
    }



}

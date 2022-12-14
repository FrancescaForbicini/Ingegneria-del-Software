package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Discards a leader card
 */
public class DiscardLeaderCard implements TurnAction{
    private final LeaderCard cardToDiscard;


    public DiscardLeaderCard(LeaderCard cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    /**
     * {@inheritDoc}
     *
     * Discards a leader cards
     *
     * @param player player that wants to discard a leader card
     */
    @Override
    public void play(Player player) {
        player.discardLeaderCard(cardToDiscard);
    }

}

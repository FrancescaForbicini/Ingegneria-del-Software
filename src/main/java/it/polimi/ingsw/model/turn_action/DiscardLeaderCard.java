package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;


public class DiscardLeaderCard implements TurnAction{
    private LeaderCard cardToDiscard;

    public DiscardLeaderCard(LeaderCard cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void play(Player player) {
        player.discardLeaderCard(cardToDiscard);
    }

}

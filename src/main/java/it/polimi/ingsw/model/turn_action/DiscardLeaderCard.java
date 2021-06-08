package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;


public class DiscardLeaderCard implements TurnAction{
    private ArrayList<LeaderCard> cardToDiscard;

    public DiscardLeaderCard(ArrayList<LeaderCard> cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void play(Player player) {
        player.discardLeaderCard(cardToDiscard);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

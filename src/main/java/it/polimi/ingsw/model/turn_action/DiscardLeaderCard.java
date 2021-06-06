package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.turn_taker.Player;

public class DiscardLeaderCard implements TurnAction{
    @Override
    public void play(Player player) {
//        // TODO
//        player.discardLeaderCard();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

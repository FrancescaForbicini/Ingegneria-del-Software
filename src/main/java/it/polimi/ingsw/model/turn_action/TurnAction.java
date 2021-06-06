package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.turn_taker.Player;

public interface TurnAction {
    void play(Player player);
    boolean isFinished();
}

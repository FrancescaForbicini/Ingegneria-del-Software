package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.turn_taker.Player;

public interface TurnAction {
    public void play(Player player);
    public boolean isFinished();
}

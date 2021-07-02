package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Represents an action that can be be done only during the player's turn
 */
public interface TurnAction {
    /**
     * Plays this after checking if it is possible
     * @param player whom to apply the action to
     */
    void play(Player player);
}

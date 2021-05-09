package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.turn_taker.Opponent;

/**
 * Action that can be take by an `Opponent`
 */
public interface SoloToken {
    /**
     * Performs the action
     */
    void use(Opponent opponent);
}

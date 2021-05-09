package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Moves the black cross (the `Opponent`) in the FaithTrack
 */
public class MoveBlackCross implements SoloToken {

    /**
     * Moves the black cross of two steps forward
     *
     * @param opponent the black cross of the opponent
     */
    @Override
    public void use( Opponent opponent) {
        Game.getInstance().getFaithTrack().move(opponent,2);
    }

}

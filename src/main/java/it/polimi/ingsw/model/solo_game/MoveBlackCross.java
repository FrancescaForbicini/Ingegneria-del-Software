package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;

/**
 * Moves the black cross (the `Opponent`) in the FaithTrack
 */
public class MoveBlackCross implements SoloToken {
    public final int steps;

    public MoveBlackCross(int steps) {
        this.steps = steps;
    }

    /**
     * Moves the black cross of two steps forward

     */
    @Override
    public void use() {
        Game.getInstance().getFaithTrack().moveOpponent(steps);
    }

    @Override
    public String toString() {
        return "MoveBlackCross{" +
                "steps=" + steps +
                '}';
    }
}

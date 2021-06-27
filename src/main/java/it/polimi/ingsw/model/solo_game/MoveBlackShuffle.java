package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Opponent;

/**
 * Shuffles the deck of the SoloToken and to move the black cross in the FaithTrack
 */
public class MoveBlackShuffle implements SoloToken {
    public final int steps;
    public final boolean shuffle;

    public MoveBlackShuffle(int steps, boolean shuffle) {
        this.steps = steps;
        this.shuffle = shuffle;
    }

    /**
     * Moves the black cross of one step forwards and shuffles the solo token decks
     */
    @Override
    public void use() {
        Game.getInstance().getFaithTrack().moveOpponent(steps);
        if (shuffle)
            Opponent.getInstance().resetDecks();
    }

    @Override
    public String toString() {
        return "MoveBlackShuffle{" +
                "steps=" + steps +
                ", shuffle=" + shuffle +
                '}';
    }
}

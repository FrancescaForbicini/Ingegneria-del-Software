package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Opponent;

/**
 * Shuffles the deck of the SoloToken and to move the black cross in the FaithTrack
 */
public class MoveBlackShuffle implements SoloToken {

    /**
     * Moves the black cross of one step forwards and shuffles the solo token decks
     *
     * @param opponent the black cross of the opponent
     */
    @Override
    public void use( Opponent opponent) {
        Game.getInstance().getFaithTrack().move(opponent,1);
        opponent.resetDecks();
        opponent.getSoloTokens().shuffle();
    }
}

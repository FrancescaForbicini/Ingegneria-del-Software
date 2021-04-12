package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;

/**
 * Shuffles the deck of the SoloToken and to move the black cross in the FaithTrack
 */
public class MoveBlackShuffle implements SoloTokenStrategy {
    @Override
    public void use() {
        Game.getInstance().getOpponent().ifPresent(o -> o.resetDecks());
        // TODO
        // FaithTrack.move(game.opponent,1);
    }
}

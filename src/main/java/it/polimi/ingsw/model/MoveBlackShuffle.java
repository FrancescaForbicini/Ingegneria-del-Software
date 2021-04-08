package it.polimi.ingsw.model;

public class MoveBlackShuffle implements SoloTokenStrategy{
    /**Used to shuffle the deck of the SoloToken and to move the black cross in the FaithTrack
     *
     */
    @Override
    public void use() {
        Game.getsoloTokens().addAll(game.getdiscardedSoloTokens());
        Game.getsoloTokens().shuffle();
        FaithTrack.move(game.opponent,1);
    }
}

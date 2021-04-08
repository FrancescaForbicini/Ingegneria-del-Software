package it.polimi.ingsw.model;

public class MoveBlackCross implements SoloTokenStrategy{
    /** Used to move the black cross in the FaithTrack
     *
     */
    @Override
    public void use() {
        FaithTrack.move(Game.opponent,2);
    }
}

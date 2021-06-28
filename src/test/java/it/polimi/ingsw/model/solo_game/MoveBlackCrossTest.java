package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.turn_taker.Opponent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MoveBlackCrossTest {
    private MoveBlackCross moveBlackCross;
    private Game game;
    private Opponent opponent;
    private int stepToMove;
    private FaithTrack faithTrack;

    @Before
    public void setUp(){
        stepToMove = 2;
        moveBlackCross = new MoveBlackCross(stepToMove);
        game = Game.getInstance();
        opponent = Opponent.getInstance();
        game.addPlayer(opponent.getUsername());
        faithTrack = game.getFaithTrack();
    }

    @Test
    public void moveBlackCross(){
        faithTrack.move(opponent,stepToMove);
        moveBlackCross.use();
        assertEquals(faithTrack.getPosition(opponent),Game.getInstance().getFaithTrack().getPosition(opponent));
    }
    @After
    public void clean(){
        Game.getInstance().clean();
    }
}
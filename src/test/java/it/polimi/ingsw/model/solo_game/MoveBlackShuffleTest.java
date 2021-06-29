package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.turn_taker.Opponent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoveBlackShuffleTest {
    private Game game;
    private Opponent opponent;
    private Deck<SoloToken> soloToken;
    private FaithTrack faithTrack;
    private MoveBlackShuffle moveBlackShuffle;
    private int stepToMove;

    @Before
    public void setUp(){
        stepToMove = 1;
        game = Game.getInstance();
        faithTrack = game.getFaithTrack();
        opponent = Opponent.getInstance();
        game.addPlayer(opponent.getUsername());
        soloToken = opponent.getSoloTokens();
    }

    @Test
    public void moveBlackShuffle(){
        moveBlackShuffle = new MoveBlackShuffle(stepToMove,true);
        faithTrack.move(opponent,stepToMove);
        assertEquals(faithTrack.getPosition(opponent),game.getFaithTrack().getPosition(opponent));
        assertEquals(opponent.getSoloTokens().size(),Opponent.getInstance().getSoloTokens().size());
    }
    @After
    public void clean(){
        Game.getInstance().clean();
    }
}
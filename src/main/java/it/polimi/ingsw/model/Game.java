package it.polimi.ingsw.model;

import java.util.Collection;
import java.util.Optional;

public class Game {
    //TODO players:Collection<Player>
    //TODO opponent: Optional<Opponent>
    // TODO currentPlayer: TurnTaker
    private Deck<DevelopmentCard>developmentCardDecks;
    private Deck<LeaderCardStrategy> leaderCards;
    private Optional<Deck <SoloTokenStrategy> > soloTokens;
    private boolean endend = false;
    private Game instance = null;

    /** Initaliazes the decks, the players and the current player.
     *
     */
    public Game() {
        this.developmentCardDecks = developmentCardDecks;
        this.leaderCards = leaderCards;
        this.soloTokens = soloTokens;
        this.endend = endend;
    }
    public void getInstance(){
        if (instance== null )
            instance=new Game();
    }
    public void getPlayerByUsername (String username){
        //TODO Player instead void
    }
    public boolean isEnded(){
        return this.endend;
    }
    public void setEnded(boolean ended){
        this.endend=ended;
    }
    //TODO getCurrentPlayer: TurnTaker

}

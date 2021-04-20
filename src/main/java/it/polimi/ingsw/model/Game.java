package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.solo_game.SoloTokenStrategy;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;
import java.util.Optional;

public class Game {
    private Collection<Player> players;
    private Optional<Opponent> opponent;
    private Deck<DevelopmentCard> developmentCardDecks[][];
    private Deck<LeaderCardStrategy> leaderCards;
    private boolean ended = false;

    // TODO test in a real multi thread env
    private static ThreadLocal<Game> instance = ThreadLocal.withInitial(() -> new Game());

    /**
     * Initializes the game using appropriate settings
     */
    private Game() {
        // TODO properly initialize with SETTINGS
    }

    /**
     * Returns the thread local singleton instance
     */
    public static Game getInstance() {
        return instance.get();
    }
    public Deck<LeaderCardStrategy> getLeaderCardStrategy(){
        return leaderCards;
    }

    /**
     * Gets a player by a username
     *
     * @param username the username of the player
     * @return Optionally, the player which match the username given
     */
    public Optional<Player> getPlayerByUsername (String username){
        return players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst();
    }

    /**
     * Gets all players of the game
     * @return a collection of the players of the game
     */
    public Collection<Player> getPlayers(){
        return players;
    }

    /**
     * Checks if the game is ended
     *
     * @return The state of the game
     */
    public boolean isEnded(){
        return this.ended;
    }

    /**
     * Sets the game as ended
     */
    public void setEnded() {
        this.ended = true;
    }

    public Optional<Opponent> getOpponent() {
        return opponent;
    }
}

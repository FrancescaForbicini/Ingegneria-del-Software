package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.solo_game.SoloTokenStrategy;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;

public class Game {
    private Settings settings;
    private ConcurrentLinkedDeque<Player> players;//HashSet maybe?
    private Optional<Opponent> opponent;
    private ArrayList<ArrayList<Deck<DevelopmentCard>>> developmentCardDecks;
    private Deck<LeaderCardStrategy> leaderCards;
    private FaithTrack faithTrack;
    private Market market;
    private boolean ended = false;

    private static final ThreadLocal<Game> instance = ThreadLocal.withInitial(Game::new);

    /**
     * Returns the thread local singleton instance
     */
    public static Game getInstance() {
        return instance.get();
    }

    /**
     * Initializes the game using appropriate settings
     */
    private Game() {
        players = new ConcurrentLinkedDeque<>();
    }


    private void createDevelopmentCardDecks(ArrayList<DevelopmentCard> cards){
        developmentCardDecks = new ArrayList<>();
        for(int i=0;i<4;i++){
            developmentCardDecks.add(new ArrayList<>());
            for(int j=0;j<3;j++){
                developmentCardDecks.get(i).add(new Deck<>());
            }
        }
        Collections.shuffle(cards);
        for (DevelopmentCard card : cards) {
            DevelopmentColor color = card.getColor();
            int level = card.getLevel();
            switch (color) {
                case Green:
                    developmentCardDecks.get(0).get(level).addCard(card);
                    break;
                case Blue:
                    developmentCardDecks.get(1).get(level).addCard(card);
                    break;
                case Yellow:
                    developmentCardDecks.get(2).get(level).addCard(card);
                    break;
                case Purple:
                    developmentCardDecks.get(3).get(level).addCard(card);
                    break;
            }
        }
    }

    /**
     * Gets a player by a username
     *
     * @param username the username of the player
     * @return Optionally, the player which match the username given
     */
    public Optional<Player> getPlayerByUsername (String username){
        return players.stream()
                .filter(p -> p.getUsername() == username)
                .findFirst();
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

    public Market getMarket(){
        return market;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
    public Deck<LeaderCardStrategy> getLeaderCards(){
        return leaderCards;
    }

    public void addPlayer(String username) {
        players.add(new Player(username));
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public Stream<String> getPlayersNames(){
        return players.stream().map(Player::getUsername);
    }
}

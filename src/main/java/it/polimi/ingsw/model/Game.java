package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.*;
import java.util.stream.Stream;

public class Game {
    private Settings settings;
    private List<Player> players;
    private Optional<Opponent> opponent;
    private ArrayList<ArrayList<Deck<DevelopmentCard>>> developmentCardDecks;
    private Deck<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private Market market;
    private boolean ended = false;
    private final String gameID;

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
        players = new ArrayList<>();
        gameID = Thread.currentThread().getName();
        settings = Settings.getInstance();
        leaderCards = new Deck<>(settings.getLeaderCards());
        faithTrack = FaithTrack.getInstance();
        market = Market.getInstance();
        initializeDevelopmentCardDecks(settings.getDevelopmentCards());
    }

    public String getGameID(){
        return gameID;
    }

    public ArrayList<ArrayList<Deck<DevelopmentCard>>>  getDevelopmentCards(){
        return this.developmentCardDecks;
    }

    private void initializeDevelopmentCardDecks(ArrayList<DevelopmentCard> cards){
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
            int level = card.getLevel() - 1;
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
                .filter(p -> p.getUsername().equals(username))
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
    public Deck<LeaderCard> getLeaderCards(){
        return leaderCards;
    }

    public synchronized void addPlayer(String username) {
        Player player = new Player(username);
        players.add(player);
        faithTrack.addNewPlayer(player);
        notifyAll();
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public List<Player> getPlayers(){
        return players;
    }

    public Stream<String> getPlayersNames() {
        return getPlayers().stream().map(Player::getUsername);
    }

}

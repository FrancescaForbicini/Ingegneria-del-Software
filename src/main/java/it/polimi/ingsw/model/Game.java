package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game implements ThreadLocalCleanable {
    private Settings settings;
    private List<TurnTaker> turnTakers;
    private DevelopmentCardColumn developmentCardColumn;
    private Deck<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private Market market;
    private boolean ended = false;
    private final String gameID;
    private int maxPlayers;
    
    private static ThreadLocal<Game> instance = ThreadLocal.withInitial(Game::new);

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
        turnTakers = new ArrayList<>();
        gameID = Thread.currentThread().getName();
        settings = Settings.getInstance();
        leaderCards = new Deck<>(settings.getLeaderCards());
        faithTrack = FaithTrack.getInstance();
        market = Market.getInstance();
        developmentCardColumn = settings.getDevelopmentCardComuns();
    }

    public String getGameID() {
        return gameID;
    }

    public ArrayList<ArrayList<Deck<DevelopmentCard>>> getDevelopmentCards() {
        return this.developmentCardDecks;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean removeDevelopmentCard(DevelopmentCard developmentCard) {
        for (ArrayList<Deck<DevelopmentCard>> decks : getDevelopmentCards()) {
            for (Deck<DevelopmentCard> developmentCardDeck : decks) {
                if (developmentCardDeck.showFirstCard().isPresent() && developmentCardDeck.showFirstCard().get().equals(developmentCard)) {
                    developmentCardDeck.drawFirstCard();
                    return true;
                }
            }
        }
        return false;
    }

    public void removeDevelopmentCards(DevelopmentColor color, int numberToRemove) {

    }

    /**
     * Gets a player by a username
     *
     * @param username the username of the player
     * @return Optionally, the player which match the username given
     */
    public Optional<Player> getPlayerByUsername(String username) {
        return getPlayers().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst();
    }

    /**
     * Checks if the game is ended
     *
     * @return The state of the game
     */
    public boolean isEnded() {
        return this.ended;
    }

    /**
     * Sets the game as ended
     */
    public void setEnded() {
        this.ended = true;
    }

    public Market getMarket() {
        return market;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public Deck<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public synchronized void addPlayer(String username) {
        Player player = new Player(username);
        turnTakers.add(player);
        faithTrack.addNewPlayer(player);
        notifyAll();
    }

    public int getPlayersNumber() {
        return turnTakers.size();
    }

    public List<TurnTaker> getTurnTakers() {
        return turnTakers;
    }

    public List<Player> getPlayers() {
        return turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(Player.class))
                .map(turnTaker -> (Player)turnTaker)
                .collect(Collectors.toList());

    }

    public Stream<String> getPlayersNames() {
        return getTurnTakers().stream().map(TurnTaker::getUsername);
    }

    public Optional<TurnTaker> computeWinner() {
        if (!ended)
            return Optional.empty();
        return turnTakers.stream().max(Comparator.comparing(TurnTaker::computeScore));
    }

    @Override
    public void clean() {
        instance.remove();
    }

    public void setupPlayers() {
        getPlayers().forEach(Player::loadFromSettings);
    }


    public void setupSoloGame() {
        Opponent opponent = new Opponent();
        turnTakers.add(opponent);
        faithTrack.addNewPlayer(opponent);
    }

}

package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.DevelopmentCardColumn;
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

/**
 * Representation of the entire game status
 */
public class Game implements ThreadLocalCleanable {
    private Settings settings;
    private List<TurnTaker> turnTakers;
    private DevelopmentCardColumn[] developmentCardColumns;
    private Deck<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private Market market;
    private boolean ended = false;
    private boolean corrupted = false;
    private final String gameID;
    private int maxPlayers;
    
    private static ThreadLocal<Game> instance = ThreadLocal.withInitial(Game::new);

    public DevelopmentCardColumn[] getDevelopmentCardColumns() {
        return developmentCardColumns;
    }

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
        setupDevelopmentCardColumns(settings.getDevelopmentCards());
    }

    public String getGameID() {
        return gameID;
    }


    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Creates the DevelopmentCardColumns
     *
     * @param developmentCards to be distributed inside the columns
     */
    public void setupDevelopmentCardColumns(ArrayList<DevelopmentCard> developmentCards) {
        developmentCardColumns = new DevelopmentCardColumn[4];
        ArrayList<DevelopmentCardColumn> developmentCardColumnsList = new ArrayList<>();
        developmentCards.stream()
                .collect(Collectors.groupingBy(DevelopmentCard::getColor))
                .forEach((developmentColor, developmentCardsPerColor) ->
                        developmentCardColumnsList.add(
                                new DevelopmentCardColumn(
                                        developmentColor,
                                        (ArrayList<DevelopmentCard>) developmentCardsPerColor
                                )
                        )
                );
        developmentCardColumns = developmentCardColumnsList.stream().toArray(DevelopmentCardColumn[]::new);
    }

    /**
     * Removes a DevelopmentCard from the ones available to be bought
     *
     * @param developmentCard to be removed
     */
    public void removeDevelopmentCard(DevelopmentCard developmentCard) {
        DevelopmentColor color = developmentCard.getColor();
        Arrays.stream(developmentCardColumns)
                .filter(developmentCardColumn -> developmentCardColumn.getColor().equals(color)).findFirst().get()
                .removeIfPresent(developmentCard);
    }

    /**
     * Removes a certain amount of DevelopmentCards of a certain color
     *
     * @param color to be matched to be removed
     * @param numberToRemove amount to be removed
     * @return available cards updated after removing the requested amount
     */
    public ArrayList<DevelopmentCard> removeDevelopmentCards(DevelopmentColor color, int numberToRemove) {
        return Arrays.stream(developmentCardColumns)
                .filter(developmentCardColumn -> developmentCardColumn.getColor().equals(color))
                .findFirst().get()
                .remove(numberToRemove);
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

    public boolean isCorrupted(){
        return this.corrupted;
    }

    public void setCorrupted(){
        this.corrupted = true;
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

    /**
     * Add a new Player to the game
     *
     * @param username unique id to identify the Player
     */
    public synchronized void addPlayer(String username) {
        Player player = new Player(username);
        turnTakers.add(player);
        faithTrack.addNewPlayer(player);
        notifyAll();
    }

    /**
     * Gets the amount of Players playing this
     *
     * @return amount of Players
     */
    public int getPlayersNumber() {
        return turnTakers.size();
    }

    public List<TurnTaker> getTurnTakers() {
        return turnTakers;
    }

    /**
     * Gets all Players without the Opponent
     *
     * @return
     */
    public List<Player> getPlayers() {
        return turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(Player.class))
                .map(turnTaker -> (Player)turnTaker)
                .collect(Collectors.toList());

    }

    /**
     * Gets all the Players' names
     *
     * @return stream of usernames
     */
    public Stream<String> getPlayersNames() {
        return getTurnTakers().stream().map(TurnTaker::getUsername);
    }

    /**
     * Determines who is the winner
     *
     * @return winner
     */
    public Optional<TurnTaker> computeWinner() {
        if (!ended || corrupted)
            return Optional.empty();
        return turnTakers.stream().max(Comparator.comparing(TurnTaker::computeScore));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        instance.remove();
    }

    /**
     * Create PersonalBoard for each Player
     */
    public void setupPlayers() {
        getPlayers().forEach(Player::createPersonalBoard);
    }

    /**
     * Setups the SoloGame
     */
    public void setupSoloGame() {
        Opponent opponent = Opponent.getInstance();
        turnTakers.add(opponent);
        faithTrack.addNewPlayer(opponent);
    }
}

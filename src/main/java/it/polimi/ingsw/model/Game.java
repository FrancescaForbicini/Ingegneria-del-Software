package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    private Settings settings;
    private Collection<Player> players;
    private Optional<Opponent> opponent;
    private ArrayList<ArrayList<Deck<DevelopmentCard>>> developmentCardDecks;
    private Deck<LeaderCardStrategy> leaderCards;
    private Deck<SoloToken> soloToken;
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
        // TODO properly initialize with SETTINGS
    }


    /**
     * Sets the game
     * @param settingsFileName file where the components of the Game are created
     */
    public void setUp(String settingsFileName){
        settings = new Settings(settingsFileName);
        market = new Market(settings.getMarbles());
        faithTrack = settings.getFaithTrack();
        createDevelopmentCardDecks(settings.getDevelopmentCards());
        if(settings.isSoloGame()){
            opponent = Optional.of(new Opponent());
        }
        for(LeaderCardStrategy card : settings.getLeaderCards()){
            leaderCards.addCard(card);
        }
    }

    /**
     * Gets the deck of development cards that has a specific color
     * @param deck the deck that has cards of the color required
     * @return the color of the cards of the deck
     */
    private Optional<DevelopmentColor> getColor(Deck<DevelopmentCard> deck){
        if (deck.showFirstCard().isPresent())
            return Optional.ofNullable(deck.showFirstCard().get().getColor());
        else
            return Optional.empty();
    }

    /**
     * Select the deck in base of the color of the two development cards that have to be discarded
     * @return the deck from which the player has to discard the development cards
     */
    public Deck<DevelopmentCard> getDevelopmentDeck(DevelopmentColor color){
        ArrayList<Deck<DevelopmentCard>> rightColumn = new ArrayList<>();
        for (ArrayList<Deck<DevelopmentCard>> colorColumn : developmentCardDecks){
            if (rightColumn.size() == 0) {
                for (Deck<DevelopmentCard> deck : colorColumn) {
                    if (deck.showFirstCard().isPresent()) {
                        if (deck.showFirstCard().get().getColor().equals(color)) {
                            rightColumn.addAll(colorColumn);
                            break;
                        }
                    }
                }
            }
        }
        Deck<DevelopmentCard> rightDeck = new Deck<>();
        int min = 3;
        for (Deck<DevelopmentCard> deck : rightColumn){
            if (deck.showFirstCard().isPresent()) {
                if (deck.showFirstCard().get().getLevel()<min) {
                    rightDeck = deck;
                }
            }
        }
        return rightDeck;
    }

    /**
     * Discards two developments card from the development card deck
     * @param deck the development card deck
     */
    public void discardTwoDevelopmentCards(Deck<DevelopmentCard> deck) {
        for (int i = 0; i < 2; i++) {
            if (deck.drawFirstCard().isEmpty()) {
                setEnded();
                break;
            }
        }
    }

   
    /**
     * Select the deck in base of the color of the two development cards that have to be discarded
     * @return the deck from which the player has to discard the development cards
     */
    public Deck<DevelopmentCard> getDevelopmentDeck(DevelopmentColor color){
        ArrayList<Deck<DevelopmentCard>> rightColumn = new ArrayList<>();
        for (ArrayList<Deck<DevelopmentCard>> colorColumn : developmentCardDecks){
            if (rightColumn.size() == 0) {
                for (Deck<DevelopmentCard> deck : colorColumn) {
                    if (deck.showFirstCard().isPresent()) {
                        if (deck.showFirstCard().get().getColor().equals(color)) {
                            rightColumn.addAll(colorColumn);
                            break;
                        }
                    }
                }
            }
        }
        Deck<DevelopmentCard> rightDeck = new Deck<>();
        int min = 3;
        for (Deck<DevelopmentCard> deck : rightColumn){
            if (deck.showFirstCard().isPresent()) {
                if (deck.showFirstCard().get().getLevel()<min) {
                    rightDeck = deck;
                }
            }
        }
        return rightDeck;
    }

    /**
     * Discards two developments card from the development card deck
     * @param deck the development card deck
     */
    public void discardTwoDevelopmentCards(Deck<DevelopmentCard> deck) {
        for (int i = 0; i < 2; i++) {
            if (deck.drawFirstCard().isEmpty()) {
                setEnded();
                break;
            }
        }
    }

    /**
     * Creates the development card decks
     * @param cards the cards to add to the deck
     **/
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

    public Deck<SoloToken> getSoloToken (){ return soloToken; }

    public ArrayList<ArrayList<Deck<DevelopmentCard>>> getDevelopmentCardDecks (){
        return this.developmentCardDecks;
    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Settings;
import it.polimi.ingsw.server.GamesRegistry;

import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);
    private final Settings settings;
    private final Game game;


    /**
     * Returns the thread local singleton instance
     */
    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {
        settings = Settings.getInstance();
        game = Game.getInstance();
    }

    private boolean waitForPlayers(){
        LOGGER.info("Waiting for players to join");
        try {
            synchronized (game) {
                while (game.getPlayersNumber() < settings.getMaxPlayers()) {
                    LOGGER.info("Not enough players, waiting for players to join...");
                    game.wait();
                }
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


    // TODO ENTRY POINT OF THE GAME, WHEN THIS METHOD ENDS, THE THREAD DIES
    public void startGame(String gameId) {
        GamesRegistry.getInstance().addThreadLocalGame(gameId);

        if (!waitForPlayers()) {
            LOGGER.info("Game interrupted. Exiting...");
            return;
        }

//        setUpGame();
//
//        pickLeaderCards();
//
//        notifyPlayers();
//
//        // TODO notify that game is started, send initial state to players!
//        playGame();
        LOGGER.info("Game finished.");
        // TODO SUPER TODO CLEAN thread locals
    }

    private void setUpGame(){
        // TODO setup players, and game completely, players are all presents...
        // TODO do stuff, pick cards etc
        // TODO change this
//        Market market = new Market(settings.getMarbles());
//        FaithTrack faithTrack = settings.getFaithTrack();
//        //createDevelopmentCardDecks(settings.getDevelopmentCards());
//        if(settings.isSoloGame()){
//            opponent = Optional.of(new Opponent());
//        }
//        for(LeaderCard card : settings.getLeaderCards()){
//            leaderCards.addCard(card);
//        }
    }

    private void pickLeaderCards() {
        // TODO:
        //  1) shuffles players
        //  2) give players leader cards
        //  3) notify players
        //  4) wait for responses
    }


    private void notifyPlayers() {
        // TODO:
        //  1) Notify players of the starts
    }


    public void playGame() {
        // TODO main cycle, while game is not finished
    }

    public void addPlayer(String username) {
        game.addPlayer(username);
        LOGGER.info(String.format("Waiting players: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
    }

}

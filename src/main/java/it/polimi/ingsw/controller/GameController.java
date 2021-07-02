package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import it.polimi.ingsw.server.GamesRegistry;
import it.polimi.ingsw.server.VirtualView;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);
    private Settings settings;
    private Game game;
    private final VirtualView virtualView;
    private boolean gameStarted;

    /**
     * Returns the thread local singleton instance
     */
    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {
        virtualView = VirtualView.getInstance();
        virtualView.setGameController(this);
        gameStarted = false;
    }


    /**
     * Checks if there are players to wait in order to start the game
     *
     * @return true iff the are all players to start the game
     */
    private boolean waitForPlayers(){
        LOGGER.info("Waiting for players to join");
        try {
            synchronized (game) {
                while (game.getPlayersNumber() < game.getMaxPlayers()) {
                    LOGGER.info("Not enough players, waiting for players to join...");
                    LOGGER.info(String.format("Waiting players: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
                    game.wait();
                }
            }
            gameStarted = true;
            game.setupPlayers();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * Runs the thread
     *
     * @param gameID ID of the game
     * @param maxPlayers amount of player of the game
     */
    public void runGame(String gameID, int maxPlayers){
        Thread.currentThread().setName(gameID);
        game = Game.getInstance();
        game.setMaxPlayers(maxPlayers);
        settings = Settings.getInstance();
        GamesRegistry.getInstance().addThreadLocalGame(gameID);
        startGame();
        cleanupThreadLocal();
    }


    /**
     *  Starts the game
     *
     */
    private void startGame() {
        if (!waitForPlayers()) {
            LOGGER.info("Game interrupted. Exiting...");
            return;
        }
        LOGGER.info("\n\n--- SETTING UP GAME ---\n\n");
        LOGGER.info(String.format("Players are: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
        setUpGame();
        LOGGER.info("\n\n--- GAME STARTED ---\n\n");
        playGame();
        LOGGER.info("\n\n--- GAME FINISHED ---\n\n");
    }


    /**
     * Sets up the game
     *
     */
    private void setUpGame() {
        LOGGER.info(String.format("Setting up game with id: '%s'", game.getGameID()));
        LOGGER.info("Shuffling players");
        Collections.shuffle(game.getTurnTakers());
        if (game.getTurnTakers().size() == 1) {
            LOGGER.info("Setting up a solo game");
            game.setupSoloGame();
        }
        LOGGER.info("Notifying initial state of the game");
        virtualView.notifyGameData();
        virtualView.notifyGameData(GameStatus.SETUP);
        LOGGER.info("Serving cards");
        serveCards();
        virtualView.notifyGameData();

        LOGGER.info("Serving starting resources");
        pickStartingResources();
        virtualView.notifyGameData();

        LOGGER.info("Starting the game");
        virtualView.notifyGameData(GameStatus.START);
    }


    /**
     * Serves the leader cards at the beginning of the game
     *
     */
    private void serveCards() {
        Deck<LeaderCard> leaderCardDeck = game.getLeaderCards();
        leaderCardDeck.shuffle();
        game.getPlayers().forEach(player -> virtualView.serveCards(player, leaderCardDeck.drawFourCards()));

        LOGGER.info("Waiting for players to pick the cards");
        Map<String, PickStartingLeaderCardsDTO> pickLeaderCardsDTOs = virtualView.getSelectedLeaderCards();

        LOGGER.info("Setting picked cards to related players");
        pickLeaderCardsDTOs.forEach((username, pickStartingLeaderCardsDTO) -> game.getPlayerByUsername(username).get().setNonActiveLeaderCards(pickStartingLeaderCardsDTO.getCards()));
    }

    /**
     * Asks to pick the starting resources at the beginning of the game
     *
     */
    private void pickStartingResources() {
        virtualView.askForStartingResources();
        virtualView.addStartingResources();
    }


    /**
     * Plays the game
     *
     */
    public void playGame() {
        while (!game.isEnded()) {
            game.getTurnTakers().forEach(TurnTaker::playTurn);
        }
        virtualView.notifyGameFinished();
    }


    /**
     * Adds a player to the game
     *
     * @param username username of the player to add
     */
    public void addPlayer(String username) {
        game.addPlayer(username);
    }

    /**
     * Cleans local thread
     *
     */
    public void cleanupThreadLocal() {
        Game.getInstance().clean();
        FaithTrack.getInstance().clean();
        Market.getInstance().clean();
        Opponent.getInstance().clean();
    }

    /**
     * Checks if the game is started
     *
     * @return true iff the game is started
     */
    public boolean isGameStarted() {
        return gameStarted;
    }
}

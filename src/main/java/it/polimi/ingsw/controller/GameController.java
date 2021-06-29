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
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


// TODO move "notify" methods on virtual view??

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

    public void runGame(String gameID, int maxPlayers){
        Thread.currentThread().setName(gameID);
        game = Game.getInstance();
        game.setMaxPlayers(maxPlayers);
        settings = Settings.getInstance();
        GamesRegistry.getInstance().addThreadLocalGame(gameID);
        startGame();
        cleanupThreadLocal();
    }

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



    private void serveCards() {
        Deck<LeaderCard> leaderCardDeck = game.getLeaderCards();
        leaderCardDeck.shuffle();
        game.getPlayers().forEach(player -> virtualView.serveCards(player, leaderCardDeck.drawFourCards()));

        LOGGER.info("Waiting for players to pick the cards");
        Map<String, PickStartingLeaderCardsDTO> pickLeaderCardsDTOs = virtualView.getSelectedLeaderCards();

        //  TODO check that leader exists, picked are 2 in 4 proposed, validate
        LOGGER.info("Setting picked cards to related players");
        pickLeaderCardsDTOs.forEach((username, pickStartingLeaderCardsDTO) -> game.getPlayerByUsername(username).get().setNonActiveLeaderCards(pickStartingLeaderCardsDTO.getCards()));
    }

    private void pickStartingResources() {
        virtualView.askForStartingResources();
        virtualView.addStartingResources();
    }


    public void playGame() {
        while (!game.isEnded()) {
            game.getTurnTakers().forEach(TurnTaker::playTurn);
        }
        virtualView.notifyGameFinished();
    }


    public void addPlayer(String username) {
        game.addPlayer(username);
    }

    public void cleanupThreadLocal() {
        Game.getInstance().clean();
        FaithTrack.getInstance().clean();
        Market.getInstance().clean();
        Opponent.getInstance().clean();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}

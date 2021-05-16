package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.view.VirtualView;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class GamesRegistry {
    private final static Logger LOGGER = Logger.getLogger(GamesRegistry.class.getName());
    public static int MAX_PARALLEL_GAMES = 16;

    private final Map<String, VirtualView> games; // TODO virtual view or game it.polimi.ingsw.controller

    private static GamesRegistry instance;

    private final ThreadPoolExecutor executor;

    private GamesRegistry() {
        LOGGER.info("GamesRegistry starts");
        games = new ConcurrentHashMap<>();  // TODO check
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_PARALLEL_GAMES);
    }

    public static GamesRegistry getInstance() {
        if (instance == null) {
            instance = new GamesRegistry();
        }
        return instance;
    }

    public void addThreadLocalGame(String gameId) {
        games.put(gameId, VirtualView.getInstance());
    }

    // TODO check thread safe-ness
    public boolean subscribe(LoginMessageDTO loginMessage, SocketConnector socketConnector) {
        String username = loginMessage.getUsername();
        String gameId = loginMessage.getGameId();
        Optional<Settings> customSettings = Optional.ofNullable(loginMessage.getCustomSettings());
        VirtualView waitingGame = games.get(gameId);

        LOGGER.info(String.format("Subscribing '%s' to '%s'", username, gameId));
        if (waitingGame == null) {
            LOGGER.info("No game found, creating a new game");

            // TODO settings, write settings to file

            executor.execute(() -> GameController.getInstance().runGame(gameId));
            do {
                waitingGame = games.get(gameId); // TODO no better solutions?
            } while (waitingGame == null);
        }

        return waitingGame.addPlayer(username, socketConnector, customSettings);
    }

}

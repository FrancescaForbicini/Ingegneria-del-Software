package it.polimi.ingsw.server;

import it.polimi.ingsw.client.SocketClientConnector;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.view.VirtualView;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class GamesRegistry {
    private final static Logger LOGGER = Logger.getLogger(GamesRegistry.class.getName());
    public static int MAX_PARALLEL_GAMES = 16;

    private Map<String, VirtualView> games; // TODO virtual view or game controller

    private static GamesRegistry instance;

    private ThreadPoolExecutor executor;

    private GamesRegistry() {
        LOGGER.info("GamesRegistry starts");
        games = new ConcurrentHashMap<>();
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

    public boolean subscribe(String username, String gameId, Socket playerSocket) {
        VirtualView waitingGame = games.get(gameId);
        LOGGER.info(String.format("Subscribing '%s' to '%s'", username, gameId));
        if (waitingGame == null) {
            LOGGER.info(String.format("No game found, creating a new game"));
            executor.execute(() -> GameController.getInstance().startGame(gameId));
            do {
                waitingGame = games.get(gameId); // TODO no better solutions?
            } while (waitingGame == null);
        }
        return waitingGame.addPlayer(username, playerSocket);
    }

}

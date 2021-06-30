package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.model.turn_taker.Opponent;
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

    private final Map<String, VirtualView> games;

    private static GamesRegistry instance;

    private final ThreadPoolExecutor executor;

    private GamesRegistry() {
        LOGGER.info("GamesRegistry starts");
        games = new ConcurrentHashMap<>();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_PARALLEL_GAMES);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down inside...");
        }));
    }

    public static GamesRegistry getInstance() {
        if (instance == null) {
            instance = new GamesRegistry();
        }
        return instance;
    }

    public synchronized void addThreadLocalGame(String gameId) {
        games.put(gameId, VirtualView.getInstance());
        notifyAll();
    }

    public boolean subscribe(LoginMessageDTO loginMessage, SocketConnector socketConnector) {
        String username = loginMessage.getUsername();
        if (username.equals(Opponent.USERNAME)){
            return false;
        }
        String gameId = loginMessage.getGameId();
        int playersNumber = loginMessage.getMaxPlayers();
        Optional<Settings> customSettings = Optional.ofNullable(loginMessage.getCustomSettings());
        VirtualView waitingGame = games.get(gameId);

        LOGGER.info(String.format("Subscribing '%s' to '%s'", username, gameId));
        if (waitingGame == null) {
            LOGGER.info("No game found, creating a new game");
            Settings.writeCustomSettings(customSettings, gameId);
            executor.execute(() -> {
                Thread.currentThread().setName(gameId);
                GameController.getInstance().runGame(gameId, playersNumber);
            });
            synchronized (GamesRegistry.getInstance()) {
                while (games.get(gameId) == null) {
                    try {
                        GamesRegistry.getInstance().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                waitingGame = games.get(gameId);
            }
        }
        if (waitingGame.isGameStarted())
            return false;
        return waitingGame.addPlayer(username, socketConnector);
    }

}

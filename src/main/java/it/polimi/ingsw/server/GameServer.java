package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;


public class GameServer {
    private final static Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    public static final int PORT = 8544;
    public static final int MAX_CLIENT_SYNC_CONNECTIONS = 16;

    private ServerSocket gameServerSocket;
    private Socket userSocket;
    private ThreadPoolExecutor executor;

    public GameServer() throws IOException {
        LOGGER.info("The game server is created");
        gameServerSocket = new ServerSocket(PORT);
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CLIENT_SYNC_CONNECTIONS);
    }

    public void startGameServer() throws IOException {
        LOGGER.info("The game server starts");
        while (true) { // TODO gracefully kill with some command
            userSocket = gameServerSocket.accept();
            LOGGER.info("A client connection is accepted");
            executor.execute(new PlayerLoginHandler(userSocket));
        }
    }
    public static void main(String[] args) throws IOException {
        new GameServer().startGameServer();
    }
}
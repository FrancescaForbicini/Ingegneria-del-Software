package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.message.LoginMessage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

public class PlayerLoginHandler implements Runnable{
    public static int MAX_LOGIN_ATTEMPT = 5;
    private final static Logger LOGGER = Logger.getLogger(PlayerLoginHandler.class.getName());
    private final SocketConnector socketConnector;

    public PlayerLoginHandler(Socket userSocket) throws IOException {
        socketConnector = new SocketConnector(userSocket);
    }


    private Optional<LoginMessage> getValidLogin() {
        LOGGER.info("Waiting for a login request");
        Optional<Message> loginMessage = socketConnector.receiveMessage(LoginMessage.class);
        int i = 0;
        while(loginMessage.isEmpty() && i < MAX_LOGIN_ATTEMPT) {
            LOGGER.warning("Bad request. Waiting for a correct login request");
            loginMessage = socketConnector.receiveMessage(LoginMessage.class);
            i++;
        }
        return loginMessage.map(message -> (LoginMessage) message);
    }

    public boolean subscribePlayer(LoginMessage loginMessage) {
        LOGGER.info(String.format("Login request from '%s' in game with id '%s'", loginMessage.getUsername(), loginMessage.getGameId()));
        return GamesRegistry.getInstance().subscribe(loginMessage, socketConnector);
    }

    @Override
    public void run() {
        Optional<LoginMessage> loginMessageOptional = getValidLogin();
        LoginMessage loginMessageResponse;
        if (loginMessageOptional.isPresent() && subscribePlayer(loginMessageOptional.get())) {
            LOGGER.info(String.format("Successful login[username: %s, gameId: %s]", loginMessageOptional.get().getUsername(), loginMessageOptional.get().getGameId()));
            // Settings.writeCustomSettings(loginMessageOptional.get().getCustomSettings()); TODO empty settings
            loginMessageResponse = loginMessageOptional.get();
        } else {
            loginMessageResponse = LoginMessage.LoginFailed;
        }
        socketConnector.sendMessage(loginMessageResponse);
        // TODO ??
    }
}

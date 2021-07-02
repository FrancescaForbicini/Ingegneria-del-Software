package it.polimi.ingsw.server;

import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.message.MessageDTO;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages the login
 */
public class PlayerLoginHandler implements Runnable{
    public static int MAX_LOGIN_ATTEMPT = 5;
    private final static Logger LOGGER = Logger.getLogger(PlayerLoginHandler.class.getName());
    private final SocketConnector socketConnector;

    public PlayerLoginHandler(Socket userSocket) throws IOException {
        socketConnector = new SocketConnector(userSocket);
    }


    private Optional<LoginMessageDTO> getValidLogin() {
        LOGGER.info("Waiting for a login request");
        Optional<MessageDTO> loginMessage = socketConnector.receiveMessage(LoginMessageDTO.class);
        int i = 0;
        while(loginMessage.isEmpty() && i < MAX_LOGIN_ATTEMPT) {
            LOGGER.warning("Bad request. Waiting for a correct login request");
            loginMessage = socketConnector.receiveMessage(LoginMessageDTO.class);
            i++;
        }
        return loginMessage.map(message -> (LoginMessageDTO) message);
    }

    public boolean subscribePlayer(LoginMessageDTO loginMessage) {
        LOGGER.info(String.format("Login request from '%s' in game with id '%s'", loginMessage.getUsername(), loginMessage.getGameId()));
        return GamesRegistry.getInstance().subscribe(loginMessage, socketConnector);
    }

    @Override
    public void run() {
        Optional<LoginMessageDTO> loginMessageOptional = Optional.empty();
        for (int i = 0; i < MAX_LOGIN_ATTEMPT ; i++) {
            loginMessageOptional = getValidLogin();
            if (loginMessageOptional.isPresent() && subscribePlayer(loginMessageOptional.get())) {
                LOGGER.info(String.format("Successful login[username: %s, gameId: %s]", loginMessageOptional.get().getUsername(), loginMessageOptional.get().getGameId()));
                socketConnector.sendMessage(loginMessageOptional.get());
                break;
            } else {
                socketConnector.sendMessage(LoginMessageDTO.LoginFailed);
                loginMessageOptional = Optional.empty();
            }
        }
        if (loginMessageOptional.isEmpty())
            socketConnector.close();
    }
}

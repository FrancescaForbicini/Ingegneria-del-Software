package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.server.SocketConnector;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class VirtualView {
    private final static Logger LOGGER = Logger.getLogger(VirtualView.class.getName());
    private final ConcurrentHashMap<String, SocketConnector> usersSocketConnectors;
    private static final ThreadLocal<VirtualView> instance = ThreadLocal.withInitial(VirtualView::new);
    private  GameController gameController;

    /**
     * Returns the thread local singleton instance
     */
    public static VirtualView getInstance() {
        return instance.get();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private VirtualView() {
        usersSocketConnectors = new ConcurrentHashMap<>();
    }

    public boolean addPlayer(String username , SocketConnector playerSocket){
        if (usersSocketConnectors.containsKey(username)) {
            LOGGER.info(String.format("Cannot log '%s' in the game, there is another player with the same username", username));
            return false;
        }
        usersSocketConnectors.put(username, playerSocket);
        LOGGER.info(String.format("Adding '%s' to the game.", username));
        gameController.addPlayer(username);
        return true;
    }

    public void sendMessageTo(String username, MessageDTO message) {
        usersSocketConnectors.get(username).sendMessage(message);
    }

    public Optional<MessageDTO> receiveMessageFrom(String username, Type typeOfMessage){
        return usersSocketConnectors.get(username).receiveMessage(typeOfMessage);
    }

    public Optional<MessageDTO> receiveAnyMessageFrom(String username){
        return usersSocketConnectors.get(username).receiveAnyMessage();
    }

}

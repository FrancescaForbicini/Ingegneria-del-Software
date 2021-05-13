package it.polimi.ingsw.view;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Settings;
import it.polimi.ingsw.server.SocketConnector;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class VirtualView {
    private final static Logger LOGGER = Logger.getLogger(VirtualView.class.getName());
    private final ConcurrentHashMap<String, SocketConnector> usersSocketConnectors;
    private static final ThreadLocal<VirtualView> instance = ThreadLocal.withInitial(VirtualView::new);

    /**
     * Returns the thread local singleton instance
     */
    public static VirtualView getInstance() {
        return instance.get();
    }

    private VirtualView() {
        usersSocketConnectors = new ConcurrentHashMap<>();
    }

    public boolean addPlayer(String username, SocketConnector playerSocket, Optional<Settings> customSettings){
        if (usersSocketConnectors.get(username) != null) {
            LOGGER.info(String.format("Cannot log '%s' in the game, there is another player with the same username", username));
            return false;
        }
        Settings.writeCustomSettings(customSettings);
        usersSocketConnectors.put(username, playerSocket);
        LOGGER.info(String.format("Adding '%s' to the game.", username));
        GameController.getInstance().addPlayer(username);
        return true;
    }
}

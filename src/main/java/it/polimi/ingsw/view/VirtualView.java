package it.polimi.ingsw.view;

import it.polimi.ingsw.client.SocketClientConnector;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VirtualView {
    private final static Logger LOGGER = Logger.getLogger(VirtualView.class.getName());
    private List<String> players;
    private static final ThreadLocal<VirtualView> instance = ThreadLocal.withInitial(VirtualView::new);

    /**
     * Returns the thread local singleton instance
     */
    public static VirtualView getInstance() {
        return instance.get();
    }

    private VirtualView() {
        players = new ArrayList<>();
    }

    public boolean addPlayer(String username, Socket playerSocket){
        // TODO
        LOGGER.info(String.format("Adding '%s' to the game", username));
        return false;
    }
}

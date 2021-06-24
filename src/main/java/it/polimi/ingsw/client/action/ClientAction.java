package it.polimi.ingsw.client.action;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;

/**
 * Actions can done each turn
 */
public abstract class ClientAction implements Consumable {
    protected SocketConnector clientConnector;
    protected View view;
    protected ClientGameObserverProducer clientGameObserverProducer;

    public ClientAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        this.clientConnector = clientConnector;
        this.view = view;
        this.clientGameObserverProducer = clientGameObserverProducer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isDoable() {
        return true;
    }
}

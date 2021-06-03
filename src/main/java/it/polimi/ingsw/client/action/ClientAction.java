package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.Action;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public abstract class ClientAction implements Action {
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
}

package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class ClientAction {
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

    public void consumableFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.remove(this);
    }
    public boolean isDoable() {
        return true;
    }

    public abstract void doAction();
}

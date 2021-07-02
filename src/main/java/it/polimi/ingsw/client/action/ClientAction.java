package it.polimi.ingsw.client.action;

import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.connector.Connector;

/**
 * Actions can done each turn
 */
public abstract class ClientAction implements Consumable {
    protected Connector clientConnector;
    protected View view;
    protected ClientGameObserverProducer clientGameObserverProducer;

    public ClientAction(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        this.clientConnector = clientConnector;
        this.view = view;
        this.clientGameObserverProducer = clientGameObserverProducer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * {@inheritDoc}
     *
     * @return always true, needs to be overwritten
     */
    @Override
    public boolean isDoable() {
        return true;
    }
}

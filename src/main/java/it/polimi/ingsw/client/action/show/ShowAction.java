package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Shows something relevant about the status of the game
 */
public abstract class ShowAction extends ClientAction{
    public ShowAction(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);

    }

    /**
     * {@inheritDoc}
     *
     * @param from
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }
}

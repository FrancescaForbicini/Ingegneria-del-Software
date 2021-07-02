package it.polimi.ingsw.client.action.starting;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Lets pick objects at the start of the game
 */
public abstract class StartingAction extends ClientAction {
    public StartingAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * @param from
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.remove(this);
    }
}

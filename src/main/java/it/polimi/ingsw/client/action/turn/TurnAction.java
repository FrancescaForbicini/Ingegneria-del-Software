package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Actions available on each turn
 */
public abstract class TurnAction extends ClientAction {
    public TurnAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Removes the action from the actions available, if it is already done
     *
     * @param from the actions available
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.removeIf(action -> action instanceof TurnAction);
    }

}

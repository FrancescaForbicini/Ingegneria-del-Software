package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Perform an action on a Leader Card
 */
public abstract class LeaderAction extends ClientAction{
    public LeaderAction(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
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

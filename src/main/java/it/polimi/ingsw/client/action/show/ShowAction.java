package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;


public abstract class ShowAction extends ClientAction{
    public ShowAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);

    }

    @Override
    public void consumableFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }
}

package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public interface ClientAction {
    void doAction (SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer);

}

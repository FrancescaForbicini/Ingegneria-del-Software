package it.polimi.ingsw.client;

import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public interface Action {
    void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer);
}

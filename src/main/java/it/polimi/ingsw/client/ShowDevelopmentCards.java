package it.polimi.ingsw.client;

import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowDevelopmentCards implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        view.showDevelopmentCards(clientGameObserverProducer.getDevelopmentCards());
    }
}

package it.polimi.ingsw.client.show;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowMarket implements ClientAction {
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        view.showMarket(clientGameObserverProducer.getMarket());
    }
}
package it.polimi.ingsw.client;

import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowFaithTrack implements ClientAction{
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        view.showFaithTrack(clientGameObserverProducer.getFaithTrack());
    }
}

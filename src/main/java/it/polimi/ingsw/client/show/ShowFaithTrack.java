package it.polimi.ingsw.client.show;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowFaithTrack extends ClientAction {
    public ShowFaithTrack(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        view.showFaithTrack(clientGameObserverProducer.getFaithTrack());
    }
}

package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ShowOpponentLastAction extends ShowAction {
    public ShowOpponentLastAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        if(clientGameObserverProducer.getOpponent().get().getLastAction() != null) {
            view.showMessage("The last action of Lorenzo is: " + clientGameObserverProducer.getOpponent().get().getLastAction());
        } else {
            view.showMessage("Lorenzo hasn't done anything yet");
        }
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        super.consumeFrom(from);
    }
}

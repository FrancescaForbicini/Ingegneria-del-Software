package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Shows the last action of Opponent
 */
public class ShowOpponentLastAction extends ShowAction {
    public ShowOpponentLastAction(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Displays the last action done by the bot (Lorenzo Il Magnifico) during a Solo Game
     *
     */
    @Override
    public void doAction() {
        if(clientGameObserverProducer.getOpponent().get().getLastAction() != null) {
            view.showMessage("The last action of Lorenzo is: " + clientGameObserverProducer.getOpponent().get().getLastAction());
        } else {
            view.showMessage("Lorenzo hasn't done anything yet");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param from
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        super.consumeFrom(from);
    }
}

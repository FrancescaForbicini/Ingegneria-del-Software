package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

/**
 * Shows the market
 */
public class ShowMarket extends ShowAction {
    public ShowMarket(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Displays the market with its current configuration of marbles
     *
     */
    @Override
    public void doAction() {
        view.showMarket(clientGameObserverProducer.getMarket());
    }
}

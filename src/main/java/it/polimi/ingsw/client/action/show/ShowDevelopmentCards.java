package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

/**
 * Shows available Development Cards
 */
public class ShowDevelopmentCards extends ShowAction {

    public ShowDevelopmentCards(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Displays the Development Cards which are available to be bougth at the moment
     *
     */
    @Override
    public void doAction() {
        view.showDevelopmentCards(clientGameObserverProducer.getDevelopmentCards());
    }
}

package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class SortWarehouse extends TurnAction {
    public SortWarehouse(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        Player player = clientGameObserverProducer.getCurrentPlayer();
        clientConnector.sendMessage(new SortWarehouseDTO(view.sortWarehouse(player.getWarehouse())));
    }

    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getCurrentPlayer().getWarehouse().getWarehouseDepots().isEmpty();
    }
}

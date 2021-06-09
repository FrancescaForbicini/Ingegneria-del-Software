package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

public class SortWarehouse extends ClientAction {
    public SortWarehouse(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        Player player = clientGameObserverProducer.getCurrentPlayer();
        clientConnector.sendMessage(new SortWarehouseDTO(view.sortWarehouse(player.getWarehouse())));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }

    @Override
    public boolean isDoable() {
        PersonalBoard personalBoard = clientGameObserverProducer.getCurrentPlayer().getPersonalBoard();
        return !personalBoard.isWarehouseEmpty()  && !personalBoard.isWarehouseFull();
    }
}

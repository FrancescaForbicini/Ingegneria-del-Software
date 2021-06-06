package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.turn_action.TurnAction;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

public class FinishTurn extends ClientAction{
    public FinishTurn(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getActions().stream().anyMatch(action -> action instanceof TurnAction);
    }

    @Override
    public void doAction() {
        clientConnector.sendMessage(new GameStatusDTO(GameStatus.TURN_FINISHED));
        // TODO remove leader action from queue
        // TODO remove
    }

    // TODO instanceof everywhere...
    @Override
    public void consumableFrom(ConcurrentLinkedDeque<ClientAction> from) {
        super.consumableFrom(from);
    }
}

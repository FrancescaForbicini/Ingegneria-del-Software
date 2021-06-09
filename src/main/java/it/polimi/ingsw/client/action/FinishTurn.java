package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.leader.LeaderAction;
import it.polimi.ingsw.client.action.turn.TurnAction;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

public class FinishTurn extends ClientAction {
    public FinishTurn(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getActions().stream().noneMatch(action -> action instanceof TurnAction);
    }

    // TODO
    @Override
    public void doAction() {
        clientConnector.sendMessage(new GameStatusDTO(GameStatus.TURN_FINISHED));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.removeIf(action ->
                action instanceof LeaderAction || action instanceof SortWarehouse);
        from.remove(this);
    }
}


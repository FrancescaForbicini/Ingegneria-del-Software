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
        return clientGameObserverProducer.getActions().stream().anyMatch(action -> action instanceof TurnAction);
    }

    @Override
    public void doAction() {
        clientConnector.sendMessage(new GameStatusDTO(GameStatus.TURN_FINISHED));
        // TODO remove leader action from queue
        // TODO remove
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.removeIf(action ->
                action.getClass().isInstance(LeaderAction.class)
                        || action.getClass().isInstance(TurnAction.class));
        from.remove(this);
    }
}


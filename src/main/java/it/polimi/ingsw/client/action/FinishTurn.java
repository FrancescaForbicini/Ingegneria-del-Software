package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.leader.LeaderAction;
import it.polimi.ingsw.client.action.turn.TurnAction;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Finishes the turn of a player
 */
public class FinishTurn extends ClientAction {
    public FinishTurn(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * Checks if a player can finish his turn
     * @return true iff the turn can be ended
     */
    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getActions().stream().noneMatch(action -> action instanceof TurnAction);
    }

    /**
     * Finishes the turn
     */
    @Override
    public void doAction() {
        clientConnector.sendMessage(new GameStatusDTO(GameStatus.TURN_FINISHED));
    }

    /**
     * Removes this action from the actions available
     * @param from the actions available
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.removeIf(action ->
                action instanceof LeaderAction || action instanceof SortWarehouse);
        from.remove(this);
    }
}


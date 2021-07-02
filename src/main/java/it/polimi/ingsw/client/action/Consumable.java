package it.polimi.ingsw.client.action;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Checks if an action can be done
 */
public interface Consumable {

    /**
     * Checks if the action is doable
     *
     * @return true iff the action is actually doable
     */
    boolean isDoable();

    /**
     * Performs the action
     */
    void doAction();

    /**
     * Removes the actions from its queue
     *
     * @param from queue where the action is removed
     */
    void consumeFrom(ConcurrentLinkedDeque<ClientAction> from);

}

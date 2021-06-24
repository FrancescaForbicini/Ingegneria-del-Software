package it.polimi.ingsw.client.action;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Checks if an action can be done
 */
public interface Consumable {
    boolean isDoable();

    void doAction();

    void consumeFrom(ConcurrentLinkedDeque<ClientAction> from);

}

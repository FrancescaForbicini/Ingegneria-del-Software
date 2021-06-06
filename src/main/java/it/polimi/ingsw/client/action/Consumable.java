package it.polimi.ingsw.client.action;

import java.util.concurrent.ConcurrentLinkedDeque;

public interface Consumable {
    boolean isDoable();

    void doAction();

    void consumeFrom(ConcurrentLinkedDeque<ClientAction> from);

}

package it.polimi.ingsw.client;

import it.polimi.ingsw.message.Message;

public interface ClientConnector {
    boolean send(Message action);
    Message receive();
}

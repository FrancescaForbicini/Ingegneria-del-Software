package it.polimi.ingsw.server;

import it.polimi.ingsw.message.Message;

import java.lang.reflect.Type;
import java.util.Optional;


public interface Connector {
    boolean sendMessage(Message message);
    Optional<Message> receiveMessage(Type cls);
}

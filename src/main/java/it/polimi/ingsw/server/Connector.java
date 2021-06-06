package it.polimi.ingsw.server;

import it.polimi.ingsw.message.MessageDTO;

import java.lang.reflect.Type;
import java.util.Optional;


public interface Connector {
    void sendMessage(MessageDTO messageDTO);
    Optional<MessageDTO> receiveMessage(Type cls);
    Optional<MessageDTO> receiveAnyMessage();
}

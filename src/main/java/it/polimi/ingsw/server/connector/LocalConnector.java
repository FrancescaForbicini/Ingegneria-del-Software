package it.polimi.ingsw.server.connector;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.server.GamesRegistry;
import it.polimi.ingsw.view.VirtualView;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LocalConnector implements Connector {
    private BlockingQueue<MessageDTO> message;  

    public LocalConnector() throws ConnectException {
        message = new ArrayBlockingQueue<>(1);
        new Thread(() -> {
            GameController.getInstance().runGame("local", 1, Optional.of(this));
        }).start();
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        try {
            message.put(messageDTO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<MessageDTO> receiveMessage(Type cls) {
        return receiveAnyMessage();
    }

    @Override
    public Optional<MessageDTO> receiveAnyMessage() {
        try {
            return Optional.of(message.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

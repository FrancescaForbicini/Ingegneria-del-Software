package it.polimi.ingsw.client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
        while (client.getActions().size() > 0) {
            client.performAnAction();
        }
        //TODO I should find  etc on message queue
    }
}


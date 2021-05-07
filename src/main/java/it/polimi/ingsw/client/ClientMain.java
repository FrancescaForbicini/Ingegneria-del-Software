package it.polimi.ingsw.client;

import it.polimi.ingsw.message.LoginMessage;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        // TODO this is only for testing purposes
        ClientConnector clientConnector = new SocketClientConnector();
        System.out.println(clientConnector.send(new LoginMessage("Satoshi Nakamoto", "Testnet")));


    }
}

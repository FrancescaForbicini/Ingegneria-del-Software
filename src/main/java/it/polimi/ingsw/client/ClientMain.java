package it.polimi.ingsw.client;

import it.polimi.ingsw.message.LoginMessage;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        // TODO this is only for testing purposes
        SocketConnector clientConnector = new SocketConnector(new Socket("127.0.0.1", GameServer.PORT));

        // Log-in Sats in testnet
        if (!clientConnector.sendMessage(new LoginMessage("Satoshi Nakamoto", "Testnet"))) {
            System.exit(1);
        }

        LoginMessage loginMessageResponse = (LoginMessage) clientConnector.receiveMessage(LoginMessage.class).get();
        if (loginMessageResponse.getUsername() != null) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login unsuccessful");
        }



    }
}

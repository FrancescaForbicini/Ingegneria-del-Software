package it.polimi.ingsw.client;

import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        // TODO this is only for testing purposes
        SocketConnector clientConnector = new SocketConnector(new Socket("127.0.0.1", GameServer.PORT)); // TODO USER INPUT

        // MESSAGE 1 (sent)
        LoginMessageDTO loginMessageDTO = new LoginMessageDTO("Na", "mainnet"); // TODO USER INPUT
        // Log-in Sats in testnet
        if (!clientConnector.sendMessage(loginMessageDTO)) {
            System.exit(1);
        }

        // MESSAGE 2 (received)
        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        if (loginMessageDTO.equals(LoginMessageDTO.LoginFailed)) {
            System.out.println("Login unsuccessful");
        } else {
            System.out.println("Login successful");
        }

        // MESSAGE 3 (received)
        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        List<LeaderCard> proposedCards = loginMessageDTO.getCards();
        System.out.printf("Proposed cards: %s%n", proposedCards);

        List<LeaderCard> pickedCards = Arrays.asList(proposedCards.get(0), proposedCards.get(1));
        System.out.printf("Picked cards: %s%n", pickedCards);


        // MESSAGE 4 (sent)
        if (!clientConnector.sendMessage(new LoginMessageDTO(null, null, null, pickedCards))) {
            System.exit(1);
        }

        // MESSAGE 5 (received)
        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        if (loginMessageDTO.getCustomSettings() != null) {
            System.out.println("Game started");
        } else {
            System.out.println("Error stating the game");
        }
    }
}

package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public ClientGameObserverProducer gameObserverProducer;
    public SocketConnector clientConnector;

    public View view;

    public Client(){
        view = setupView();
    }

    public void start() throws IOException {
        view.start();
        clientConnector = new SocketConnector(new Socket(view.askIP(), GameServer.PORT));
        String username = login();
        gameObserverProducer = new ClientGameObserverProducer(clientConnector, username);
        new Thread(gameObserverProducer).start();
    }

    private View setupView() {
        Scanner in = new Scanner(System.in);
        String response;
        System.out.print("Choose 'CLI' or 'GUI': ");
        response = in.nextLine();
        System.out.println(response);
        while (!response.equalsIgnoreCase("CLI") &&
                !response.equalsIgnoreCase("GUI")) {
            System.out.println("Error! Choose 'CLI' or 'GUI': ");
            response = in.nextLine();
        }
        if (response.equalsIgnoreCase("CLI"))
            return new CLI();
        //TODO GUI support
        return null;
    }

    private String login() throws IOException {
        // MESSAGE 1 (sent)
        String username = view.askUsername();

        LoginMessageDTO loginMessageDTO = new LoginMessageDTO(username, view.askGameID());

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

        //TODO custom settings, assumption: there is a custom settings file somewhere(convention)

        // MESSAGE 3 (received)
        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        List<LeaderCard> proposedCards = loginMessageDTO.getCards();
        List<LeaderCard> pickedCards = view.pickLeaderCards(proposedCards);

        // MESSAGE 4 (sent)
        if (!clientConnector.sendMessage(new LoginMessageDTO(null, null, null, pickedCards))) {
            System.exit(1);
        }

        // MESSAGE 5 (received)
        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        if (loginMessageDTO.getCustomSettings() != null) {
            view.startGame();
        } else {
            view.errorStartGame();
        }
        return username;
    }

    public ArrayList<ClientAction> getActions(){
        return new ArrayList<>(gameObserverProducer.getActions());
    }

    public void performAnAction() {
        ClientAction action = view.pickAnAction(gameObserverProducer.getActions());
        action.doAction(clientConnector, view, gameObserverProducer);
    }
}

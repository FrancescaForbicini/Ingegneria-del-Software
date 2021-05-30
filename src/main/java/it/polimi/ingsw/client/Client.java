package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.gui.GUI;
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

    private View setupView() {
        Scanner in = new Scanner(System.in);
        String response;
        System.out.print("Choose 'CLI' or 'GUI': ");
        response = in.nextLine();
        while (!response.equalsIgnoreCase("CLI") &&
                !response.equalsIgnoreCase("GUI")) {
            System.out.println("Error! Choose 'CLI' or 'GUI': ");
            response = in.nextLine();
        }
        if (response.equalsIgnoreCase("CLI"))
            return new CLI();
        else
            return new GUI();
    }

    public void start() throws IOException {
        new Thread(()->view.startView()).start();
        String IP = view.askIP();
        clientConnector = new SocketConnector(new Socket(IP, GameServer.PORT));
        String username = login();
        gameObserverProducer = new ClientGameObserverProducer(clientConnector, username);
        new Thread(gameObserverProducer).start();
    }

    private String login() throws IOException {
        // MESSAGE 1 (sent)
        ClientPlayer clientPlayer = view.askCredentials();
        String username = clientPlayer.getUsername();

        LoginMessageDTO loginMessageDTO = new LoginMessageDTO(username, clientPlayer.getGameID());

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
        view.startGame();
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

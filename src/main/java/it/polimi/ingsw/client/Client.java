package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
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
        //view.showStart();
        String IP = view.askIP();
        clientConnector = new SocketConnector(new Socket(IP, GameServer.PORT));
        String username = login();
        gameObserverProducer = new ClientGameObserverProducer(clientConnector, view, username);
        new Thread(gameObserverProducer).start();
    }

    private String login() {
        ClientPlayer clientPlayer = view.askCredentials();
        String username = clientPlayer.getUsername();

        LoginMessageDTO loginMessageDTO = new LoginMessageDTO(username, clientPlayer.getGameID());

        if (!clientConnector.sendMessage(loginMessageDTO)) {
            System.exit(1);
        }

        loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        if (loginMessageDTO.equals(LoginMessageDTO.LoginFailed)) {
            System.out.println("Login unsuccessful");
        } else {
            System.out.println("Login successful");
        }
        return username;
    }


    public ArrayList<ClientAction> getActions(){
        return new ArrayList<>(gameObserverProducer.getActions());
    }

    public void performAnAction() {
        ArrayList<ClientAction> clientActions = new ArrayList<>(gameObserverProducer.getActions());
        view.showAvailableActions(clientActions);
        Optional<ClientAction> action = view.pickAnAction(clientActions);
        if (action.isEmpty())
            return;
        action.get().doAction();
    }
}

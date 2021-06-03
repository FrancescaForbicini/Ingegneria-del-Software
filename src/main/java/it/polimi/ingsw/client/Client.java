package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.setup.LoginMessageDTO;
import it.polimi.ingsw.message.setup.PickStartingResourcesDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
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

    private void pickStartingResources() {
        PickStartingResourcesDTO pickStartingResourcesDTO = (PickStartingResourcesDTO) clientConnector
                .receiveMessage(PickStartingResourcesDTO.class).get();
        int resourceToPick = pickStartingResourcesDTO.getNumber();
        ArrayList<ResourceType> pickedResources;
        if(resourceToPick>0) {
            do {
                if(view.isSceneAlreadySeen()){
                    view.showMessage("Choose the right amount of resources, please");
                }
                pickedResources = view.pickStartingResources(resourceToPick);
                view.setSceneAlreadySeen(true);
            } while (pickedResources.size() > resourceToPick);
        }else{
            pickedResources = new ArrayList<>();
        }
        pickStartingResourcesDTO = new PickStartingResourcesDTO(resourceToPick, pickedResources);
        clientConnector.sendMessage(pickStartingResourcesDTO);
    }

    private void pickCards() throws IOException {
        //TODO custom settings, assumption: there is a custom settings file somewhere(convention)
        LoginMessageDTO loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
        List<LeaderCard> proposedCards = loginMessageDTO.getCards();
        List<LeaderCard> pickedCards = view.pickLeaderCards(proposedCards);
        if (!clientConnector.sendMessage(new LoginMessageDTO(null, null, null, pickedCards))) {
            System.exit(1);
        }
    }
    private String login() {
        // MESSAGE 1 (sent)
        boolean loginAttempt = false;//TODO fix with better logic
        String username;
        do {
            ClientPlayer clientPlayer = view.askCredentials();
            username = clientPlayer.getUsername();

            LoginMessageDTO loginMessageDTO = new LoginMessageDTO(username, clientPlayer.getGameID());

            if (!clientConnector.sendMessage(loginMessageDTO)) {
                System.exit(1);
            }

            // MESSAGE 2 (received)
            loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
            if (loginMessageDTO.equals(LoginMessageDTO.LoginFailed)) {
                view.showMessage("Login unsuccessful: try a different username");
            } else {
                loginAttempt = true;
            }
        }while(!loginAttempt);
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

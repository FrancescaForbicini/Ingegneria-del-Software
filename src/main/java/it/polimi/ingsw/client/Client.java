package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.Credentials;
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
        Thread thread = new Thread(() -> view.startView());
        thread.start();
        String IP = checkIP();
        clientConnector = new SocketConnector(new Socket(IP, GameServer.PORT));
        String username = checkUsername();
        gameObserverProducer = new ClientGameObserverProducer(clientConnector, view, username);
        new Thread(gameObserverProducer).start();
    }
    public boolean isGameActive() {
        return gameObserverProducer.isGameActive();
    }
    /**
     * Checks if the IP inserted
     * @return the correct IP
     */
    private String checkIP(){
        int subIP = 0;
        String IP;
        boolean IPCorrect = false;
        int exp = 2;
        do{
            IP = view.askIP();
            if (IP.equals("localhost") || IP.isEmpty()) {
                IPCorrect = true;
            }
            else
                if (IP.matches("^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}$")) {
                    IPCorrect = true;
                    for (int i = 0; i < IP.length(); i++) {
                        if ((IP.charAt(i)) != '.') {
                            subIP += Integer.parseInt(String.valueOf(IP.charAt(i))) * (int) Math.pow(10, exp);
                            exp--;
                        } else {
                            if (subIP < 0 || subIP >= 256) {
                                IPCorrect = false;
                                break;
                            }
                            subIP = 0;
                            exp = 2;
                        }
                    }
                }
            if (!IPCorrect)
                view.showMessage("Error! Please enter another IP");
        }while (!IPCorrect);
        if (IP.isEmpty())
            return "localhost";
        return IP;
    }
    /**
     * Checks the username inserted
     * @return the correct username
     */
    private String checkUsername() {
        Credentials credentials;
        String username;
        boolean loginSuccessful = false;
        do {
            credentials = view.askCredentials();
            username = credentials.getUsername();
            LoginMessageDTO loginMessageDTO = new LoginMessageDTO(username, credentials.getGameID(), null, credentials.getMaxPlayers());
            clientConnector.sendMessage(loginMessageDTO);
            loginMessageDTO = (LoginMessageDTO) clientConnector.receiveMessage(LoginMessageDTO.class).get();
            if (loginMessageDTO.equals(LoginMessageDTO.LoginFailed)) {
                view.showMessage("Login unsuccessful, please enter another username");
            } else {
                view.showMessage("Login successful!");
                loginSuccessful = true;
            }
        }while (!loginSuccessful);
        return username;
    }

    /**
     * Gets the possible actions that a client can choose
     * @return the possible actions
     */
    public ArrayList<ClientAction> getActions(){
        return new ArrayList<>(gameObserverProducer.getActions());
    }

    /**
     * Performs the action that the client has chosen
     */
    public void performAnAction() {
        ArrayList<ClientAction> clientActions = new ArrayList<>(gameObserverProducer.getActions());
        Optional<ClientAction> actionPicked = view.pickAnAction(clientActions);
        if (actionPicked.isEmpty())
            return;
        ClientAction action = actionPicked.get();
        if (!action.isDoable()) {
            view.showMessage("You cannot do this action");
            return;
        }
        action.doAction();
        gameObserverProducer.consumeAction(action);
    }
}

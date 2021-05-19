package it.polimi.ingsw.client;

import com.sun.jdi.event.ThreadStartEvent;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.LeaderActionMessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlotDTO;
import it.polimi.ingsw.message.action_message.market_message.*;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseAnyInputOutputDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseTradingRulesDTO;
import it.polimi.ingsw.message.action_message.production_message.InputFromWhereDTO;
import it.polimi.ingsw.message.update.TurnMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_action.BuyDevelopmentCard;
import it.polimi.ingsw.model.turn_action.TakeFromMarket;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientMain {

    // TODO HOW TO USE THIS IN GUI?
    // TODO properly handle this:
    //   the possible actions could be something like
    //      see player x
    //      activate leader cards
    //      discard leader card
    //      see available cards
    //      buy a card
    //      ...
    //   The actions are less while it is not my turn, eg I cannot activate a leader card etc...
    //   This structure is update from another thread who reads UpdateMessageDTOs,
    //     1) main thread reads,
    //     2) secodary thread updates this
    //   TODO/UPDATE: receive messages must be done in another thread, another structure "Queue" is needed to store readed message,
    //       pub/sub, second thread publish receved messages, this (main) thread consumes it in various forms

    // TODO there is always at least 1 action: "see valid actions"
    // TODO if there are no other actions, the game is ended
    public static ConcurrentLinkedDeque<ClientAction> actions; // TODO Updated by some publisher, with semantic, Object is too generic
     // TODO Updated by some publisher, without semantic, list of received, "unprocessed" messages, this means that updated message are not present here cause they can be processed by the publisher
    public static ClientGame observer = null;


    public static void main(String[] args) throws IOException {
        View view = setupClient();
        view.start();
        SocketConnector clientConnector = new SocketConnector(new Socket(view.askIP(), GameServer.PORT));
        String username = login(clientConnector, view); // TODO this must initialize models properly, somewhere
        observer = new ClientGame(clientConnector,username);
        new Thread(observer).start();
        do {
            performAnAction(clientConnector, view);
        } while (actions.size() > 0);
    //TODO I should find WHO WON, STATS, etc on message queeu
    }

    private static View setupClient(){
        Scanner in = new Scanner(System.in);
        String response = null;
        System.out.print("Choose 'CLI' or 'GUI': ");
        response = in.nextLine();
        System.out.println(response);
        while (!response.equalsIgnoreCase("CLI") &&
                !response.equalsIgnoreCase("GUI")){
            System.out.println("Error! Choose 'CLI' or 'GUI': ");
            response = in.nextLine();
        }
        if (response.equalsIgnoreCase("CLI"))
            return new CLI();
        //TODO else
        return null;
    }

    private static String login(SocketConnector clientConnector, View view) throws IOException {
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

        // TODO receive all the update messages, setup "someone" who waits for them (another thread), and then updates models

        return username;
    }

    public static void performAnAction(SocketConnector clientConnector, View view) {
        ClientAction action = view.pickAnAction(actions);
        action.doAction(clientConnector,view,observer);
        //Action
        //TODO thread or thread_server
        MessageDTO message = clientConnector.receiveMessage(message.getClass()).get();
        while (!message.toString().equalsIgnoreCase("Play") && !message.toString().equalsIgnoreCase("End of game")) {
            view.waitingPlayers();
            message = clientConnector.receiveMessage(message.getClass()).get();
        }
    }

    public static ConcurrentLinkedDeque getActions() {
        return actions;
    }
}

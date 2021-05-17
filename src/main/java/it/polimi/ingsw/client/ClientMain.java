package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.LeaderActionMessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCard;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCard;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlot;
import it.polimi.ingsw.message.action_message.market_message.ChooseLine;
import it.polimi.ingsw.message.action_message.market_message.ChooseResourceAny;
import it.polimi.ingsw.message.action_message.market_message.ResourceToDepot;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarket;
import it.polimi.ingsw.message.action_message.production_message.ActivateProduction;
import it.polimi.ingsw.message.action_message.production_message.ChooseAnyInputOutput;
import it.polimi.ingsw.message.action_message.production_message.ChooseTradingRules;
import it.polimi.ingsw.message.action_message.production_message.InputFromWhere;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.GameServer;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        View view = new CLI();
        //Choose CLI or GUI
        Scanner in = new Scanner(System.in);
        String response = null;
        System.out.println("Choose 'CLI' or 'GUI': ");
        response = in.toString();
        while (!response.equalsIgnoreCase("CLI") ||
                !response.equalsIgnoreCase("GUI")){
            System.out.println("Error! Choose 'CLI' or 'GUI': ");
            response = in.toString();
        }
        if (response.equalsIgnoreCase("CLI"))
            view = new CLI();
        /*else
            view = new GUI();*/
        view.start();
        SocketConnector clientConnector = new SocketConnector(new Socket(view.askIP(), GameServer.PORT));
        // MESSAGE 1 (sent)
        LoginMessageDTO loginMessageDTO = new LoginMessageDTO(view.askUsername(), view.askGameID());
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

        //TODO custom settings

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

        //Action

        MessageDTO chooseAction = view.chooseLeaderOrNormalAction();
        //TODO server
        if (chooseAction.getClass().getName().equals("LeaderActionMessageDTO") ) {
            LeaderActionMessageDTO leaderActionMessageDTO = (LeaderActionMessageDTO) chooseAction;
            LeaderActionMessageDTO leaderAction = (LeaderActionMessageDTO) clientConnector.receiveMessage(leaderActionMessageDTO.getClass()).get();
            leaderAction.setLeaderCardsActivated(view.chooseLeaderAction(pickedCards));
            clientConnector.sendMessage(leaderActionMessageDTO);
        }
        else{
            TurnActionMessageDTO turnActionMessageDTO = view.chooseTurnAction();
            clientConnector.sendMessage(turnActionMessageDTO);
            switch(turnActionMessageDTO.getClass().getName()){
                //TODO error message
                case "ActivateProduction":
                    ActivateProduction activateProduction = (ActivateProduction) chooseAction;
                    ChooseTradingRules tradingRulesMessage = (ChooseTradingRules) clientConnector.receiveMessage(ChooseTradingRules.class).get();
                    tradingRulesMessage.setChosenTradingRules(view.chooseTradingRulesToActivate(activateProduction.getTradingRulesToChoose()));
                    clientConnector.sendMessage(tradingRulesMessage);
                    ChooseAnyInputOutput chooseAnyInputOutput = (ChooseAnyInputOutput) clientConnector.receiveMessage(ChooseAnyInputOutput.class).get();
                    chooseAnyInputOutput.setChosenInputAny(view.chooseAnyInput(activateProduction.getInputAnyToChoose()));
                    chooseAnyInputOutput.setChosenOutputAny(view.chooseAnyOutput(activateProduction.getOutputAnyToChoose()));
                    clientConnector.sendMessage(chooseAnyInputOutput);
                    InputFromWhere inputFromWhere = (InputFromWhere) clientConnector.receiveMessage(InputFromWhere.class).get();
                    inputFromWhere.setInputFromStrongbox(view.inputFromStrongbox(activateProduction.getInputFromStrongBoxToChoose()));
                    inputFromWhere.setInputFromWarehouse(view.inputFromWarehouse(activateProduction.getInputFromWarehouseToChoose()));
                    clientConnector.sendMessage(inputFromWhere);

                case "BuyDevelopmentCards":
                    BuyDevelopmentCard buyDevelopmentCard = (BuyDevelopmentCard) chooseAction;
                    ChooseDevelopmentCard developmentCard = (ChooseDevelopmentCard) clientConnector.receiveMessage(ChooseDevelopmentCard.class).get();
                    developmentCard.setCard(view.buyDevelopmentCards(buyDevelopmentCard.getDevelopmentCardsDeck()));
                    clientConnector.sendMessage(developmentCard);
                    ChooseSlot chooseSlot = (ChooseSlot)  clientConnector.receiveMessage(ChooseSlot.class).get();
                    chooseSlot.setSlotID(view.chooseSlot());
                    clientConnector.sendMessage(chooseSlot);

                case "TakeFromMarket" :
                    TakeFromMarket takeFromMarket = (TakeFromMarket) chooseAction;
                    ChooseLine chooseLine = (ChooseLine) clientConnector.receiveMessage(ChooseLine.class).get();
                    Map<String,Integer> line = view.chooseLine();
                    chooseLine.setRc(line.keySet().toString());
                    chooseLine.setNum(line.get(chooseLine.getRc()));
                    clientConnector.sendMessage(chooseLine);
                    ChooseResourceAny chooseResourceAny = (ChooseResourceAny) clientConnector.receiveMessage(ChooseResourceAny.class).get();
                    chooseResourceAny.setChosenResourceAny(view.chooseResourceAny(takeFromMarket.getResourceAnyToChoose(),takeFromMarket.getActiveWhiteMarbleConversion()));
                    clientConnector.sendMessage(chooseResourceAny);
                    ResourceToDepot resourceToDepot = (ResourceToDepot) clientConnector.receiveMessage(ResourceToDepot.class).get();
                    resourceToDepot.setResourceToDepot(view.resourceToDepot(takeFromMarket.getResourcesTaken()));
                    clientConnector.sendMessage(resourceToDepot);
            }
        }


    }
}

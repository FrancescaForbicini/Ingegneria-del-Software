package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.Credentials;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.application.Application.launch;


public class GUI implements View {

    @Override
    public String askIP() {
        String IP = GUIController.getInstance().getIp();
        System.out.println(IP);
        return IP;
    }

    @Override
    public Credentials askCredentials() {
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "Login.fxml");
        Credentials credentials = GUIController.getInstance().getCredentials();
        System.out.println(credentials);
        return credentials;
    }

    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        GUIController.getInstance().setPossibleActions(actions);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "PickAnAction.fxml");
        ClientAction clientAction = GUIController.getInstance().getPickedAction();
        return Optional.ofNullable(clientAction);
    }

    @Override
    public ResourceType pickStartingResources() {
        return chooseResource();
    }

    @Override
    public int chooseAdditionalOrDevelopmentProduction(ArrayList<Eligible> availableProductions, boolean oneUsed) {
        return 0;
    }


    @Override
    public boolean userWantToDoIt() {
        return false;
    }


    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    @Override
    public boolean showMessage(String message) {
        GUIController.getInstance().setMessageToShow(message);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowMessage.fxml");
        return GUIController.getInstance().getAckMessage();
    }

    @Override
    public void showMarket(Market market) {
        GUIController.getInstance().setMarket(market);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowMarket.fxml");
    }

    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        GUIController.getInstance().setDevelopmentCards(developmentCards);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowDevelopmentCards.fxml");

    }

    @Override
    public void showPlayer(ClientPlayer player) {
        GUIController.getInstance().setPickedPlayerToShow(player);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowPlayer.fxml");
    }



    @Override
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards){
        return pickLeaderCard(proposedCards);
    }

    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        GUIController.getInstance().setProposedLeaderCards(new ArrayList<>(proposedCards));
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "PickLeaderCard.fxml");
        return GUIController.getInstance().getPickedLeaderCardIndex();
    }


    @Override
    public ArrayList<DevelopmentCard> chooseDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        /*GUIController.getInstance().setActiveTradingRules(developmentCardsAvailable);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ChooseTradingRules.fxml");
        ArrayList<TradingRule> chosenTradingRules = GUIController.getInstance().getChosenTradingRules();
        return chosenTradingRules;*/
        return null;
    }


    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards) {
        return 0;
    }

    @Override
    public int chooseSlot(ArrayList<Integer> slotsAvailable) {
        return 0;
    }


    @Override
    public ChosenLine chooseLine(Market market) {
        return null;
    }

    @Override
    public int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions) {
        return 0;
    }

    @Override
    public boolean notifyNewActions() {
        return showMessage("Loading new actions");
    }

    @Override
    public int choose(List<?> elemsToChoose) {
        return 0;
    }

    @Override
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose) {
        return 0;
    }

    @Override
    public int choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose) {
        GUIController.getInstance().setPlayersToShow(clientPlayersToChoose);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "ChoosePlayer.fxml");
        return GUIController.getInstance().getPickedPlayerIndex();
    }

    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        return 0;
    }

    @Override
    public ResourceType chooseResource() {
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "PickResource.fxml");
        return GUIController.getInstance().getPickedResource();
    }


    @Override
    public void showWinner(String winnerUsername) {
        GUIController.getInstance().setWinner(winnerUsername);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowWinner.fxml");
    }

    @Override
    public int chooseQuantity(int maxQuantity) {
        return 0;
    }

}
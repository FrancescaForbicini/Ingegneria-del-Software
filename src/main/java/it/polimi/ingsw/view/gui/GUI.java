package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ResourcesChosen;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.Credentials;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.application.Application.launch;


public class GUI implements View {
    private boolean sceneAlreadySeen = false;

    @Override
    public int chooseAdditionalOrDevelopmentProduction(ArrayList<DevelopmentCard> developmentCardsAvailable, ArrayList<AdditionalTradingRule> additionalTradingRulesAvailable) {
        return 0;
    }

    @Override
    public int chooseAdditionalTradingRule(ArrayList<AdditionalTradingRule> additionalTradingRulesAvailable, boolean oneUsed) {
        return 0;
    }

    @Override
    public int chooseDevelopmentCardProduction(ArrayList<DevelopmentCard> developmentCardsAvailable, boolean oneUsed) {
        return 0;
    }

    @Override
    public boolean askToChoose() {
        return false;
    }

    @Override
    public ResourcesChosen inputFrom(ResourceType resourceType, int quantityStrongbox, int quantityWarehouse) {
        return null;
    }

    @Override
    public void setSceneAlreadySeen(boolean sceneAlreadySeen) {
        this.sceneAlreadySeen = sceneAlreadySeen;
    }

    @Override
    public boolean isSceneAlreadySeen() {
        return sceneAlreadySeen;
    }

    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    @Override
    public void showStart(){
    }

    @Override
    public void showMessage(String message) {
        GUIController.getInstance().setMessageToShow(message);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowMessage.fxml");
        boolean ack;
        do {
            ack = GUIController.getInstance().getAckMessage();
        }while(!ack);
    }

    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        GUIController.getInstance().setPossibleActions(actions);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "PickAnAction.fxml");
        ClientAction clientAction = GUIController.getInstance().getPickedAction();
        return Optional.ofNullable(clientAction);
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

    }

    public void showPlayer(Player currentPlayer,ArrayList<ClientPlayer> players, FaithTrack faithTrack) {
        GUIController.getInstance().setPlayersToShow(players);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"PickPlayerToShow.fxml");
    }

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
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards){
        GUIController.getInstance().setLeaderCards(new ArrayList<>(proposedCards));
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "PickLeaderCard.fxml");
        ArrayList<LeaderCard> pickedLeaderCards = GUIController.getInstance().getPickedLeaderCards();
        return 0;
    }

    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        return 0;
    }

    @Override
    public ResourceType pickStartingResources() {
        return null;
    }


    public int pickStartingResources(int numberOfResources) {
        GUIController.getInstance().setNumberOfResources(numberOfResources);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"PickStartingResource.fxml");
        ArrayList<ResourceType> pickedResource = GUIController.getInstance().getPickedResources();
        return 0;
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
    public void notifyNewActions() {
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
        return 0;
    }

    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        return 0;
    }


    public int choose(ArrayList<?> elemsToChoose) {
        return 0;
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
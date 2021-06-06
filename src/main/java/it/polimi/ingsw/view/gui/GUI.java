package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.view.LeaderCardChoice;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javafx.application.Application.launch;


public class GUI implements View{
    private boolean sceneAlreadySeen = false;

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
    public void showAvailableActions(ArrayList<ClientAction> actions) {

    }

    @Override
    public void showStart(){
    }

    @Override
    public void showMessage(String message) {
        GUIController.getInstance().setMessageToShow(message);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"ShowMessage.fxml");
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
    public void showPlayer(ArrayList<ClientPlayer> players, FaithTrack faithTrack) {
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
    public ClientPlayer askCredentials() {
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "Login.fxml");
        ClientPlayer clientPlayer = GUIController.getInstance().getCredentials();
        System.out.println(clientPlayer);
        return clientPlayer;
    }

    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException {
        return null;
    }

    @Override
    public ArrayList<ResourceType> pickStartingResources(int numberOfResources) {
        GUIController.getInstance().setNumberOfResources(numberOfResources);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(),"PickStartingResource.fxml");
        ArrayList<ResourceType> pickedResource = GUIController.getInstance().getPickedResources();
        return pickedResource;
    }

    @Override
    public LeaderCardChoice chooseLeaderCardAction() {
        return null;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToActivate(List<LeaderCard> leaderCards) {
        return null;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToDiscard(List<LeaderCard> leaderCards) {
        return null;
    }

    @Override
    public ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules) {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseAnyInput(ArrayList<ResourceType> chosenInputAny) {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseAnyOutput(ArrayList<ResourceType> chosenOutputAny) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> inputFromStrongbox(Map<ResourceType, Integer> resources) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> inputFromWarehouse(Map<ResourceType, Integer> resources) {
        return null;
    }

    @Override
    public DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards) {
        return null;
    }

    @Override
    public int chooseSlot() {
        return 0;
    }

    @Override
    public boolean askSortWarehouse() {
        return false;
    }

    @Override
    public Warehouse sortWarehouse(Warehouse warehouse) {
        return null;
    }

    @Override
    public Map<String, Integer> chooseLine() {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseResourceAny(ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> resourceToDepot(ArrayList<ResourceType> resources, ClientPlayer player) {
        return null;
    }

    @Override
    public void notifyNewActions() {

    }

    @Override
    public void showWinner(String winnerUsername) {

    }

}
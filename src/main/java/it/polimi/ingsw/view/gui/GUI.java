package it.polimi.ingsw.view.gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.solo_game_action.SoloGameAction;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.view.LeaderCardChoice;
import it.polimi.ingsw.view.SoloTokenChoice;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

import static javafx.application.Application.launch;


public class GUI implements View{
    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    @Override
    public ClientAction pickAnAction(ConcurrentLinkedDeque<ClientAction> actions) {
        return null;
    }

    @Override
    public void showMarket(Market market) {

    }

    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {

    }

    @Override
    public void showFaithTrack(FaithTrack faithTrack) {

    }

    @Override
    public void showPlayer(ArrayList<ClientPlayer> players) {

    }

    @Override
    public String askIP() {
        String IP = GUIController.getInstance().getIp();
        System.out.println(IP);
        return IP;
    }

    @Override
    public ClientPlayer askCredentials() {
        return null;
    }

    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException {
        return null;
    }

    @Override
    public ArrayList<ResourceType> pickStartingResources(int numberOfResources) {
        return null;
    }

    @Override
    public void showStart() throws IOException {

    }

    @Override
    public void errorStartGame() throws IOException {

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
    public SoloTokenChoice pickSoloToken(ConcurrentLinkedDeque<SoloGameAction> soloTokens) {
        return null;
    }

    @Override
    public ArrayList<DevelopmentCard> DevelopmentCardsToDiscard(ArrayList<DevelopmentCard> developmentCardsAvailable, ArrayList<DevelopmentCard> developmentCardsToDiscard) {
        return null;
    }

    @Override
    public void showMoveBlackShuffle(Deck<SoloToken> soloTokenDecks, FaithTrack faithTrack) {

    }

    @Override
    public void showMoveBlackCross(FaithTrack faithTrack) {

    }

}
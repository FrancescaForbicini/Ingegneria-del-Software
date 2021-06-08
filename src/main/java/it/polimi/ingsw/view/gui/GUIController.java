package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.view.gui.scene_controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

public class GUIController {
    private static GUIController instance;
    private Stage stage;
    private Parent root;
    private LoginController loginController;
    private FXMLLoader loader;
    private ArrayBlockingQueue<String> messageToShowQueue;
    private ArrayBlockingQueue<Boolean> ackMessageQueue;
    private ArrayBlockingQueue<String> ipQueue;
    private ArrayBlockingQueue<String> usernameQueue;
    private ArrayBlockingQueue<String> gameIDQueue;
    private ArrayBlockingQueue<ArrayList<ResourceType>> pickedResourcesQueue;
    private ArrayBlockingQueue<ArrayList<LeaderCard>> leaderCardsQueue;
    private ArrayBlockingQueue<ArrayList<LeaderCard>> pickedLeaderCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientAction>> possibleActionsQueue;
    private ArrayBlockingQueue<ClientAction> pickedActionQueue;
    private ArrayBlockingQueue<Market> marketQueue;
    private ArrayBlockingQueue<ArrayList<DevelopmentCard>> developmentCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientPlayer>> playersToShowQueue;
    private ArrayBlockingQueue<ClientPlayer> pickedPlayerQueue;
    private ArrayBlockingQueue<ArrayList<TradingRule>> activeTradingRulesQueue;
    private ArrayBlockingQueue<ArrayList<TradingRule>> chosenTradingRulesQueue;
    private int numberOfResources;
    private ArrayBlockingQueue<String> winnerQueue;



    public static GUIController getInstance(){
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }

    private GUIController(){
        messageToShowQueue = new ArrayBlockingQueue<>(1);
        ackMessageQueue = new ArrayBlockingQueue<>(1);
        ipQueue = new ArrayBlockingQueue<>(1);
        usernameQueue = new ArrayBlockingQueue<>(1);
        gameIDQueue = new ArrayBlockingQueue<>(1);
        pickedResourcesQueue = new ArrayBlockingQueue<>(1);
        leaderCardsQueue = new ArrayBlockingQueue<>(1);
        pickedLeaderCardsQueue = new ArrayBlockingQueue<>(1);
        possibleActionsQueue = new ArrayBlockingQueue<>(1);
        pickedActionQueue = new ArrayBlockingQueue<>(1);
        marketQueue = new ArrayBlockingQueue<>(1);
        developmentCardsQueue = new ArrayBlockingQueue<>(1);
        playersToShowQueue = new ArrayBlockingQueue<>(1);
        pickedPlayerQueue = new ArrayBlockingQueue<>(1);
        activeTradingRulesQueue = new ArrayBlockingQueue<>(1);
        chosenTradingRulesQueue = new ArrayBlockingQueue<>(1);
        winnerQueue = new ArrayBlockingQueue<>(1);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public Parent getRoot() {
        return root;
    }

    public void setMessageToShow(String messageToShow) {
        try {
            messageToShowQueue.put(messageToShow);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getMessageToShow() {
        String message = null;
        try {
            message = messageToShowQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;    }

    public void setAckMessage(Boolean ackMessage) {
        try {
            ackMessageQueue.put(ackMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean getAckMessage(){
        boolean ack = false;
        try {
            ack = ackMessageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ack;
    }

    public void setIp(String ip){
        try {
            ipQueue.put(ip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getIp() {
        String ip = null;
        try {
            ip = ipQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ip;
    }
    public ClientPlayer getCredentials(){
        String username = null;
        String gameID = null;
        try {
            username = usernameQueue.take();
            gameID = gameIDQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ClientPlayer(username,gameID);
    }
    public void setUsername(String username){
        try {
            usernameQueue.put(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setGameID(String gameID){
        try {
            gameIDQueue.put(gameID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setNumberOfResources(int numberOfResources){
        this.numberOfResources = numberOfResources;
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }

    public void setPickedResources(ArrayList<ResourceType> pickedResources) {
        try {
            pickedResourcesQueue.put(pickedResources);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ResourceType> getPickedResources(){
        ArrayList<ResourceType> pickedResources = null;
        try {
            pickedResources = pickedResourcesQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedResources;
    }

    public void setLeaderCards(ArrayList<LeaderCard> proposedLeaderCards){
        try {
            leaderCardsQueue.put(proposedLeaderCards);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaderCard> getLeaderCards(){
        ArrayList<LeaderCard> proposedLeaderCards = null;
        try {
            proposedLeaderCards = pickedLeaderCardsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return proposedLeaderCards;
    }

    public void setPickedLeaderCards(ArrayList<LeaderCard> pickedLeaderCards){
        try {
            leaderCardsQueue.put(pickedLeaderCards);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaderCard> getPickedLeaderCards(){
        ArrayList<LeaderCard> pickedLeaderCards = null;
        try {
            pickedLeaderCards = pickedLeaderCardsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedLeaderCards;
    }

    public void setActiveTradingRules(ArrayList<TradingRule> activeTradingRules){
        try {
            activeTradingRulesQueue.put(activeTradingRules);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TradingRule> getActiveTradingRules() {
        ArrayList<TradingRule> activeTradingRules = new ArrayList<>();
        try {
            activeTradingRules = activeTradingRulesQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return activeTradingRules;
    }

    public void setChosenTradingRules(ArrayList<TradingRule> chosenTradingRules){
        try {
            chosenTradingRulesQueue.put(chosenTradingRules);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TradingRule> getChosenTradingRules() {
        ArrayList<TradingRule> chosenTradingRules = new ArrayList<>();
        try {
            chosenTradingRules = chosenTradingRulesQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chosenTradingRules;
    }


    public void setPossibleActions(ArrayList<ClientAction> possibleActions) {
        try {
            possibleActionsQueue.put(possibleActions);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClientAction> getPossibleActions() {
        ArrayList<ClientAction> possibleActions = new ArrayList<>();
        try {
            possibleActions = possibleActionsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return possibleActions;
    }
    //TODO check with Optional
    public void setPickedAction(ClientAction pickedAction){
        try {
            pickedActionQueue.put(pickedAction);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public ClientAction getPickedAction(){
        ClientAction pickedAction = null;
        try {
            pickedActionQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedAction;
    }

    public void setMarket(Market market){
        try {
            marketQueue.put(market);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Market getMarket(){
        Market market = null;
        try {
            market = marketQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return market;
    }

    public void setDevelopmentCards(ArrayList<DevelopmentCard> developmentCards){
        try {
            developmentCardsQueue.put(developmentCards);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        ArrayList<DevelopmentCard> developmentCards = null;
        try {
            developmentCards = developmentCardsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return developmentCards;
    }

    public void setPlayersToShow(ArrayList<ClientPlayer> playersToShow){
        try {
            playersToShowQueue.put(playersToShow);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClientPlayer> getPlayersToShow(){
        ArrayList<ClientPlayer> playerToShow = null;
        try {
            playerToShow = playersToShowQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return playerToShow;
    }
    public void setPickedPlayer(ClientPlayer pickedPlayer){
        try {
            pickedPlayerQueue.put(pickedPlayer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ClientPlayer getPickedPlayer(){
        ClientPlayer pickedPlayer = null;
        try {
            pickedPlayer = pickedPlayerQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedPlayer;
    }

    public void setWinner(String winnerUsername){
        try {
            winnerQueue.put(winnerUsername);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getWinner(){
        String winnerUsername = null;
        try {
            winnerUsername = winnerQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return winnerUsername;
    }

    public void setupScene(Scene scene , String file){
        System.out.println(file);
        loader = new FXMLLoader(GUIController.class.getClassLoader().getResource(file));
        try{
            root = loader.load();
            scene.setRoot(root);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

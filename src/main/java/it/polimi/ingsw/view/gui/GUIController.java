package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.view.Credentials;
import it.polimi.ingsw.view.gui.scene_controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
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
    private ArrayBlockingQueue<Credentials> credentialsQueue;
    private ArrayBlockingQueue<ResourceType> pickedResourceQueue;
    private ArrayBlockingQueue<Integer> pickedLeaderCardIndexQueue;
    private ArrayBlockingQueue<ArrayList<LeaderCard>> proposedLeaderCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientAction>> possibleActionsQueue;
    private ArrayBlockingQueue<ClientAction> pickedActionQueue;
    private ArrayBlockingQueue<Market> marketQueue;
    private ArrayBlockingQueue<ArrayList<DevelopmentCard>> developmentCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientPlayer>> playersToShowQueue;
    private ArrayBlockingQueue<ClientPlayer> pickedPlayerToShowQueue;
    private ArrayBlockingQueue<Integer> pickedPlayerIndexQueue;
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
        credentialsQueue = new ArrayBlockingQueue<>(1);
        pickedResourceQueue = new ArrayBlockingQueue<>(1);
        pickedLeaderCardIndexQueue = new ArrayBlockingQueue<>(1);
        proposedLeaderCardsQueue = new ArrayBlockingQueue<>(1);
        possibleActionsQueue = new ArrayBlockingQueue<>(1);
        pickedActionQueue = new ArrayBlockingQueue<>(1);
        marketQueue = new ArrayBlockingQueue<>(1);
        developmentCardsQueue = new ArrayBlockingQueue<>(1);
        playersToShowQueue = new ArrayBlockingQueue<>(1);
        pickedPlayerToShowQueue = new ArrayBlockingQueue<>(1);
        pickedPlayerIndexQueue = new ArrayBlockingQueue<>(1);
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
    public Credentials getCredentials(){
        Credentials credentials = null;
        try {
            credentials = credentialsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return credentials;
    }
    public void setCredentials(Credentials credentials){
        try {
            credentialsQueue.put(credentials);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setPickedResource(ResourceType pickedResource) {
        try {
            pickedResourceQueue.put(pickedResource);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ResourceType getPickedResource(){
        ResourceType pickedResource = null;
        try {
            pickedResource = pickedResourceQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedResource;
    }

    public void setProposedLeaderCards(ArrayList<LeaderCard> proposedLeaderCards){
        try {
            proposedLeaderCardsQueue.put(proposedLeaderCards);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaderCard> getProposedLeaderCards(){
        ArrayList<LeaderCard> proposedLeaderCards = null;
        try {
            proposedLeaderCards = proposedLeaderCardsQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return proposedLeaderCards;
    }

    public void setPickedLeaderCardIndex(int pickedLeaderCardIndex){
        try {
            pickedLeaderCardIndexQueue.put(pickedLeaderCardIndex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getPickedLeaderCardIndex(){
        int pickedLeaderCardIndex = 0;
        try {
            pickedLeaderCardIndex = pickedLeaderCardIndexQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedLeaderCardIndex;
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
    public void setPickedPlayerIndex(int pickedPlayerIndex){
        try {
            pickedPlayerIndexQueue.put(pickedPlayerIndex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getPickedPlayerIndex(){
        int pickedPlayer = 0;
        try {
            pickedPlayer = pickedPlayerIndexQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedPlayer;
    }

    public void setPickedPlayerToShow(ClientPlayer pickedPlayer){
        try {
            pickedPlayerToShowQueue.put(pickedPlayer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ClientPlayer getPickedPlayerToShow(){
        ClientPlayer pickedPlayerToShow = null;
        try {
            pickedPlayerToShow = pickedPlayerToShowQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedPlayerToShow;
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


    public void setupScene(Scene scene , String file){//TODO maybe another method to show messages (with another loader to not overwrite)
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

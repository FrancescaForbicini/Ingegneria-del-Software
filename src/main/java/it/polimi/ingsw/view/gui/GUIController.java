package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.Credentials;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Controller of the GUI application
 */
public class GUIController {

    private static GUIController instance;
    private Stage stage;
    private ArrayBlockingQueue<String> messageToShowQueue;
    private ArrayBlockingQueue<Boolean> ackMessageQueue;
    private ArrayBlockingQueue<String> ipQueue;
    private ArrayBlockingQueue<Credentials> credentialsQueue;
    private ArrayBlockingQueue<ResourceType> pickedResourceQueue;
    private ArrayBlockingQueue<Integer> pickedIndexQueue;
    private ArrayBlockingQueue<ArrayList<LeaderCard>> proposedLeaderCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientAction>> possibleActionsQueue;
    private ArrayBlockingQueue<ClientAction> pickedActionQueue;
    private ArrayBlockingQueue<Market> marketQueue;
    private ArrayBlockingQueue<ArrayList<DevelopmentCard>> developmentCardsQueue;
    private ArrayBlockingQueue<ArrayList<ClientPlayer>> playersToShowQueue;
    private ArrayBlockingQueue<ClientPlayer> pickedPlayerToShowQueue;
    private ArrayBlockingQueue<ArrayList<TradingRule>> activeTradingRulesQueue;
    private ArrayBlockingQueue<ArrayList<TradingRule>> chosenTradingRulesQueue;
    private ArrayBlockingQueue<ArrayList<WarehouseDepot>> possibleDepotsQueue;
    private ArrayBlockingQueue<ChosenLine> chosenLineQueue;
    private ArrayBlockingQueue <DevelopmentCard> developmentCardQueue;
    private ArrayBlockingQueue<Eligible> productionToActivate;
    private ArrayBlockingQueue<Integer> chosenQuantityQueue;

    /**
     * Initializes the controller of the gui
     * @return instance of the controller
     */
    public static GUIController getInstance(){
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }

    /**
     * Initializes the queues
     *
     */
    private GUIController(){
        messageToShowQueue = new ArrayBlockingQueue<>(1);
        ackMessageQueue = new ArrayBlockingQueue<>(1);
        ipQueue = new ArrayBlockingQueue<>(1);
        credentialsQueue = new ArrayBlockingQueue<>(1);
        pickedResourceQueue = new ArrayBlockingQueue<>(1);
        pickedIndexQueue = new ArrayBlockingQueue<>(1);
        proposedLeaderCardsQueue = new ArrayBlockingQueue<>(1);
        possibleActionsQueue = new ArrayBlockingQueue<>(1);
        pickedActionQueue = new ArrayBlockingQueue<>(1);
        marketQueue = new ArrayBlockingQueue<>(1);
        developmentCardsQueue = new ArrayBlockingQueue<>(1);
        playersToShowQueue = new ArrayBlockingQueue<>(1);
        pickedPlayerToShowQueue = new ArrayBlockingQueue<>(1);
        activeTradingRulesQueue = new ArrayBlockingQueue<>(1);
        chosenTradingRulesQueue = new ArrayBlockingQueue<>(1);
        possibleDepotsQueue = new ArrayBlockingQueue<>(1);
        chosenLineQueue = new ArrayBlockingQueue<>(1);
        developmentCardQueue = new ArrayBlockingQueue<>(1);
        productionToActivate = new ArrayBlockingQueue<>(1);
        chosenQuantityQueue = new ArrayBlockingQueue<>(1);
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setChosenLineQueue(ChosenLine chosenLineQueue) {
        try {
            this.chosenLineQueue.put(chosenLineQueue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public ChosenLine getChosenLineQueue() {
        try {
            return chosenLineQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


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

    public void setPickedDevelopmentCard(DevelopmentCard developmentCard) {
        try {
            this.developmentCardQueue.put(developmentCard);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DevelopmentCard getPickedDevelopmentCard (){
        DevelopmentCard developmentCard = null;
        try {
            developmentCard = this.developmentCardQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return developmentCard;
    }


    public void setPickedIndex(int pickedIndex){
        try {
            pickedIndexQueue.put(pickedIndex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getPickedIndex(){
        int pickedIndex = 0;
        try {
            pickedIndex = pickedIndexQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedIndex;
    }

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
            pickedAction = pickedActionQueue.take();
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

    public void setChosenQuantity(int chosenQuantity){
        try {
            chosenQuantityQueue.put(chosenQuantity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getChosenQuantity(){
        int chosenQuantity = 0;
        try {
            chosenQuantity = chosenQuantityQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chosenQuantity;
    }
}

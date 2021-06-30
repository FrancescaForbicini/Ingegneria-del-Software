package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ReactiveObserver;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.leader.ActivateLeaderCard;
import it.polimi.ingsw.client.action.leader.DiscardLeaderCard;
import it.polimi.ingsw.client.action.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.action.show.ShowMarket;
import it.polimi.ingsw.client.action.show.ShowOpponentLastAction;
import it.polimi.ingsw.client.action.show.ShowPlayer;
import it.polimi.ingsw.client.action.starting.PickStartingLeaderCards;
import it.polimi.ingsw.client.action.starting.PickStartingResources;
import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.client.action.turn.SortWarehouse;
import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class PickAnActionController extends ReactiveObserver {
    @FXML
    private FlowPane startingPane;
    @FXML
    private Button showMarket;
    @FXML
    private Button showPlayers;
    @FXML
    private Button showDevelopmentCard;
    @FXML
    private Button discardLeaderCard;
    @FXML
    private Button activateLeaderCard;
    @FXML
    private Button sortWarehouse;
    @FXML
    private Button activateProduction;
    @FXML
    private Button buyDevelopmentCard;
    @FXML
    private Button takeFromMarket;
    @FXML
    private Button finishTurn;

    @FXML
    private Button pickStartingLeaderCards;

    @FXML
    private Button pickStartingResources;

    @FXML
    private Button showOpponentLastAction;

    @FXML
    private ScrollBar logger;
    @FXML
    private VBox leaderCards;

    private ArrayList<ButtonBase> showButtons;
    private ArrayList<ButtonBase> turnButtons;
    private ConcurrentLinkedDeque<ClientAction> possibleActions;


    public PickAnActionController(ClientGameObserverProducer clientGameObserverProducer) {
        super(clientGameObserverProducer);
    }

        private void setupShowButtons() {
        showButtons = new ArrayList<>();
        showButtons.add(showDevelopmentCard);
        buttonActions.put(ShowDevelopmentCards.class, showDevelopmentCard);
        showButtons.add(showMarket);
        buttonActions.put(ShowMarket.class, showMarket);
        showButtons.add(showPlayers);
        buttonActions.put(ShowPlayer.class, showPlayers);
        showButtons.add(showOpponentLastAction);
        buttonActions.put(ShowOpponentLastAction.class, showOpponentLastAction); // TODO make invisible??
    }
    private void setupTurnButtons() {
        turnButtons = new ArrayList<>();
        turnButtons.add(pickStartingLeaderCards);
        buttonActions.put(PickStartingLeaderCards.class, pickStartingLeaderCards);
        turnButtons.add(pickStartingResources);
        buttonActions.put(PickStartingResources.class, pickStartingResources);
        turnButtons.add(discardLeaderCard);
        buttonActions.put(DiscardLeaderCard.class, discardLeaderCard);
        turnButtons.add(sortWarehouse);
        buttonActions.put(SortWarehouse.class, sortWarehouse);
        turnButtons.add(activateLeaderCard);
        buttonActions.put(ActivateLeaderCard.class, activateLeaderCard);
        turnButtons.add(takeFromMarket);
        buttonActions.put(TakeFromMarket.class, takeFromMarket);
        turnButtons.add(activateProduction);
        buttonActions.put(ActivateProduction.class, activateProduction);
        turnButtons.add(buyDevelopmentCard);
        buttonActions.put(BuyDevelopmentCard.class, buyDevelopmentCard);
        turnButtons.add(finishTurn);
        buttonActions.put(FinishTurn.class, finishTurn);
    }
    private Map<Class<? extends ClientAction>, Button> buttonActions;

    public void initialize(){
        buttonActions = new HashMap<>();
        setupShowButtons();
        setupTurnButtons();
        buttonActions.forEach((klass, button) -> button.setOnAction(actionEvent -> setPickedAction(klass)));
     }

    /**
     * setup all buttons to make available only the ones which correspond to an available action ones
     * @param possibleActions
     */
    private void react(ConcurrentLinkedDeque<ClientAction> possibleActions, Player currentPlayer, FaithTrack faithTrack) {
        this.possibleActions = possibleActions;
        turnButtons.forEach(turnButton -> turnButton.setDisable(true));
        System.out.println(possibleActions.stream().map(ClientAction::toString).collect(Collectors.joining(" - "))); // TODO remove
        possibleActions.stream()
                .map(possibleAction -> buttonActions.get(possibleAction.getClass()))
                .forEach(button -> button.setDisable(false));
        if (currentPlayer != null)
            react(currentPlayer);
        if (faithTrack != null) {
            react(faithTrack);
        }
    }
    private void react(FaithTrack faithTrack){
        // TODO ...
    }
    private void react(Player player){
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()){
            //TODO put right development cards
        }
        int heightLeaderCard = 200;
        int widthLeaderCard = 150;
        leaderCards.getChildren().clear();
        for (LeaderCard leaderCard: player.getNonActiveLeaderCards()){
            Label label = new Label("Non Active");
            ImageView card = new ImageView(new Image(leaderCard.getPath()));
            card.setFitHeight(heightLeaderCard);
            card.setFitWidth(widthLeaderCard);
            leaderCards.getChildren().add(label);
            leaderCards.getChildren().add(card);
        }
        for (LeaderCard leaderCard: player.getActiveLeaderCards()){
            Label label = new Label("Active");
            ImageView card = new ImageView(new Image(leaderCard.getPath()));
            card.setFitHeight(heightLeaderCard);
            card.setFitWidth(widthLeaderCard);
            leaderCards.getChildren().add(label);
            leaderCards.getChildren().add(card);
        }
    }

    private void setPickedAction(Class<? extends ClientAction> pickedActionClass){
        Optional<ClientAction> oPickedAction = possibleActions.stream()
                .filter(clientAction -> clientAction.getClass().equals(pickedActionClass)).findFirst();
        oPickedAction.ifPresent(pickedAction -> GUIController.getInstance().setPickedAction(pickedAction));
    }

    @Override
    public void update() {
        Platform.runLater(() -> react(
                clientGameObserverProducer.getActions(),
                clientGameObserverProducer.getCurrentPlayer(),
                clientGameObserverProducer.getFaithTrack()));
    }
}

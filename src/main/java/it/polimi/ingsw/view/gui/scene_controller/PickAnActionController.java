package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.turn.SortWarehouse;
import it.polimi.ingsw.client.action.leader.ActivateLeaderCard;
import it.polimi.ingsw.client.action.leader.DiscardLeaderCard;
import it.polimi.ingsw.client.action.show.ShowAction;
import it.polimi.ingsw.client.action.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.action.show.ShowMarket;
import it.polimi.ingsw.client.action.show.ShowPlayer;
import it.polimi.ingsw.client.action.starting.PickStartingLeaderCards;
import it.polimi.ingsw.client.action.starting.PickStartingResources;
import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.client.action.turn.TurnAction;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PickAnActionController {
    @FXML
    private FlowPane startingPane;
    @FXML
    private Button developmentButton;
    @FXML
    private Button marketButton;
    @FXML
    private Button playersButton;
    @FXML
    private Button discardButton;
    @FXML
    private Button activateButton;
    @FXML
    private Button warehouseButton;
    @FXML
    private MenuButton actionMenuButton;
    @FXML
    private MenuItem productionItem;
    @FXML
    private MenuItem buyDevelopmentItem;
    @FXML
    private MenuItem takeFromMarketItem;
    @FXML
    private Button endButton;

    @FXML
    private ScrollBar logger;
    @FXML
    private VBox leaderCards;

    private ArrayList<ButtonBase> allButtons;
    private ArrayList<ButtonBase> showButtons;
    private ArrayList<ButtonBase> turnButtons;
    private ArrayList<ClientAction> possibleActions;

    public void initialize(){
        allButtons = new ArrayList<>();
        showButtons = new ArrayList<>();
        turnButtons = new ArrayList<>();
        developmentButton.setOnAction(actionEvent -> setPickedAction(ShowDevelopmentCards.class));
        showButtons.add(developmentButton);
        marketButton.setOnAction(actionEvent -> setPickedAction(ShowMarket.class));
        showButtons.add(marketButton);
        playersButton.setOnAction(actionEvent -> setPickedAction(ShowPlayer.class));
        showButtons.add(playersButton);
        //TODO add showOpponentLastAction if there is the opponent
        discardButton.setOnAction(actionEvent -> setPickedAction(DiscardLeaderCard.class));
        turnButtons.add(discardButton);
        activateButton.setOnAction(actionEvent -> setPickedAction(ActivateLeaderCard.class));
        turnButtons.add(activateButton);
        warehouseButton.setOnAction(actionEvent -> setPickedAction(SortWarehouse.class));
        turnButtons.add(warehouseButton);
        productionItem.setOnAction(actionEvent -> setPickedAction(ActivateProduction.class));
        buyDevelopmentItem.setOnAction(actionEvent -> setPickedAction(BuyDevelopmentCard.class));
        takeFromMarketItem.setOnAction(actionEvent -> setPickedAction(TakeFromMarket.class));
        turnButtons.add(actionMenuButton);
        endButton.setOnAction(actionEvent -> setPickedAction(FinishTurn.class));
        turnButtons.add(endButton);
        //TODO set show player itself
        //        //ClientPlayer self = GUIController.getInstance().getSelf();
        //        //setupShowSelf(); //TODO take the logic from ShowPlayerController
        possibleActions = GUIController.getInstance().getPossibleActions();
        Player player = GUIController.getInstance().getCurrentPlayer();
        update(player);
        setupPossibleButtons();
    }

    /**
     * setup all buttons to make available only the ones which correspond to an available action ones
     */
    private void setupPossibleButtons() {
        for(ButtonBase buttonBase : showButtons){
            //able all show buttons
            buttonBase.setDisable(false);
        }
        for(ButtonBase buttonBase : turnButtons){
            //able all turn buttons
            buttonBase.setDisable(false);
        }
        if(possibleActions.stream().anyMatch(action -> action instanceof PickStartingLeaderCards)){
            //user must pick starting leader cards
            Button startingLeaderCardsButton = new Button();
            startingLeaderCardsButton.setDisable(false);
            startingLeaderCardsButton.setText("Pick starting Leader Cards");
            startingLeaderCardsButton.setOnMousePressed(actionEvent -> setPickedAction(PickStartingLeaderCards.class));
            startingPane.getChildren().add(startingLeaderCardsButton);

        }
        if(possibleActions.stream().anyMatch(action -> action instanceof PickStartingResources)){
            //user must pick starting resources
            Button startingResourcesButton = new Button();
            startingResourcesButton.setDisable(false);
            startingResourcesButton.setText("Pick starting resources");
            startingResourcesButton.setOnMousePressed(actionEvent -> setPickedAction(PickStartingResources.class));
            startingPane.getChildren().add(startingResourcesButton);
        }
        if (possibleActions.stream().allMatch(action -> action instanceof ShowAction)) {
            //there's no turn action possible (user not in their turn)
            for(ButtonBase buttonBase : turnButtons){
                //disable turn buttons
                buttonBase.setDisable(true);
            }
        } else {
            //user in their turn
            if (possibleActions.stream().noneMatch(action -> action instanceof DiscardLeaderCard)) {
                //there's no discard action available
                turnButtons.get(turnButtons.indexOf(discardButton)).setDisable(true);
            }
            if (possibleActions.stream().noneMatch(action -> action instanceof ActivateLeaderCard)) {
                //there's no activate action available
                turnButtons.get(turnButtons.indexOf(activateButton)).setDisable(true);
            }
            if (possibleActions.stream().noneMatch(action -> action instanceof TurnAction)) {
                //user already did their turn action
                turnButtons.get(turnButtons.indexOf(actionMenuButton)).setDisable(true);
            }
        }
    }

    private void update(Player player){
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()){
            //TODO put right development cards
        }
        int heightLeaderCard = 200;
        int widthLeaderCard = 150;
        for (LeaderCard leaderCard: player.getNonActiveLeaderCards()){
            TextField textField = new TextField("Non Active");
            ImageView card = new ImageView(new Image(leaderCard.getPath()));
            card.setFitHeight(heightLeaderCard);
            card.setFitWidth(widthLeaderCard);
            leaderCards.getChildren().add(textField);
            leaderCards.getChildren().add(card);
        }
        for (LeaderCard leaderCard: player.getActiveLeaderCards()){
            TextField textField = new TextField("Active");
            ImageView card = new ImageView(new Image(leaderCard.getPath()));
            card.setFitHeight(heightLeaderCard);
            card.setFitWidth(widthLeaderCard);
            leaderCards.getChildren().add(textField);
            leaderCards.getChildren().add(card);
        }
    }

    private void setPickedAction(Class pickedActionClass){
        ClientAction pickedAction = null;
        for(ClientAction possibleAction : possibleActions){
            if(possibleAction.getClass().getName().equals(pickedActionClass.getName())){
                pickedAction = possibleAction;
                break;
            }
        }
        GUIController.getInstance().setPickedAction(pickedAction);
    }

}

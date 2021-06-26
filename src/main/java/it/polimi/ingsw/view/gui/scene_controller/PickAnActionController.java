package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.SortWarehouse;
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
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;

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
        //ClientPlayer self = GUIController.getInstance().getSelf();
        //setupShowSelf(); //TODO take the logic from ShowPlayerController
        possibleActions = GUIController.getInstance().getPossibleActions();
        setupPossibleButtons();
        //pickAnAction();
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
/*
    private void pickAnAction(){
        allButtons.addAll(showButtons);
        allButtons.addAll(turnButtons);
        for(ButtonBase buttonBase : allButtons){
            if(buttonBase.equals(actionMenuButton) && !buttonBase.isDisable()){
                for(MenuItem menuItem : ((MenuButton) buttonBase).getItems()){
                    menuItem.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            event -> setPickedTurnAction(((MenuButton) buttonBase).getItems().indexOf(menuItem)));
                }
            }else if(!buttonBase.isDisable()){
                buttonBase.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setPickedAction(allButtons.indexOf(buttonBase)));
            }
        }
    }

    private void setPickedAction(int index){
        ButtonBase buttonClicked = allButtons.get(index);
        ClientAction pickedAction;
        String classPassed;
        switch (buttonClicked.getId()){
            case "developmentButton":
                classPassed = ShowDevelopmentCards.class.toString();
                break;
            case "marketButton":
                classPassed = ShowMarket.class.toString();
                break;
            case "playersButton":
                classPassed = ShowPlayer.class.toString();
                break;
            case "discardButton":
                classPassed = DiscardLeaderCard.class.toString();
                break;
            case "activateButton":
                classPassed = ActivateLeaderCard.class.toString();
                break;
//            case "warehouseButton":
//                classPassed = SortWarehouse.class.toString();
//                break;
            case "endButton":
                classPassed = FinishTurn.class.toString();
                break;
            default:
                classPassed = ShowDevelopmentCards.class.toString();
        }
        pickedAction = getActionByClass(classPassed);
        GUIController.getInstance().setPickedAction(pickedAction);
    }

    private void setPickedTurnAction(int index){
        MenuItem itemClicked = actionMenuButton.getItems().get(index);
        ClientAction pickedAction;
        String classPassed;
        switch(itemClicked.getId()){
            case "productionItem":
                classPassed = ActivateProduction.class.toString();
                break;
            case "buyDevelopmentItem":
                classPassed = BuyDevelopmentCard.class.toString();
                break;
            case "takeFromMarketItem":
            default:
                classPassed = TakeFromMarket.class.toString();
        }
        pickedAction = getActionByClass(classPassed);
        GUIController.getInstance().setPickedAction(pickedAction);
    }

 */

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

/*
    private ClientAction getActionByClass(String classPassed){
        for(ClientAction clientAction : possibleActions){
            if(clientAction.getClass().toString().equals(classPassed)){
                return clientAction;
            }
        }
        return null;
    }

 */
}

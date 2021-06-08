package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.action.ActivateLeaderCard;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.DiscardLeaderCard;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.show.ShowAction;
import it.polimi.ingsw.client.action.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.action.show.ShowMarket;
import it.polimi.ingsw.client.action.show.ShowPlayer;
import it.polimi.ingsw.client.action.turn_action.ActivateProduction;
import it.polimi.ingsw.client.action.turn_action.BuyDevelopmentCard;
import it.polimi.ingsw.client.action.turn_action.TakeFromMarket;
import it.polimi.ingsw.client.action.turn_action.TurnAction;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class PickAnActionController {
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
        showButtons.add(developmentButton);
        showButtons.add(marketButton);
        showButtons.add(playersButton);

        turnButtons.add(discardButton);
        turnButtons.add(activateButton);
        turnButtons.add(warehouseButton);
        turnButtons.add(actionMenuButton);
        turnButtons.add(endButton);
        //TODO set show player itself
        //ClientPlayer self = GUIController.getInstance().getSelf();
        //setupShowSelf(); //TODO take the logic from ShowPlayerController
        possibleActions = GUIController.getInstance().getPossibleActions();
        setupPossibleButtons();
        pickAnAction();
    }
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

    private void setupPossibleButtons() {
        double usableOpacity = 1;
        for(ButtonBase buttonBase : showButtons){
            buttonBase.setOpacity(usableOpacity);
            buttonBase.setDisable(false);
        }
        for(ButtonBase buttonBase : turnButtons){
            buttonBase.setOpacity(usableOpacity);
            buttonBase.setDisable(false);
        }
        double unusableOpacity = 0.25;
        if (possibleActions.stream().allMatch(action -> action instanceof ShowAction)) {
            for(ButtonBase buttonBase : turnButtons){
                buttonBase.setOpacity(unusableOpacity);
                buttonBase.setDisable(true);
            }
        } else {
            if (possibleActions.stream().anyMatch(action -> action instanceof DiscardLeaderCard)) {
                turnButtons.get(turnButtons.indexOf(discardButton)).setOpacity(unusableOpacity);
                turnButtons.get(turnButtons.indexOf(discardButton)).setDisable(true);
            }
            if (possibleActions.stream().anyMatch(action -> action instanceof ActivateLeaderCard)) {
                turnButtons.get(turnButtons.indexOf(activateButton)).setOpacity(unusableOpacity);
                turnButtons.get(turnButtons.indexOf(activateButton)).setDisable(true);
            }
//            if (possibleActions.stream().anyMatch(action -> action instanceof SortWarehouse)) {
//                turnButtons.get(turnButtons.indexOf(warehouseButton)).setOpacity(unusableOpacity);
//                turnButtons.get(turnButtons.indexOf(warehouseButton)).setDisable(true);
//            }
            if (possibleActions.stream().anyMatch(action -> action instanceof TurnAction)) {
                turnButtons.get(turnButtons.indexOf(actionMenuButton)).setOpacity(unusableOpacity);
                turnButtons.get(turnButtons.indexOf(actionMenuButton)).setDisable(true);
            }
            if (possibleActions.stream().anyMatch(action -> action instanceof FinishTurn)) {
                turnButtons.get(turnButtons.indexOf(endButton)).setOpacity(unusableOpacity);
                turnButtons.get(turnButtons.indexOf(endButton)).setDisable(true);
            }
        }
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
    private ClientAction getActionByClass(String classPassed){
        for(ClientAction clientAction : possibleActions){
            if(clientAction.getClass().toString().equals(classPassed)){
                return clientAction;
            }
        }
        return null;
    }
}

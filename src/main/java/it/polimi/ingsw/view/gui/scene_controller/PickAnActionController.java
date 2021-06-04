package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class PickAnActionController {
    @FXML
    private ChoiceBox<String> pickAnActionBox;

    public void initialize(){
        ArrayList<ClientAction> possibleActions = GUIController.getInstance().getPossibleActions();
        for(ClientAction clientAction : possibleActions){
            pickAnActionBox.getItems().add(clientAction.toString());
        }
        pickAnActionBox.setOnAction((actionEvent -> setPickedAction(possibleActions)));
    }
    private void setPickedAction(ArrayList<ClientAction> possibleActions){
        ClientAction pickedAction = null;
        String pickedActionString = pickAnActionBox.getValue();
        for (ClientAction clientAction : possibleActions){
            if(clientAction.toString().equals(pickedActionString)){
                pickedAction = clientAction;
                break;
            }
        }
        GUIController.getInstance().setPickedAction(pickedAction);
    }
}

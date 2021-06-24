package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class PickResourceController {
    @FXML
    private Button coinButton;
    @FXML
    private Button stoneButton;
    @FXML
    private Button servantButton;
    @FXML
    private Button shieldButton;

    public void initialize(){
        coinButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Coins));
        stoneButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Stones));
        servantButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Servants));
        shieldButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Shields));
    }
    private void setPickedResource(ResourceType pickedResource){
        GUIController.getInstance().setPickedResource(pickedResource);
    }
}

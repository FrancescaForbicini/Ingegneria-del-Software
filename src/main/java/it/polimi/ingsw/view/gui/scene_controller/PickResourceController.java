package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;


public class PickResourceController {
    private final ArrayList<ResourceType> resourcesToChoose;
    @FXML
    private Button coinButton;
    @FXML
    private Button stoneButton;
    @FXML
    private Button servantButton;
    @FXML
    private Button shieldButton;

    public PickResourceController(ArrayList<ResourceType> resourcesToChoose) {
        this.resourcesToChoose = resourcesToChoose;
    }

    public void initialize(){
        coinButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Coins));
        stoneButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Stones));
        servantButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Servants));
        shieldButton.setOnAction(actionEvent -> setPickedResource(ResourceType.Shields));
        for (ResourceType resourceType: ResourceType.getAllValidResources()){
            if (resourcesToChoose.contains(resourceType))
                activateButtons(resourceType);
        }
    }

    private void activateButtons(ResourceType resourceType){
        switch (resourceType){
            case Coins:
                coinButton.setDisable(false);
                break;
            case Stones:
                stoneButton.setDisable(false);
                break;
            case Servants:
                servantButton.setDisable(false);
                break;
            case Shields:
                shieldButton.setDisable(false);
        }
    }

    /**
     * Sets the resource picked by the player
     * @param pickedResource the resource chosen
     */
    private void setPickedResource(ResourceType pickedResource){
        GUIController.getInstance().setPickedResource(pickedResource);
    }
}

package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;


/**
 * Sets the scene to pick the starting resources
 */
public class PickResourceController implements SceneController{
    private final ArrayList<ResourceType> resourcesToChoose;
    @FXML
    private Button coinButton;
    @FXML
    private Button stoneButton;
    @FXML
    private Button servantButton;
    @FXML
    private Button shieldButton;

    /**
     * Sets the resources to pick
     * @param resourcesToChoose available resources to pick
     */
    public PickResourceController(ArrayList<ResourceType> resourcesToChoose) {
        this.resourcesToChoose = resourcesToChoose;
    }

    /**
     * Initializes the scene
     */
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

    /**
     * Sets which buttons has to be activated
     * @param resourceType resource corresponding to the button to activate
     */
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

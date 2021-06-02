package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class PickStartingResourceController {
    @FXML
    private DialogPane numberOfResources;
    @FXML
    private Button okButton;
    @FXML
    private Spinner<Integer> coinsSpinner;
    @FXML
    private Spinner<Integer> stonesSpinner;
    @FXML
    private Spinner<Integer> servantsSpinner;
    @FXML
    private Spinner<Integer> shieldsSpinner;

    public void initialize(){
        Integer number = GUIController.getInstance().getNumberOfResources();
        numberOfResources.setContentText(number.toString());
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setPickedResources());
    }
    private void setPickedResources(){
        ArrayList<ResourceType> pickedResources = new ArrayList<>();
        if(coinsSpinner.getValue()>0){
            pickedResources.add(ResourceType.Coins);
        }
        if(stonesSpinner.getValue()>0){
            pickedResources.add(ResourceType.Stones);
        }
        if(servantsSpinner.getValue()>0){
            pickedResources.add(ResourceType.Servants);
        }
        if(shieldsSpinner.getValue()>0){
            pickedResources.add(ResourceType.Shields);
        }
        GUIController.getInstance().setPickedResources(pickedResources);
    }
}

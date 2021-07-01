package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ChooseQuantityController {
    private final ResourceType resourceToTake;
    private final int maxQuantity;

    @FXML
    VBox msgBox;
    @FXML
    Spinner<Integer> quantitySpinner;
    @FXML
    Button okButton;

    public ChooseQuantityController(ResourceType resourceToTake, int maxQuantity){
        this.resourceToTake = resourceToTake;
        this.maxQuantity = maxQuantity;
    }

    public void initialize(){
        Label msgLabel = new Label("Choose the quantity of this resource to take from strongbox ");
        msgBox.getChildren().add(msgLabel);
        ImageView resourceImageView = (ImageView) SceneManager.getInstance().getNode(ResourceType.getPath(resourceToTake));
        msgBox.getChildren().add(resourceImageView);
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxQuantity, 1));

        okButton.setOnAction(actionEvent -> setChosenQuantity());
    }

    private void setChosenQuantity(){
        int chosenQuantity = 0;
        if(quantitySpinner.getValue()!=null){
            chosenQuantity = quantitySpinner.getValue();
        }
        GUIController.getInstance().setChosenQuantity(chosenQuantity);
    }
}

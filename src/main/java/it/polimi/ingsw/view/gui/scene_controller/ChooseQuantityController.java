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



public class ChooseQuantityController {
    private final ResourceType resourceToTake;
    private final int maxQuantity;

    @FXML
    HBox msgBox;
    @FXML
    Spinner<Integer> quantitySpinner;
    @FXML
    Button okButton;

    public ChooseQuantityController(ResourceType resourceToTake, int maxQuantity){
        this.resourceToTake = resourceToTake;
        this.maxQuantity = maxQuantity;
    }

    public void initialize(){
        Label msgLabel1 = new Label("Choose the quantity of ");
        msgBox.getChildren().add(msgLabel1);
        ImageView resourceImageView = (ImageView) SceneManager.getInstance().getNode(ResourceType.getPath(resourceToTake));
        msgBox.getChildren().add(resourceImageView);
        Label msgLabel2 = new Label(" from the strongbox: ");
        msgBox.getChildren().add(msgLabel2);
        quantitySpinner = new Spinner<>();
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,maxQuantity));

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

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
import javafx.scene.layout.VBox;

/**
 * Scene to choose the quantity of a resource
 */
public class ChooseQuantityController implements SceneController {
    private final ResourceType resourceToTake;
    private final int maxQuantity;

    @FXML
    VBox msgBox;
    @FXML
    Spinner<Integer> quantitySpinner;
    @FXML
    Button okButton;

    /**
     * Initializes the resource to take and its possible quantity
     * @param resourceToTake resource to take
     * @param maxQuantity possible amount to take
     */
    public ChooseQuantityController(ResourceType resourceToTake, int maxQuantity){
        this.resourceToTake = resourceToTake;
        this.maxQuantity = maxQuantity;
    }

    /**
     * Initializes the scene
     */
    public void initialize(){
        Label msgLabel = new Label("Choose the quantity of this resource to take from strongbox ");
        msgBox.getChildren().add(msgLabel);
        ImageView resourceImageView = (ImageView) SceneManager.getInstance().getNode(ResourceType.getPath(resourceToTake));
        msgBox.getChildren().add(resourceImageView);
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxQuantity, 1));

        okButton.setOnAction(actionEvent -> setChosenQuantity());
    }

    /**
     * Sets the quantity chosen of the resource
     */
    private void setChosenQuantity(){
        int chosenQuantity = 0;
        if(quantitySpinner.getValue()!=null){
            chosenQuantity = quantitySpinner.getValue();
        }
        GUIController.getInstance().setChosenQuantity(chosenQuantity);
    }
}

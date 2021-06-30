package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ChooseDepotController {
    private final ArrayList<WarehouseDepot> depotsToChoose;

    @FXML
    private FlowPane depots;

    public ChooseDepotController(ArrayList<WarehouseDepot> depotsToChoose) {
        this.depotsToChoose = depotsToChoose;
    }

    public void initialize(){
        for(int i=0; i < depotsToChoose.size(); i++){
            HBox possibleDepots = new HBox();
            Button choose = new Button();
            choose.setText("choose");
            WarehouseDepot possibleDepot = depotsToChoose.get(i);
            Label depotID = new Label();
            depotID.setText(String.valueOf(possibleDepot.getDepotID()));
            possibleDepots.getChildren().add(depotID);
            if (!possibleDepot.isEmpty()) {
                ImageView resource = new ImageView(new Image(possibleDepot.getResourceType().getPath()));
                for (int j = 0; j < possibleDepot.getQuantity(); j++) {
                    possibleDepots.getChildren().add(resource);
                }
            }
            else{
                TextField textField = new TextField();
                textField.setText("empty");
                possibleDepots.getChildren().add(textField);
            }
            int finalI = i;
            choose.setOnAction(actionEvent -> setPickedDepotIndex(finalI));
            possibleDepots.getChildren().add(choose);
            depots.getChildren().add(possibleDepots);
        }
    }

    private void setPickedDepotIndex(int pickedDepotIndex){
        GUIController.getInstance().setPickedIndex(pickedDepotIndex);
    }
}

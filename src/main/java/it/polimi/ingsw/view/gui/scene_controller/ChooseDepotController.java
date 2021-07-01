package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ChooseDepotController {
    private final ArrayList<WarehouseDepot> depotsToChoose;

    @FXML
    private VBox depots;

    public ChooseDepotController(ArrayList<WarehouseDepot> depotsToChoose) {
        this.depotsToChoose = depotsToChoose;
    }

    public void initialize(){
        for(int i=0; i < depotsToChoose.size(); i++){
            System.out.println("DEPOT : " + depotsToChoose.size());
            HBox possibleDepots = new HBox();
            Button choose = new Button();
            choose.setText("choose");
            WarehouseDepot possibleDepot = depotsToChoose.get(i);
            Label depotID = new Label();
            depotID.setText(String.valueOf(possibleDepot.getDepotID()));
            possibleDepots.getChildren().add(depotID);
            if (!possibleDepot.isEmpty()) {
                ImageView resource = (ImageView) SceneManager.getInstance().getNode(ResourceType.getPath(possibleDepot.getResourceType()));
                Label quantity = new Label();
                quantity.setText("Quantity: " + possibleDepot.getQuantity());
                possibleDepots.getChildren().add(resource);
                possibleDepots.getChildren().add(quantity);
            }
            else{
                Label textField = new Label();
                textField.setText(" EMPTY ");
                possibleDepots.getChildren().add(textField);
            }
            int finalI = i;
            choose.setOnAction(actionEvent -> setPickedDepotIndex(finalI));
            possibleDepots.getChildren().add(choose);
            possibleDepots.setPrefSize(200,200);
            depots.getChildren().add(possibleDepots);
            depots.setAlignment(Pos.CENTER);
        }
    }

    private void setPickedDepotIndex(int pickedDepotIndex){
        GUIController.getInstance().setPickedIndex(pickedDepotIndex);
    }
}

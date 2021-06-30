package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChooseDepotController {
    private final ArrayList<WarehouseDepot> depotsToChoose;
    @FXML
    private GridPane possibleDepotsGrid;

    public ChooseDepotController(ArrayList<WarehouseDepot> depotsToChoose) {
        this.depotsToChoose = depotsToChoose;
    }

    public void initialize(){
        possibleDepotsGrid = new GridPane();
        for(int i=0; i< depotsToChoose.size(); i++){
            WarehouseDepot possibleDepot = depotsToChoose.get(i);
            Label depotID = new Label();
            depotID.setText(String.valueOf(possibleDepot.getDepotID()));
            possibleDepotsGrid.add(depotID,i,0);
            GridPane level = new GridPane();
            level.setMinSize(50,50);
            for(int j=0; j<possibleDepot.getLevel(); j++){
                if(j<possibleDepot.getQuantity()){
                    ImageView resource = new ImageView(new Image(possibleDepot.getResourceType().getPath()));
                    level.add(resource,0,j);
                }
            }
            level.setGridLinesVisible(true);
            possibleDepotsGrid.add(level,i,1);
            Button choiceButton = new Button();
            choiceButton.setText("Choose this depot");
            int finalI = i;
            choiceButton.setOnAction(actionEvent -> setPickedDepotIndex(finalI));
            possibleDepotsGrid.add(choiceButton,i,2);
        }
    }

    private void setPickedDepotIndex(int pickedDepotIndex){
        GUIController.getInstance().setPickedIndex(pickedDepotIndex);
    }
}

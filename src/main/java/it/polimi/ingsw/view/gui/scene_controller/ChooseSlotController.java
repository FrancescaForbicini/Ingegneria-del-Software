package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChooseSlotController {

    private final ArrayList<DevelopmentSlot> developmentSlots;

    @FXML
    private GridPane slots;

    @FXML
    private Button slot0;

    @FXML
    private Button slot1;

    @FXML
    private Button slot2;

    public ChooseSlotController(ArrayList<DevelopmentSlot> developmentSlots) {
        this.developmentSlots = developmentSlots;
    }
    public void initialize() {
        slots.setGridLinesVisible(true);
        for (int i = 0; i < 3 ; i++){
            int finalI = i;
            Label choose = new Label();
            DevelopmentSlot developmentSlot = null;
            if (developmentSlots.stream().anyMatch(slot -> slot.getSlotID() == finalI)){
                for (int numberSlot = 0; numberSlot < 3; numberSlot++ ) {
                    if (developmentSlots.get(numberSlot).getSlotID() == i) {
                        developmentSlot = developmentSlots.get(numberSlot);
                        break;
                    }
                }
                if (developmentSlot== null)
                    developmentSlot = developmentSlots.get(0);
                if(developmentSlot.showCardOnTop().isPresent())
                    choose.setGraphic(SceneManager.getInstance().getNode(developmentSlots.get(i).getCards().getLast().getPath(),150,100));
                else
                    choose.setText("Choose");
                switch (developmentSlot.getSlotID()){
                    case 0:
                        slot0.setGraphic(choose);
                        slot0.setOnAction(actionEvent -> setSlot(0));
                        slot0.setDisable(false);
                        break;
                    case 1:
                        slot1.setGraphic(choose);
                        slot1.setOnAction(actionEvent -> setSlot(1));
                        slot1.setDisable(false);
                        break;
                    case 2:
                        slot2.setGraphic(choose);
                        slot2.setOnAction(actionEvent -> setSlot(2));
                        slot2.setDisable(false);
                        break;
                    default:
                }
            }
        }
    }
    private void setSlot(int slotIndex){
        GUIController.getInstance().setPickedIndex(slotIndex);
    }
}

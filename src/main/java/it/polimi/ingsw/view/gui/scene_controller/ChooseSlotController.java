package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChooseSlotController {

    private final ArrayList<DevelopmentSlot> developmentSlots;

    @FXML
    private GridPane slots;

    @FXML
    private Button slot1;

    @FXML
    private Button slot2;

    @FXML
    private Button slot3;

    public ChooseSlotController(ArrayList<DevelopmentSlot> developmentSlots) {
        this.developmentSlots = developmentSlots;
    }
    public void initialize() {
        int numberSlot = 0;
        for (Node node: slots.getChildren()){
            Button choose = (Button) node;
            if (developmentSlots.get(numberSlot).getSlotID() == numberSlot){
                if (developmentSlots.get(numberSlot).showCardOnTop().isPresent()) {
                    ImageView imageView = (ImageView) SceneManager.getInstance().getNode(
                            developmentSlots.get(numberSlot).getCards().getFirst().getPath(),
                            slots.getWidth(),
                            slots.getHeight()
                    );
                    choose.setGraphic(imageView);
                }
                else{
                    choose.setText("Choose");
                }
                int finalNumberSlot = numberSlot;
                choose.setOnAction(actionEvent -> setSlot(finalNumberSlot));
            }
            numberSlot++;
        }
    }
    private void setSlot(int slotIndex){
        GUIController.getInstance().setPickedIndex(slotIndex);
    }
}

package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Scenes to choose the slot
 */
public class ChooseSlotController implements SceneController{

    private final ArrayList<DevelopmentSlot> developmentSlots;
    private final double HEIGHT = 150;
    private final double WIDTH = 100;


    @FXML
    private GridPane slots;

    /**
     * Sets the lists of possible slots to choose
     * @param developmentSlots list of slots available
     */
    public ChooseSlotController(ArrayList<DevelopmentSlot> developmentSlots) {
        this.developmentSlots = developmentSlots;
    }

    /**
     * Initializes the scene
     */
    public void initialize() {
        slots.setGridLinesVisible(true);
        for(int i=0; i<developmentSlots.size(); i++){
            FlowPane singleSlot = new FlowPane();
            if(developmentSlots.get(i).showCardOnTop().isPresent()){
                singleSlot.getChildren().add(getNodeSlot(developmentSlots.get(i).getCards().getLast(),i));
            } else {
                Button slotButton = new Button("slot" + developmentSlots.get(i).getSlotID());
                int finalI = i;
                slotButton.setOnAction(actionEvent -> setSlot(finalI));
                singleSlot.getChildren().add(slotButton);
            }
            slots.add(singleSlot,i,0);
        }
    }

    /**
     * Sets the slot chosen
     * @param slotIndex the index of the slot chosen
     */
    private void setSlot(int slotIndex){
        GUIController.getInstance().setPickedIndex(slotIndex);
    }

    /**
     * Gets the image corresponding to the slot to show
     * @param developmentCard development card in the slot
     * @param index index of the slot
     * @return image of the slot
     */
    private Node getNodeSlot(DevelopmentCard developmentCard, int index){
        FlowPane cardPane = new FlowPane();
        Button cardButton = new Button();
        if(developmentCard.getPath()!=null){
            cardButton.setGraphic(SceneManager.getInstance().getNode(developmentCard.getPath(),HEIGHT, WIDTH));
        } else {
            cardButton.setText("Choose");
        }
        cardButton.setOnAction(actionEvent -> setSlot(index));
        cardPane.getChildren().add(cardButton);
        return cardPane;
    }
}

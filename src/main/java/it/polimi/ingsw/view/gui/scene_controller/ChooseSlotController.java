package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChooseSlotController {

    private final ArrayList<DevelopmentSlot> developmentSlots;
    private final double HEIGHT = 150;
    private final double WIDTH = 100;


    @FXML
    private GridPane slots;

    public ChooseSlotController(ArrayList<DevelopmentSlot> developmentSlots) {
        this.developmentSlots = developmentSlots;
    }
    public void initialize() {
        slots.setGridLinesVisible(true);
        for(int i=0; i<developmentSlots.size(); i++){
            FlowPane singleSlot = new FlowPane();
            if(developmentSlots.get(i).showCardOnTop().isPresent()){
                singleSlot.getChildren().add(getNodeSlot(developmentSlots.get(i).getCards().getLast(),i));
            } else {
                Button slotButton = new Button("Choose");
                int finalI = i;
                slotButton.setOnAction(actionEvent -> setSlot(finalI));
                singleSlot.getChildren().add(slotButton);
            }
            slots.add(singleSlot,i,0);
        }
    }
    private void setSlot(int slotIndex){
        GUIController.getInstance().setPickedIndex(slotIndex);
    }

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

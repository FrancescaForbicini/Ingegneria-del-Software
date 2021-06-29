package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class PickLeaderCardsController  {
    @FXML
    private HBox buttons;

    private final int height = 400;
    private final int width = 300;

    public void initialize() {
        ArrayList<LeaderCard> proposedLeaderCards = GUIController.getInstance().getProposedLeaderCards();
        for (int i = 0; i < proposedLeaderCards.size();i++){
            Button buttonToAdd = new Button();
            ImageView imageView = new ImageView(new Image(proposedLeaderCards.get(i).getPath()));
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            buttonToAdd.setGraphic(imageView);
            buttonToAdd.setDisable(false);
            int finalI = i;
            buttonToAdd.setOnMousePressed(actionEvent -> setPickedLeaderCard(finalI));
            buttons.getChildren().add(buttonToAdd);
        }
    }

    /**
     * Sets the leader card chosen by the player
     * @param pickedLeaderCardIndex the index of the leader chosen
     */
    private void setPickedLeaderCard(int pickedLeaderCardIndex){
        GUIController.getInstance().setPickedIndex(pickedLeaderCardIndex);
    }

}

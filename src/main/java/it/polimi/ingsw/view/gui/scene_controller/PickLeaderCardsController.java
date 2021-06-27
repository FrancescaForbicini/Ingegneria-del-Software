package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.LeaderCard;
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

    public void initialize() {
        ArrayList<LeaderCard> proposedLeaderCards = GUIController.getInstance().getProposedLeaderCards();
        ArrayList<Button> buttonsToChooseLeaderCards = new ArrayList<>();
        for (int i = 0; i < proposedLeaderCards.size();i++){
            Button buttonToAdd = new Button();
            ImageView imageView = new ImageView(new Image(proposedLeaderCards.get(i).getPath()));
            imageView.setFitHeight(400);
            imageView.setFitWidth(300);
            buttonToAdd.setGraphic(imageView);
            buttonToAdd.setDisable(false);
            int finalI = i;
            buttonToAdd.setOnAction(actionEvent -> setPickedLeaderCard(finalI));
            buttons.getChildren().add(buttonToAdd);
            buttonsToChooseLeaderCards.add(buttonToAdd);
        }
    }

    private void setPickedLeaderCard(int pickedLeaderCardIndex){
        GUIController.getInstance().setPickedIndex(pickedLeaderCardIndex);
    }

}

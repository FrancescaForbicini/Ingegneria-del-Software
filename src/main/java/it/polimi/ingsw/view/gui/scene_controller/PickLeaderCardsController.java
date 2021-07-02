package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import it.polimi.ingsw.view.gui.custom_gui.CustomCard;
import it.polimi.ingsw.view.gui.custom_gui.CustomEligible;
import it.polimi.ingsw.view.gui.custom_gui.CustomLeaderCard;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class PickLeaderCardsController  {
    private final ArrayList<LeaderCard> proposedLeaderCards;
    @FXML
    private HBox buttons;

    private final int HEIGHT = 400;
    private final int WIDTH = 300;

    public PickLeaderCardsController(ArrayList<LeaderCard> proposedLeaderCards) {
        this.proposedLeaderCards = proposedLeaderCards;
    }
    public void initialize() {
        FlowPane mainPane = new FlowPane();
        for (int i = 0; i < proposedLeaderCards.size();i++){
            Button buttonToAdd = new Button();
            //TODO custom
            setNodeToShow(proposedLeaderCards.get(i), mainPane, buttonToAdd);
            //ImageView imageView = (ImageView) SceneManager.getInstance().getNode(proposedLeaderCards.get(i).getPath(), HEIGHT, WIDTH);
            //buttonToAdd.setGraphic(imageView);
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

    private void setNodeToShow(LeaderCard leaderCard, Pane container, Button buttonToAdd) {
        if (SceneManager.custom) {
            CustomCard customCard = CustomEligible.getCustomCard(leaderCard);
            container.getChildren().add(customCard.getNodeToShow(HEIGHT, WIDTH));
            buttonToAdd.setText("Pick this");
        } else {
            ImageView imageView = (ImageView) SceneManager.getInstance().getNode(leaderCard.getPath(), HEIGHT, WIDTH);
            buttonToAdd.setGraphic(imageView);
        }
        container.getChildren().add(buttonToAdd);
    }


    }

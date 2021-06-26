package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class PickLeaderCardsController {
    @FXML
    private Button button0;
    @FXML
    private ImageView card0;
    @FXML
    private Button button1;
    @FXML
    private ImageView card1;
    @FXML
    private Button button2;
    @FXML
    private ImageView card2;
    @FXML
    private Button button3;
    @FXML
    private ImageView card3;

    public void initialize(){
        ArrayList<LeaderCard> proposedLeaderCards = GUIController.getInstance().getProposedLeaderCards();

        ArrayList<Button> buttons = new ArrayList<>();
        card0.setImage(new Image((proposedLeaderCards.get(0).getPath())));
        button0.setOnAction(actionEvent -> setPickedLeaderCard(0));
        buttons.add(button0);
        card1.setImage(new Image(proposedLeaderCards.get(1).getPath()));
        button1.setOnAction(actionEvent -> setPickedLeaderCard(1));
        buttons.add(button1);
        //if the player has to choose both of the starting leader cards
        if (proposedLeaderCards.size() > 3){
            card2.setImage(new Image(proposedLeaderCards.get(2).getPath()));
            button2.setOnAction(actionEvent -> setPickedLeaderCard(2));
            buttons.add(button2);
            card3.setImage(new Image(proposedLeaderCards.get(3).getPath()));
            button3.setOnAction(actionEvent -> setPickedLeaderCard(3));
            buttons.add(button3);

        }
        else {
            //if the player has to choose the second leader cards
            if (proposedLeaderCards.size() == 3) {
                card2.setImage(new Image(proposedLeaderCards.get(2).getPath()));
                button2.setOnAction(actionEvent -> setPickedLeaderCard(2));
                buttons.add(button2);
            }
        }
        for(Button button : buttons){
            //disable all buttons
            button.setDisable(true);
            button.setVisible(false);
        }
        for(int i=0; i<proposedLeaderCards.size(); i++){
            //activate only useful buttons
            buttons.get(i).setDisable(false);
            buttons.get(i).setVisible(true);
        }
    }

    private void setPickedLeaderCard(int pickedLeaderCardIndex){
        GUIController.getInstance().setPickedIndex(pickedLeaderCardIndex);
    }

}

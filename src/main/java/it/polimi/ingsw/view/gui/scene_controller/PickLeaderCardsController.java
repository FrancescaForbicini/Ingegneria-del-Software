package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    private ArrayList<LeaderCard> proposedLeaderCards;

    public void initialize(){
//        ClientPlayer self = GUIController.getInstance().getSelf();
        ArrayList<ImageView> cards = new ArrayList<>();
        ArrayList<Button> buttons = new ArrayList<>();
        button0.setOnAction(actionEvent -> setPickedLeaderCard(0));
        buttons.add(button0);
        button1.setOnAction(actionEvent -> setPickedLeaderCard(1));
        buttons.add(button1);
        button2.setOnAction(actionEvent -> setPickedLeaderCard(2));
        buttons.add(button2);
        button3.setOnAction(actionEvent -> setPickedLeaderCard(3));
        buttons.add(button3);
        cards.add(card0);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        proposedLeaderCards = GUIController.getInstance().getProposedLeaderCards();
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
        GUIController.getInstance().setPickedLeaderCardIndex(pickedLeaderCardIndex);
    }

    private String getLeaderCardPath(LeaderCard leaderCard) {
        String cardType; //TODO maybe add method to LeaderCard class or maybe add path attribute and getPath() method
        if(leaderCard instanceof AdditionalDepot) {
            cardType = ((AdditionalDepot) leaderCard).getDepotResourceType().name();
        }else if(leaderCard instanceof AdditionalTradingRule){
            cardType = ((AdditionalTradingRule) leaderCard).getAdditionalTradingRule().getInput().keySet().getClass().getName();
        }else if(leaderCard instanceof AssignWhiteMarble){
            cardType = ((AssignWhiteMarble) leaderCard).getResourceType().name();
        }else{
            cardType = ((Discount) leaderCard).getResourceType().name();
        }
        return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Cards/LeaderCards/"+
                leaderCard.getClass().toString()+cardType+".png";
    }
}

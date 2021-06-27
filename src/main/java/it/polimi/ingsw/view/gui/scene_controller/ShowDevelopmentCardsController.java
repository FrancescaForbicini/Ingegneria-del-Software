package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class ShowDevelopmentCardsController {

    @FXML
    private GridPane decksGrid;
    @FXML
    private Button back;

    private final int width = 150;
    private final int height = 200;

    public void initialize(){

        ArrayList<DevelopmentCard> developmentCards = GUIController.getInstance().getDevelopmentCards();
        Image cardFile;
        ImageView imageView;
        for(DevelopmentCard developmentCard : developmentCards){
            cardFile = new Image(developmentCard.getPath());
            imageView = new ImageView();
            imageView.setImage(cardFile);
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            decksGrid.add(imageView,colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
            GridPane.setHalignment(imageView, HPos.CENTER);
        }
        //sets missed cards with the back image
        if (developmentCards.size() != 12)
            setMissedCards(developmentCards);
        back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
    }


    private int colorToColumn(DevelopmentColor color){
        return switch (color) {
            case Blue -> 0;
            case Green -> 1;
            case Purple -> 2;
            case Yellow -> 3;
            default -> 4;
        };
    }

    private void setMissedCards(ArrayList<DevelopmentCard> developmentCards){
            for (DevelopmentColor developmentColor: DevelopmentColor.values()){
                int amountColor = (int ) developmentCards.stream().filter(card -> card.getColor().equals(developmentColor)).count();
                if (amountColor < 3){
                    for (int level = 1; level < 4; level++){
                        int finalLevel = level;
                        if (developmentCards.stream().noneMatch(developmentCard -> developmentCard.getColor().equals(developmentColor) && developmentCard.getLevel() == finalLevel)){
                            ImageView imageView = new ImageView(DevelopmentCard.getBackPath(developmentColor,level));
                            imageView.setFitHeight(height);
                            imageView.setFitWidth(width);
                            decksGrid.add(imageView,colorToColumn(developmentColor), 3 - level);
                        }
                    }
                }
            }
        }
}

package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
//TODO do not delete unused libraries, they are used in commented part
import java.util.ArrayList;
import java.util.Collection;

public class ShowDevelopmentCardsController {
    @FXML
    GridPane decksGrid;
    public void initialize(){
        /*TODO
        - attribute to know if custom settings or not
        - double method after check: if custom then customShow() else standardShow()

        private void customShow(){
            VBox cardBox;
            HBox cardPropertiesBox;
            Rectangle cardColorShape;
            Label cardLevelLabel;
            Label cardVictoryPointsLabel;
            VBox requirementsBox;
            HBox mapResIntBox;
            Rectangle resourceTypeShape;//TODO png
            Label resourceQuantityLabel;
            HBox tradingRuleBox;
            VBox inputBox;
            VBox outputBox;
            ArrayList<DevelopmentCard> developmentCards = GUIController.getInstance().getDevelopmentCards();
            for(DevelopmentCard developmentCard : developmentCards) {
                cardBox = new VBox();
                cardPropertiesBox = new HBox();
                cardColorShape = new Rectangle();
                cardLevelLabel = new Label();
                cardVictoryPointsLabel = new Label();
                requirementsBox = new VBox();
                tradingRuleBox = new HBox();
                inputBox = new VBox();
                outputBox = new VBox();

                cardColorShape.setFill(colorToPaint(developmentCard.getColor()));
                cardPropertiesBox.getChildren().add(0, cardColorShape);
                cardLevelLabel.setText(String.valueOf(developmentCard.getLevel()));
                cardPropertiesBox.getChildren().add(1, cardLevelLabel);
                cardVictoryPointsLabel.setText(String.valueOf(developmentCard.getVictoryPoints()));
                cardPropertiesBox.getChildren().add(2, cardVictoryPointsLabel);
                cardBox.getChildren().add(0, cardPropertiesBox);
                for (RequirementResource requirement : developmentCard.getRequirements()) {
                    //TODO probable easy fix: abstract Requirement -> interface Requirable
                    mapResIntBox = new HBox();
                    requirementsBox.getChildren().add(mapResIntBox);
                }
                cardBox.getChildren().add(1, requirementsBox);
                for(ResourceType resource : developmentCard.getTradingRule().getInput().keySet()){
                    resourceTypeShape = new Rectangle();
                    resourceQuantityLabel = new Label();
                    mapResIntBox = new HBox();
                    resourceTypeShape.setFill(resourceToPaint(resource));
                    resourceQuantityLabel.setText(String.valueOf(developmentCard.getTradingRule().getInput().get(resource)));
                    mapResIntBox.getChildren().add(0,resourceTypeShape);
                    mapResIntBox.getChildren().add(1,resourceQuantityLabel);
                    inputBox.getChildren().add(mapResIntBox);
                }
                tradingRuleBox.getChildren().add(0,inputBox);
                for(ResourceType resource : developmentCard.getTradingRule().getOutput().keySet()){
                    resourceTypeShape = new Rectangle();
                    resourceQuantityLabel = new Label();
                    mapResIntBox = new HBox();
                    resourceTypeShape.setFill(resourceToPaint(resource));
                    resourceQuantityLabel.setText(String.valueOf(developmentCard.getTradingRule().getInput().get(resource)));
                    mapResIntBox.getChildren().add(0,resourceTypeShape);
                    mapResIntBox.getChildren().add(1,resourceQuantityLabel);
                    inputBox.getChildren().add(mapResIntBox);
                }
                if(developmentCard.getTradingRule().getVictoryPoints()!=0){
                    Label faithPointsLabel = new Label();
                    faithPointsLabel.setText("FP: " + String.valueOf(developmentCard.getTradingRule().getFaithPoints()));
                }
                tradingRuleBox.getChildren().add(1,outputBox);
                cardBox.getChildren().add(2, tradingRuleBox);
                decksGrid.add(cardBox, colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
                GridPane.setHalignment(cardBox, HPos.CENTER);
            }
        }
        private void standardShow(){
        */
        ArrayList<DevelopmentCard> developmentCards = GUIController.getInstance().getDevelopmentCards();
        Image cardFile;
        ImageView imageView;
        for(DevelopmentCard developmentCard : developmentCards){
            cardFile = new Image(developmentCard.getPath());
            imageView = new ImageView();
            imageView.setImage(cardFile);
            decksGrid.add(imageView,colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
            GridPane.setHalignment(imageView, HPos.CENTER);
        }
    }

    private Paint colorToPaint(DevelopmentColor color){
        switch (color){
            case Green:
                return Color.GREEN;
            case Blue:
                return Color.BLUE;
            case Yellow:
                return Color.YELLOW;
            case Purple:
            default:
                return Color.PURPLE;
        }
    }
    private int colorToColumn(DevelopmentColor color){
        switch (color){
            case Green:
                return 0;
            case Blue:
                return 1;
            case Yellow:
                return 2;
            case Purple:
            default:
                return 3;
        }
    }
    private Color resourceToPaint(ResourceType type){
        Color color = null;
        switch (type){
            case Coins:
                color = Color.YELLOW;
                break;
            case Stones:
                color = Color.GREY;
                break;
            case Servants:
                color = Color.PURPLE;
                break;
            case Shields:
                color = Color.BLUE;
                break;
        }
        return color;
    }
}

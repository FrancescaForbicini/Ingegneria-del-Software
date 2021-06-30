package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ReactiveObserver;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class ShowDevelopmentCardsController extends ReactiveObserver {

    private final boolean buy;
    private final ArrayList<DevelopmentCard> developmentCards;
    private final double width = 150;
    private final double height = 200;

    @FXML
    private GridPane decksGrid;
    @FXML
    private Button back;


    public ShowDevelopmentCardsController(ClientGameObserverProducer clientGameObserverProducer,boolean buy,ArrayList<DevelopmentCard> developmentCards){
        super(clientGameObserverProducer);
        this.buy = buy;
        this.developmentCards = developmentCards;
    }

    public void initialize() {
        back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
        react(developmentCards);
    }

    public void react(ArrayList<DevelopmentCard> developmentCards){
        Image cardFile;
        ImageView imageView;
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()){
            cardFile = new Image(developmentCard.getPath());
            imageView = new ImageView();
            imageView.setImage(cardFile);
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            if (buy)
                showRightCards(developmentCard,imageView);
            else
                decksGrid.add(imageView,colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
            GridPane.setHalignment(imageView, HPos.CENTER);
        }
        //sets missed cards with the back image
        if (developmentCards.size() != 12 && !buy)
            setMissedCards(developmentCards);
    }

    private void setCardChosen(DevelopmentCard developmentCardChosen){
        GUIController.getInstance().setPickedDevelopmentCard(developmentCardChosen);
    }

    private void showRightCards(DevelopmentCard developmentCard, ImageView card){
        if (developmentCards.contains(developmentCard)) {
            Button choose = new Button();
            choose.setGraphic(card);
            choose.setOnAction(actionEvent -> setCardChosen(developmentCard));
            choose.setMaxWidth(width);
            choose.setMaxHeight(height);
            decksGrid.add(choose, colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
        }
        else {
            card = new ImageView(DevelopmentCard.getBackPath(developmentCard.getColor(), developmentCard.getLevel()));
            card.setFitHeight(height);
            card.setFitWidth(width);
            decksGrid.add(card, colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
        }
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
                            ImageView imageView = new ImageView(DevelopmentCard.getBackPath(developmentColor,level)); // TODO
                            imageView.setFitHeight(height);
                            imageView.setFitWidth(width);
                            decksGrid.add(imageView,colorToColumn(developmentColor), 3 - level);
                        }
                    }
                }
            }
        }

    @Override
    public void update() {
        ArrayList<DevelopmentCard> developmentCards = clientGameObserverProducer.getDevelopmentCards();
        if (developmentCards != null)
            Platform.runLater(() -> react(developmentCards));
    }
}

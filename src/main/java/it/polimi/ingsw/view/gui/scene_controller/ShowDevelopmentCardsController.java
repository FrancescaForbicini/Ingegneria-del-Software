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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShowDevelopmentCardsController extends ReactiveObserver {
    private static Map<String,ImageView> developmentCardsCache = new HashMap<>();
    private final boolean buy;
    private final ArrayList<DevelopmentCard> developmentCardsBuyable;
    private static final double width = 150;
    private static final double height = 200;
    @FXML
    private GridPane decksGrid;
    @FXML
    private Button back;

    public ShowDevelopmentCardsController(ClientGameObserverProducer clientGameObserverProducer){
        this(clientGameObserverProducer,false,null);
    }

    public ShowDevelopmentCardsController(ClientGameObserverProducer clientGameObserverProducer,boolean buy,ArrayList<DevelopmentCard> developmentCards){
        super(clientGameObserverProducer);
        this.buy = buy;
        this.developmentCardsBuyable = developmentCards;
    }

    public void initialize() {
        if (!buy)
            back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
        react(developmentCardsBuyable);
    }

    public void react(ArrayList<DevelopmentCard> developmentCardsBuyable){
        if (clientGameObserverProducer.getDevelopmentCards() == null)
            return;
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()){
            if (buy)
                showFrontOrBack(developmentCard);
            else
                decksGrid.add(getImage(developmentCard.getPath()),colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
        }
        //sets missed cards with the back image
        if (developmentCardsBuyable.size() != 12 && !buy)
            setMissedCards(developmentCardsBuyable);
    }

    private void setCardChosen(DevelopmentCard developmentCardChosen){
        GUIController.getInstance().setPickedDevelopmentCard(developmentCardChosen);
    }

    private void showFrontOrBack(DevelopmentCard developmentCard){
        Label cardToShow = new Label();
        String path = developmentCardsBuyable.stream().filter(card -> card.equals(developmentCard))
                .findFirst().map(DevelopmentCard::getPath)
                .orElse(DevelopmentCard.getBackPath(developmentCard.getColor(),developmentCard.getLevel()));
        if (developmentCardsBuyable.stream().anyMatch(card -> card.equals(developmentCard)))
            cardToShow.setOnMouseClicked(actionEvent -> setCardChosen(developmentCard));
        cardToShow.setGraphic(getImage(path));
        decksGrid.add(cardToShow, colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
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
                            decksGrid.add(getImage(developmentCards.get(level).getPath()),colorToColumn(developmentColor), 3 - level);
                        }
                    }
                }
            }
    }

    private static ImageView getImage(String path){
        if (developmentCardsCache.containsKey(path))
            return developmentCardsCache.get(path);
        Image cardFile = new Image(path);
        ImageView imageView = new ImageView();
        imageView.setImage(cardFile);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        GridPane.setHalignment(imageView,HPos.CENTER);
        developmentCardsCache.put(path,imageView);
        return imageView;
    }


    @Override
    public void update() {
        ArrayList<DevelopmentCard> developmentCards = clientGameObserverProducer.getDevelopmentCards();
        if (developmentCards != null)
            Platform.runLater(() -> react(developmentCards));
    }
}

package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.view.gui.ReactiveObserver;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class ShowDevelopmentCardsController extends ReactiveObserver {
    private final boolean buy;
    private final ArrayList<DevelopmentCard> developmentCardsToShow;
    private static final double WIDTH = 150;
    private static final double HEIGHT = 200;

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
        this.developmentCardsToShow = developmentCards;
    }

    public void initialize() {
        if (!buy)
            back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
        react(developmentCardsToShow);
    }

    public void react(ArrayList<DevelopmentCard> developmentCardsToShow){
        decksGrid.getChildren().clear();
        if (clientGameObserverProducer.getDevelopmentCards() == null)
            return;
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()) {
            if (buy)
                showFrontOrBack(developmentCard);
            else {
                decksGrid.add((getDevelopmentCard(developmentCard.getPath())), colorToColumn(developmentCard.getColor()), 3 - developmentCard.getLevel());
            }
        }
        //sets missed cards with the back image
        if (developmentCardsToShow.size() != 12 && !buy)
            showMissedCards(developmentCardsToShow);
    }

    private void setCardChosen(DevelopmentCard developmentCardChosen){
        GUIController.getInstance().setPickedDevelopmentCard(developmentCardChosen);
    }

    private void showFrontOrBack(DevelopmentCard developmentCard){
        Label cardToShow = new Label();
        String path = developmentCardsToShow.stream().filter(card -> card.equals(developmentCard))
                .findFirst().map(DevelopmentCard::getPath)
                .orElse(DevelopmentCard.getBackPath(developmentCard.getColor(),developmentCard.getLevel()));
        if (buy && developmentCardsToShow.stream().anyMatch(card -> card.equals(developmentCard)))
            cardToShow.setOnMouseClicked(actionEvent -> setCardChosen(developmentCard));
        cardToShow.setGraphic(getDevelopmentCard(path));
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

    private void showMissedCards(ArrayList<DevelopmentCard> developmentCardsAvailable){
        for (DevelopmentColor developmentColor: DevelopmentColor.values()){
            if (!developmentColor.equals(DevelopmentColor.Any)) {
                int amountColor = (int) developmentCardsAvailable.stream().filter(card -> card.getColor().equals(developmentColor)).count();
                if (amountColor < 3) {
                    for (int level = 1; level < 4; level++) {
                        int finalLevel = level;
                        if (developmentCardsAvailable.stream().noneMatch(card -> card.getColor().equals(developmentColor) && card.getLevel() == finalLevel)) {
                            decksGrid.add(getDevelopmentCard(DevelopmentCard.getBackPath(developmentColor, finalLevel)), colorToColumn(developmentColor), 3 - finalLevel);
                        }
                    }
                }
            }
        }
    }

    private static ImageView getDevelopmentCard(String path){
        return (ImageView) SceneManager.getInstance().getNode(path, HEIGHT, WIDTH);
    }



    @Override
    public void update() {
        ArrayList<DevelopmentCard> developmentCards = clientGameObserverProducer.getDevelopmentCards();
        if (developmentCards != null)
            Platform.runLater(() -> react(developmentCards));
    }
}

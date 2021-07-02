package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.HasPath;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;


public class ChooseTradingRulesController {
    private static final double CARD_WIDTH = 150;
    private static final double CARD_HEIGHT = 200;

    @FXML
    private HBox tradingRules;

    private final ArrayList<Eligible> availableProductions;

    public ChooseTradingRulesController(ArrayList<Eligible> availableProductions) {
        this.availableProductions = availableProductions;
    }

    public void initialize(){
        Button productionBtn;
        Node card;
        for (Eligible production : this.availableProductions) {
            productionBtn = new Button();
            String path = ((HasPath)production).getPath();
            if (path != null) {
                //TODO custom
                card = SceneManager.getInstance().getNode(path, CARD_HEIGHT, CARD_WIDTH);
                productionBtn.setGraphic(card);
            } else {
                productionBtn.setText("2 resources --> 1 resource");
            }
            productionBtn.setOnMouseClicked(
                    mouseEvent -> GUIController.getInstance().setPickedIndex(this.availableProductions.indexOf(production))
            );
            tradingRules.getChildren().add(productionBtn);

        }
    }

}

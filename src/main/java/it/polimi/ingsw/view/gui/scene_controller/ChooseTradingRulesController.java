package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.HasPath;
import it.polimi.ingsw.view.gui.SceneManager;
import it.polimi.ingsw.view.gui.custom_gui.CustomCard;
import it.polimi.ingsw.view.gui.custom_gui.CustomEligible;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


public class ChooseTradingRulesController {
    private static final double CARD_HEIGHT = 200;
    private static final double CARD_WIDTH = 150;


    @FXML
    private HBox tradingRules;

    private final ArrayList<Eligible> availableProductions;

    public ChooseTradingRulesController(ArrayList<Eligible> availableProductions) {
        this.availableProductions = availableProductions;
    }

    public void initialize(){
        Button productionBtn;
        FlowPane productionPane;
        Node card;
        for (Eligible production : this.availableProductions) {
            productionBtn = new Button();
            productionPane = new FlowPane();
            //String path = ((HasPath)production).getPath();
            setNodeToShow(production, productionPane, productionBtn);
            /*if (path != null) {
                //TODO custom
                //card = SceneManager.getInstance().getNode(path, CARD_HEIGHT, CARD_WIDTH);
                //productionBtn.setGraphic(card);
            } else {
                //TODO custom
                //productionBtn.setText("2 resources --> 1 resource");
            }

             */
            productionBtn.setOnMouseClicked(
                    mouseEvent -> GUIController.getInstance().setPickedIndex(this.availableProductions.indexOf(production))
            );
            tradingRules.getChildren().add(productionBtn);

        }
    }

    private void setNodeToShow(Eligible eligible, Pane container, Button productionBtn){
        String path = ((HasPath) eligible).getPath();
        if(path!=null) {
            if (SceneManager.custom) {
                CustomCard customCard = CustomEligible.getCustomCard(eligible);
                container.getChildren().add(customCard.getNodeToShow(CARD_HEIGHT, CARD_WIDTH));
                productionBtn.setText("Activate");
            } else {
                Node card = SceneManager.getInstance().getNode(path, CARD_HEIGHT, CARD_WIDTH);
                productionBtn.setGraphic(card);
            }
        } else {
            TradingRule basicProduction = ((DevelopmentCard) eligible).getTradingRule();
            int in = basicProduction.getInput().get(ResourceType.Any);
            int out = basicProduction.getOutput().get(ResourceType.Any);
            int fpts = basicProduction.getFaithPoints();
            productionBtn.setText( in + " resources --> " + out + " resources, " + fpts + " faithpoints");
        }
        container.getChildren().add(productionBtn);
    }

}

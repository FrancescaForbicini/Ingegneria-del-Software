package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class CustomDevelopmentCard extends CustomCard {
    private DevelopmentCard originalDevelopmentCard;
    private Node cardToModify;
    private CustomTradingRule customTradingRule;
    private DevelopmentCard modifiedDevelopmentCard;

    public CustomDevelopmentCard(DevelopmentCard developmentCard){
        new CustomDevelopmentCard(developmentCard, false);
    }
    public CustomDevelopmentCard(DevelopmentCard originalDevelopmentCard, boolean toModify) {
        if(toModify) {
            this.originalDevelopmentCard = originalDevelopmentCard;
        }
        else {
            modifiedDevelopmentCard = new DevelopmentCard(originalDevelopmentCard.getRequirements(), originalDevelopmentCard.getColor(), originalDevelopmentCard.getLevel(),originalDevelopmentCard.getVictoryPoints(),originalDevelopmentCard.getTradingRule(),originalDevelopmentCard.getPath());
        }
        super.setCustomRequirements(originalDevelopmentCard,toModify);
    }

    @Override
    public Node getNodeToShow(double height, double width){
        VBox cardToShow = new VBox();

        cardToShow.getChildren().add(super.getNodeVictoryPointsToShow(modifiedDevelopmentCard));

        cardToShow.getChildren().add(super.getNodeRequirementsToShow());

        //trading rule
        Node trNode = new CustomTradingRule(modifiedDevelopmentCard.getTradingRule(),false).getNodeToShow();

        cardToShow.getChildren().add(trNode);
        cardToShow.setPrefSize(height, width);

        return cardToShow;
    }


    @Override
    public Node getNodeToShow() {
        return getNodeToShow(PREF_HEIGHT, PREF_WIDTH);
    }

    @Override
    public Modifiable getModified(){
        int vpts = super.getModifiedVictoryPoints(originalDevelopmentCard);
        Collection<Requirement> requirementResources = super.getModifiedRequirements();
        if(super.isModified()){
            modified = true;
        }

        TradingRule tr = (TradingRule) customTradingRule.getModified();
        if(customTradingRule.isModified()){
            modified = true;
        }

        String path;
        if(modified){
            path = MODIFIED_PATH;
        } else {
            path = originalDevelopmentCard.getPath();
        }
        modifiedDevelopmentCard = new DevelopmentCard(requirementResources, originalDevelopmentCard.getColor(),originalDevelopmentCard.getLevel(),vpts,tr, path);
        return modifiedDevelopmentCard;
    }



    public Node getNodeToModify() {
        createCardToModify();
        return cardToModify;
    }

    private void createCardToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label developmentLabel = new Label("Development Card " + originalDevelopmentCard.getColor() + " " + originalDevelopmentCard.getLevel());
        lines.getChildren().add(developmentLabel);
        HBox parts = new HBox();

        //req & pts
        parts.getChildren().add(super.createReqsAndPtsToModify(originalDevelopmentCard));

        //tr
        customTradingRule = new CustomTradingRule(originalDevelopmentCard.getTradingRule(), true);
        parts.getChildren().add(customTradingRule.getNodeToModify());

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Collection;

public class CustomAdditionalTradingRule extends CustomLeaderCard {
    private AdditionalTradingRule originalLeaderCard;
    private CustomTradingRule customTradingRule;
    private AdditionalTradingRule modifiedLeaderCard;

    public CustomAdditionalTradingRule(LeaderCard leaderCard){
        new CustomAdditionalTradingRule(leaderCard, false);
    }
    public CustomAdditionalTradingRule(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AdditionalTradingRule) leaderCard;
        }
        else {
            this.modifiedLeaderCard= (AdditionalTradingRule) leaderCard;
        }
        super.setCustomRequirements(leaderCard,toModify);
    }


    @Override
    public Node getNodeToShow(double height, double width) {
        VBox cardToShow = new VBox();

        cardToShow.getChildren().add(super.getNodeVictoryPointsToShow(modifiedLeaderCard));

        cardToShow.getChildren().add(super.getNodeRequirementsToShow());

        //additional trading rule
        Node trNode = new CustomTradingRule(modifiedLeaderCard.getAdditionalTradingRule(),false).getNodeToShow();

        cardToShow.getChildren().add(trNode);
        cardToShow.setPrefSize(height, width);

        return cardToShow;
    }

    @Override
    public Node getNodeToShow() {
        return getNodeToShow(PREF_HEIGHT, PREF_WIDTH);
    }

    @Override
    public Modifiable getModified() {
        int vpts = super.getModifiedVictoryPoints(originalLeaderCard);
        Collection<Requirement> requirementColors = super.getModifiedRequirements();
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
            path = originalLeaderCard.getPath();
        }

        modifiedLeaderCard = new AdditionalTradingRule(vpts,requirementColors,tr, path);
        return modifiedLeaderCard;
    }


    @Override
    public Node getNodeToModify(){
        createNodeToModify();
        return cardToModify;
    }

    private void createNodeToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("LeaderCard Additional Trading Rule ");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //req & pts
        parts.getChildren().add(super.createReqsAndPtsToModify(originalLeaderCard));

        //tr
        customTradingRule = new CustomTradingRule(originalLeaderCard.getAdditionalTradingRule(), true);
        parts.getChildren().add(customTradingRule.getNodeToModify());

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }

}



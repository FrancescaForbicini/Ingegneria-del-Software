package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAdditionalTradingRule extends CustomLeaderCard {
    private AdditionalTradingRule originalLeaderCard;
    private Map<DevelopmentColor,Spinner<Integer>> modifiableRequirements;
    CustomTradingRule customTradingRule;
    private AdditionalTradingRule modifiedLeaderCard;

    public CustomAdditionalTradingRule(LeaderCard originalLeaderCard) {
        this.originalLeaderCard = (AdditionalTradingRule) originalLeaderCard;
        modifiableRequirements = new HashMap<>();
    }


    @Override
    public Node getToModify(){
        createToModify();
        return cardToModify;
    }

    private void createToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("LeaderCard Additional Trading Rule ");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //pts
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalLeaderCard.getVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(modifiableVictoryPoints);
        parts.getChildren().add(pointsPart);

        //reqs
        VBox requirementPart = new VBox();
        Label reqLabel = new Label("Requirements");
        requirementPart.getChildren().add(reqLabel);
        for (Requirement requirement : originalLeaderCard.getRequirements()) {
            HBox singleCardRequired = new HBox();
            RequirementColor requirementColor = (RequirementColor) requirement;
            Label colorLabel = new Label(requirementColor.getColor().toString());
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, requirementColor.getQuantity()));
            modifiableRequirements.put(requirementColor.getColor(),actualCost);
            singleCardRequired.getChildren().add(colorLabel);
            singleCardRequired.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleCardRequired);
        }
        parts.getChildren().add(requirementPart);



        //tr
        customTradingRule = new CustomTradingRule(originalLeaderCard.getAdditionalTradingRule());

        parts.getChildren().add(customTradingRule.getToModify());
        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }

    @Override
    public ImageView getModifiedImageView() {
        return null;
    }

    @Override
    public Modifiable getModified() {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalLeaderCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
        }
        Collection<Requirement> requirementColors = new ArrayList<>();
        for(DevelopmentColor colorRequired : modifiableRequirements.keySet()){
            int quantity = 1;
            int level = 0;
            if(modifiableRequirements.get(colorRequired).getValue() == null){
                for(Requirement requirement : originalLeaderCard.getRequirements()){
                    RequirementColor requirementColor = (RequirementColor) requirement;
                    level = requirementColor.getLevel();
                    if(requirementColor.getColor().equals(colorRequired)){
                        quantity = requirementColor.getQuantity();
                    }
                }
            } else {
                quantity = modifiableRequirements.get(colorRequired).getValue();
            }
            RequirementColor requirementColor = new RequirementColor(level,quantity,colorRequired);
            requirementColors.add(requirementColor);
        }

        TradingRule tr = (TradingRule) customTradingRule.getModified();
        modifiedLeaderCard = new AdditionalTradingRule(vpts,requirementColors,tr, originalLeaderCard.getPath());
        return modifiedLeaderCard;
    }
}



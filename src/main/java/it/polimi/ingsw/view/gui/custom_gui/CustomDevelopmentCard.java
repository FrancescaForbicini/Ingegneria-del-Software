package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class CustomDevelopmentCard extends CustomEligibleCard {
    private DevelopmentCard originalDevelopmentCard;
    private Node cardToModify;
    private Spinner<Integer> modifiableVictoryPoints;
    private CustomTradingRule customTradingRule;
    private DevelopmentCard modifiedDevelopmentCard;

    public CustomDevelopmentCard(DevelopmentCard originalDevelopmentCard, boolean toModify) {
        if(toModify) {
            this.originalDevelopmentCard = originalDevelopmentCard;
        }
        else {
            modifiedDevelopmentCard = originalDevelopmentCard;
        }
        super.setCustomRequirements(originalDevelopmentCard,toModify);
    }

    @Override
    public Node getModifiedNodeToShow(){
        return null;
    }


    @Override
    public Modifiable getModified(){
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalDevelopmentCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
        }
        Collection<Requirement> requirementResources = new ArrayList<>();
        for(ResourceType resourceRequired : modifiableRequirements.keySet()){
            int quantity = 1;
            if(modifiableRequirements.get(resourceRequired).getValue() == null){
                for(Requirement requirement : originalDevelopmentCard.getRequirements()){
                    RequirementResource requirementResource = (RequirementResource) requirement;
                    if(requirementResource.getResourceType().equals(resourceRequired)){
                        quantity = requirementResource.getQuantity();

                    }
                }
            } else {
                quantity = modifiableRequirements.get(resourceRequired).getValue();
            }
            RequirementResource requirementResource = new RequirementResource(quantity,resourceRequired);
            requirementResources.add(requirementResource);
        }

        TradingRule tr = (TradingRule) customTradingRule.getModified();
        modifiedDevelopmentCard = new DevelopmentCard(requirementResources, originalDevelopmentCard.getColor(),originalDevelopmentCard.getLevel(),vpts,tr,originalDevelopmentCard.getPath());
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
        parts.getChildren().add(super.createRequirementsAndVictoryPoints(originalDevelopmentCard));

        //tr
        customTradingRule = new CustomTradingRule(originalDevelopmentCard.getTradingRule(), true);
        parts.getChildren().add(customTradingRule.getNodeToModify());

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

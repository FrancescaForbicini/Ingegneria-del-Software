package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class CustomDevelopmentCard extends CustomClass {
    private DevelopmentCard originalDevelopmentCard;
    private Node cardToModify;
    private Spinner<Integer> modifiableVictoryPoints;
    private Map<ResourceType,Spinner<Integer>> modifiableRequirements;
    private Map<ResourceType,Spinner<Integer>> modifiableInput;
    private Map<ResourceType,Spinner<Integer>> modifiableOutput;
    private Spinner<Integer> modifiableFaithPoints;
    private DevelopmentCard modifiedDevelopmentCard;

    public CustomDevelopmentCard(DevelopmentCard developmentCard) {
        originalDevelopmentCard = developmentCard;
        modifiableRequirements = new HashMap<>();
        modifiableInput = new HashMap<>();
        modifiableOutput = new HashMap<>();
        modifiableFaithPoints = new Spinner<>();
    }

    private void createCardToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label developmentLabel = new Label("Development Card " + originalDevelopmentCard.getColor() + " " + originalDevelopmentCard.getLevel());
        lines.getChildren().add(developmentLabel);
        HBox parts = new HBox();

        //pts
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalDevelopmentCard.getVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(modifiableVictoryPoints);
        parts.getChildren().add(pointsPart);

        //reqs
        VBox requirementPart = new VBox();
        Label reqLabel = new Label("Requirements");
        requirementPart.getChildren().add(reqLabel);
        for (Requirement requirement : originalDevelopmentCard.getRequirements()) {
            HBox singleResource = new HBox();
            RequirementResource requirementResource = (RequirementResource) requirement;
            Label resourceTypeLabel = new Label(requirementResource.getResourceType().toString());
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, requirementResource.getQuantity()));
            modifiableRequirements.put(requirementResource.getResourceType(),actualCost);
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleResource);
        }
        parts.getChildren().add(requirementPart);

        //tr
        CustomTradingRule customTradingRule = new CustomTradingRule(originalDevelopmentCard.getTradingRule());

        parts.getChildren().add(customTradingRule.getToModify());
        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }

    public Node getToModify() {
        createCardToModify();
        return cardToModify;
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
        Map<ResourceType,Integer> in = new HashMap<>();
        for(ResourceType resourceIn : modifiableInput.keySet()){
            int quantity;
            if(modifiableInput.get(resourceIn).getValue()==null){
                quantity = originalDevelopmentCard.getTradingRule().getInput().get(resourceIn);
            } else {
                quantity = modifiableInput.get(resourceIn).getValue();
            }
            in.put(resourceIn,quantity);
        }
        Map<ResourceType,Integer> out = new HashMap<>();
        for(ResourceType resourceOut : modifiableOutput.keySet()){
            int quantity;
            if(modifiableOutput.get(resourceOut).getValue()==null){
                quantity = originalDevelopmentCard.getTradingRule().getOutput().get(resourceOut);
            } else {
                quantity = modifiableOutput.get(resourceOut).getValue();
            }
            out.put(resourceOut,quantity);
        }
        int fpts;
        if(modifiableFaithPoints.getValue() == null){
            fpts = originalDevelopmentCard.getTradingRule().getFaithPoints();
        } else {
            fpts = modifiableFaithPoints.getValue();
        }
        TradingRule tr = new TradingRule(in,out,fpts);
        modifiedDevelopmentCard = new DevelopmentCard(requirementResources, originalDevelopmentCard.getColor(),originalDevelopmentCard.getLevel(),vpts,tr,originalDevelopmentCard.getPath());
        return modifiedDevelopmentCard;
    }

    @Override
    public ImageView getModifiedImageView(){
        return new ImageView();
    }
}

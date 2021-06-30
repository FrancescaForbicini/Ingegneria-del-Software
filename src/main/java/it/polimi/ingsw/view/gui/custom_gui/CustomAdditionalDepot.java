package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AdditionalDepot;
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

public class CustomAdditionalDepot extends CustomLeaderCard{
    private AdditionalDepot originalLeaderCard;
    private Map<ResourceType,Spinner<Integer>> modifiableRequirements;
    private Spinner<Integer> modifiableLevel;
    private AdditionalDepot modifiedLeaderCard;

    public CustomAdditionalDepot(LeaderCard originalLeaderCard) {
        this.originalLeaderCard = (AdditionalDepot) originalLeaderCard;
        modifiableRequirements = new HashMap<>();
    }

    private void createToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("Leader Card Additional Depot");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();


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

        //depot
        AdditionalDepot additionalDepot = originalLeaderCard;
        Label resourceTypeLabel = new Label(additionalDepot.getDepotResourceType().toString());
        Spinner<Integer> actualLevelSpinner = new Spinner<>();
        actualLevelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 2));
        modifiableLevel = actualLevelSpinner;
        HBox modifiableDepotBox = new HBox();
        modifiableDepotBox.getChildren().add(resourceTypeLabel);
        modifiableDepotBox.getChildren().add(actualLevelSpinner);
        parts.getChildren().add(modifiableDepotBox);
        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;

    }

    @Override
    public ImageView getModifiedImageView() {
        return null;
    }

    @Override
    public Node getToModify() {
        createToModify();
        return cardToModify;
    }

    @Override
    public Modifiable getModified() {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalLeaderCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
        }
        Collection<Requirement> requirementResources = new ArrayList<>();
        for(ResourceType resourceRequired : modifiableRequirements.keySet()){
            int quantity = 1;
            if(modifiableRequirements.get(resourceRequired).getValue() == null){
                for(Requirement requirement : originalLeaderCard.getRequirements()){
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
        int lvl;
        if(modifiableLevel.getValue()==null){
            lvl = 2;
        } else {
            lvl = modifiableLevel.getValue();
        }

        modifiedLeaderCard = new AdditionalDepot(requirementResources,vpts,originalLeaderCard.getDepotResourceType(),lvl,originalLeaderCard.getPath());
        return modifiedLeaderCard;
    }
}

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

public class CustomAdditionalDepot extends CustomEligibleCard {
    private AdditionalDepot originalLeaderCard;
    private Spinner<Integer> modifiableLevel;
    private AdditionalDepot modifiedLeaderCard;

    public CustomAdditionalDepot(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AdditionalDepot) leaderCard;
        }
        else {
            modifiedLeaderCard = (AdditionalDepot) leaderCard;
        }
        super.setCustomRequirements(leaderCard,toModify);
    }

    @Override
    public Node getModifiedNodeToShow() {

        ImageView additionalDepotImageView = new ImageView();
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


    @Override
    public Node getNodeToModify() {
        createNodeToModify();
        return cardToModify;
    }

    private void createNodeToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("Leader Card Additional Depot");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //req & pts
        parts.getChildren().add(super.createRequirementsAndVictoryPoints(originalLeaderCard));

        //depot
        HBox modifiableDepotBox = new HBox();
        AdditionalDepot additionalDepot = originalLeaderCard;
        Label resourceTypeLabel = new Label(additionalDepot.getDepotResourceType().toString());
        modifiableDepotBox.getChildren().add(resourceTypeLabel);
        Spinner<Integer> actualLevelSpinner = new Spinner<>();
        actualLevelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 2));
        modifiableLevel = actualLevelSpinner;
        modifiableDepotBox.getChildren().add(actualLevelSpinner);
        parts.getChildren().add(modifiableDepotBox);

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

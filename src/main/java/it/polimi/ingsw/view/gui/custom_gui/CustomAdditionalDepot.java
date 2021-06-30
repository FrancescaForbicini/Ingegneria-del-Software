package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AdditionalDepot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAdditionalDepot extends CustomLeaderCard{
    private AdditionalDepot originalLeaderCard;
    private Map<ResourceType,Spinner<Integer>> modifiableRequirements;
    private Spinner<Integer> modifiableDepot;
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
        modifiableDepot = actualLevelSpinner;
        HBox modifiableDepotBox = new HBox();
        modifiableDepotBox.getChildren().add(resourceTypeLabel);
        modifiableDepotBox.getChildren().add(actualLevelSpinner);
        parts.getChildren().add(modifiableDepotBox);
        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;

    }
    @Override
    public Node getToModify() {
        createToModify();
        return super.getToModify();
    }
}

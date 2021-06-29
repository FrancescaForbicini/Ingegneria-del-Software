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
import java.util.Map;

public class CustomAdditionalDepot extends CustomLeaderCard{
    private ArrayList<RequirementResource> originalRequirements;
    private Map<ResourceType,Spinner<Integer>> requirements;
    private Map<ResourceType,Spinner<Integer>> depot;

    public CustomAdditionalDepot(LeaderCard originalLeaderCard) {
        super(originalLeaderCard);
        for(Requirement requirement : originalLeaderCard.getRequirements()){
            RequirementResource requirementResource = (RequirementResource) requirement;
            originalRequirements.add(requirementResource);
        }
    }

    private void createToModify(){
        HBox singleCard = new HBox();
        VBox lines = new VBox();
        Label developmentLabel = new Label("Leader Card" + originalLeaderCard.getClass());
        lines.getChildren().add(developmentLabel);
        HBox parts = new HBox();


        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        victoryPoints = new Spinner<>();
        victoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalLeaderCard.getVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(victoryPoints);
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
            requirements.put(requirementResource.getResourceType(),actualCost);
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleResource);
        }
        parts.getChildren().add(requirementPart);

        //depot
        AdditionalDepot additionalDepot = (AdditionalDepot) originalLeaderCard;
        Label resourceTypeLabel = new Label(additionalDepot.getDepotResourceType().toString());
        Spinner<Integer> actualQuantity = new Spinner<>();
        actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 2));
        depot.put(additionalDepot.getDepotResourceType(),actualQuantity);
        HBox singleResource = new HBox();
        singleResource.getChildren().add(resourceTypeLabel);
        singleResource.getChildren().add(actualQuantity);
        parts.getChildren().add(singleResource);
        lines.getChildren().add(parts);
        singleCard.getChildren().add(lines);
        cardToModify = singleCard;

    }
    @Override
    public Node getToModify() {
        createToModify();
        return super.getToModify();
    }
}

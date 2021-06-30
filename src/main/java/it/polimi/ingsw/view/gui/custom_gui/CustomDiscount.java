package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AdditionalDepot;
import it.polimi.ingsw.model.cards.Discount;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementColor;
import it.polimi.ingsw.model.requirement.RequirementResource;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class CustomDiscount extends CustomLeaderCard{
    private Discount originalLeaderCard;
    private Map<DevelopmentColor, Spinner<Integer>> modifiableRequirements;
    private Spinner<Integer> modifiableDiscount;
    private Discount modifiedLeaderCard;


    public CustomDiscount(LeaderCard originalLeaderCard) {
        this.originalLeaderCard = (Discount) originalLeaderCard;
        modifiableRequirements = new HashMap<>();
    }

    @Override
    public Node getToModify() {
        createToModify();
        return super.getToModify();
    }

    private void createToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("Leader Card Discount ");
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
            HBox singleCardRequired = new HBox();
            RequirementColor requirementColor = (RequirementColor) requirement;
            Label colorLabel = new Label(requirementColor.getColor().toString());
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, requirementColor.getQuantity()));
            modifiableRequirements.put(requirementColor.getColor(),actualCost);
            singleCardRequired.getChildren().add(colorLabel);
            singleCardRequired.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleCardRequired);
        }
        parts.getChildren().add(requirementPart);

        //depot
        Discount discount = originalLeaderCard;
        Label resourceTypeLabel = new Label(discount.getResourceType().toString());
        Spinner<Integer> actualDiscountSpinner = new Spinner<>();
        actualDiscountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 2));
        modifiableDiscount = actualDiscountSpinner;
        HBox singleResource = new HBox();
        singleResource.getChildren().add(resourceTypeLabel);
        singleResource.getChildren().add(actualDiscountSpinner);
        parts.getChildren().add(singleResource);
        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

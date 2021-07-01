package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.Discount;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class CustomDiscount extends CustomEligibleCard {
    private Discount originalLeaderCard;
    private Spinner<Integer> modifiableDiscount;
    private Discount modifiedLeaderCard;


    public CustomDiscount(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (Discount) leaderCard;
        }
        else {
            this.modifiedLeaderCard = (Discount) leaderCard;
        }
        super.setCustomRequirements(leaderCard, toModify);
    }

    @Override
    public Node getModifiedNodeToShow() {
        return null;
    }



    @Override
    public Modifiable getModified() {
        int vpts = super.getModifiedVictoryPoints(originalLeaderCard);
        Collection<Requirement> requirements = super.getModifiedRequirements();

        int amount;
        if(modifiableDiscount.getValue()==null){
            amount = 1;
        } else {
            amount = modifiableDiscount.getValue();
        }

        modifiedLeaderCard = new Discount(vpts,originalLeaderCard.getResourceType(),amount,requirements, originalLeaderCard.getPath());
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
        Label leaderLabel = new Label("Leader Card Discount ");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //req & pts
        parts.getChildren().add(super.createRequirementsAndVictoryPoints(originalLeaderCard));

        //depot
        Discount discount = originalLeaderCard;
        Label resourceTypeLabel = new Label(discount.getResourceType().toString());
        Spinner<Integer> actualDiscountSpinner = new Spinner<>();
        actualDiscountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1));
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

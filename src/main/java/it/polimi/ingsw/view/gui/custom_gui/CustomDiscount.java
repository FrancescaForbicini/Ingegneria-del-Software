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
import javafx.scene.shape.Circle;

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
    public Node getNodeToShow() {
        VBox cardToShow = new VBox();

        cardToShow.getChildren().add(super.getNodeVictoryPointsToShow(modifiedLeaderCard));

        cardToShow.getChildren().add(super.getNodeRequirementsToShow());

        //discount
        HBox discountBox = new HBox();
        Circle resource = new Circle();
        resource.setFill(modifiedLeaderCard.getResourceType().toPaint());
        discountBox.getChildren().add(resource);
        Label amountLabel = new Label(modifiedLeaderCard.getAmount() + " ");
        discountBox.getChildren().add(amountLabel);
        cardToShow.getChildren().add(discountBox);

        return cardToShow;
    }



    @Override
    public Modifiable getModified() {
        int vpts = super.getModifiedVictoryPoints(originalLeaderCard);
        Collection<Requirement> requirements = super.getModifiedRequirements();
        if(super.isModified()){
            modified = true;
        }

        int amount;
        if(modifiableDiscount.getValue()==null){
            amount = 1;
        } else {
            amount = modifiableDiscount.getValue();
            modified = true;
        }

        String path;
        if(modified){
            path = null;
        } else {
            path = originalLeaderCard.getPath();
        }

        modifiedLeaderCard = new Discount(vpts,originalLeaderCard.getResourceType(),amount,requirements, path);
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
        parts.getChildren().add(super.createReqsAndPtsToModify(originalLeaderCard));

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

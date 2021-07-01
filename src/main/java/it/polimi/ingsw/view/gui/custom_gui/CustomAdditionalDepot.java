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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Collection;

public class CustomAdditionalDepot extends CustomEligibleCard {
    private AdditionalDepot originalLeaderCard;
    private Spinner<Integer> modifiableLevel;
    private AdditionalDepot modifiedLeaderCard;
    private int quantity;

    public CustomAdditionalDepot(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AdditionalDepot) leaderCard;
        }
        else {
            modifiedLeaderCard = (AdditionalDepot) leaderCard;
        }
        super.setCustomRequirements(leaderCard,toModify);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public Node getNodeToShow() {
        VBox cardToShow = new VBox();

        cardToShow.getChildren().add(super.getNodeVictoryPointsToShow(modifiedLeaderCard));

        cardToShow.getChildren().add(super.getNodeRequirementsToShow());

        //additional depot
        HBox depotBox = new HBox();
        for(int i=0; i<modifiedLeaderCard.getDepotLevel(); i++){
            Circle place = new Circle();
            place.setFill(modifiedLeaderCard.getDepotResourceType().toPaint());
            if(i>=quantity) {
                place.setOpacity(0.25);
            }
            depotBox.getChildren().add(place);
        }
        cardToShow.getChildren().add(depotBox);

        return cardToShow;
    }



    @Override
    public Modifiable getModified() {
        int vpts = super.getModifiedVictoryPoints(originalLeaderCard);
        Collection<Requirement> requirementResources = super.getModifiedRequirements();
        if(super.isModified()){
            modified = true;
        }

        int lvl;
        if(modifiableLevel.getValue()==null){
            lvl = 2;
        } else {
            lvl = modifiableLevel.getValue();
            modified = true;
        }

        String path;
        if(modified){
            path = null;
        } else {
            path = originalLeaderCard.getPath();
        }

        modifiedLeaderCard = new AdditionalDepot(requirementResources,vpts,originalLeaderCard.getDepotResourceType(),lvl,path);
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
        parts.getChildren().add(super.createReqsAndPtsToModify(originalLeaderCard));

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

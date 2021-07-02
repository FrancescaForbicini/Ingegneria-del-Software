package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.Requirement;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CustomCard extends CustomClass {
    public static final double PREF_HEIGHT = 200;
    public static final double PREF_WIDTH = 150;
    protected Node cardToModify;
    protected Spinner<Integer> modifiableVictoryPoints;
    protected ArrayList<CustomRequirement> customRequirements;
    public abstract Node getNodeToShow(double height, double width);

    protected void setCustomRequirements(Eligible originalCard, boolean toModify){
        customRequirements = new ArrayList<>();
        for(Requirement requirement : originalCard.getRequirements()){
            customRequirements.add(new CustomRequirement(requirement, toModify));
        }
    }

    protected Node getNodeVictoryPointsToShow(Eligible modifiedCard){
        return new Label("Victory Points: " + modifiedCard.getVictoryPoints());
    }

    protected Node getNodeRequirementsToShow(){
        VBox reqsBox = new VBox();
        Label reqsLabel = new Label("Requires : ");
        reqsBox.getChildren().add(reqsLabel);

        for(CustomRequirement customRequirement : customRequirements){
            reqsBox.getChildren().add(customRequirement.getNodeToShow());
        }
        return reqsBox;
    }

    protected int getModifiedVictoryPoints(Eligible originalCard) {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
            if(vpts!= originalCard.getVictoryPoints()) {
                modified = true;
            }
        }
        return vpts;
    }
    protected Collection<Requirement> getModifiedRequirements(){
        Collection<Requirement> modifiedRequirements = new ArrayList<>();
        for(CustomRequirement customRequirement : customRequirements){
            modifiedRequirements.add((Requirement) customRequirement.getModified());
            if(customRequirement.isModified()){
                modified = true;
            }
        }
        return modifiedRequirements;
    }


    private Node createVictoryPointsToModify(Eligible originalCard){
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        pointsPart.getChildren().add(victoryPointsLabel);
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Math.min(1, originalCard.getVictoryPoints()), 12, originalCard.getVictoryPoints()));
        pointsPart.getChildren().add(modifiableVictoryPoints);
        return pointsPart;
    }

    protected Node createReqsAndPtsToModify(Eligible originalCard){
        HBox reqAndPts = new HBox();

        //pts
        reqAndPts.getChildren().add(createVictoryPointsToModify(originalCard));

        //reqs
        VBox requirementPart = new VBox();
        Label reqLabel = new Label("Requirements");
        requirementPart.getChildren().add(reqLabel);
        for(CustomRequirement customRequirement : customRequirements){
            requirementPart.getChildren().add(customRequirement.getNodeToModify());
        }
        reqAndPts.getChildren().add(requirementPart);
        return reqAndPts;
    }
}



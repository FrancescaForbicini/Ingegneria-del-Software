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

public abstract class CustomEligibleCard extends CustomClass {
    protected Node cardToModify;
    protected Spinner<Integer> modifiableVictoryPoints;
    protected ArrayList<CustomRequirement> customRequirements;

    protected void setCustomRequirements(Eligible originalCard, boolean toModify){
        customRequirements = new ArrayList<>();
        for(Requirement requirement : originalCard.getRequirements()){
            customRequirements.add(new CustomRequirement(requirement, toModify));
        }
    }
    private Node createVictoryPointsToModify(Eligible originalCard){
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        pointsPart.getChildren().add(victoryPointsLabel);
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalCard.getVictoryPoints()));
        pointsPart.getChildren().add(modifiableVictoryPoints);
        return pointsPart;
    }

    protected Node createRequirementsAndVictoryPoints(Eligible originalCard){
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

    protected int getModifiedVictoryPoints(Eligible originalCard) {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
        }
        return vpts;
    }
    protected Collection<Requirement> getModifiedRequirements(){
        Collection<Requirement> modifiedRequirements = new ArrayList<>();
        for(CustomRequirement customRequirement : customRequirements){
            modifiedRequirements.add((Requirement) customRequirement.getModified());
        }
        return modifiedRequirements;
    }
}



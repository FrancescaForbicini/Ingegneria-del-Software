package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAssignWhiteMarble extends CustomLeaderCard {
    private AssignWhiteMarble originalLeaderCard;
    private AssignWhiteMarble modifiedLeaderCard;

    public CustomAssignWhiteMarble(LeaderCard leaderCard){
        new CustomAssignWhiteMarble(leaderCard, false);
    }

    public CustomAssignWhiteMarble(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AssignWhiteMarble) leaderCard;
        }
        else {
            modifiedLeaderCard = (AssignWhiteMarble) leaderCard;
        }
        super.setCustomRequirements(leaderCard,toModify);
    }

    @Override
    public Node getNodeToShow(double height, double width) {
        VBox cardToShow = new VBox();

        cardToShow.getChildren().add(super.getNodeVictoryPointsToShow(modifiedLeaderCard));

        cardToShow.getChildren().add(super.getNodeRequirementsToShow());

        Label conversionLabel = new Label("White marbles converted into: " + modifiedLeaderCard.getResourceType());
        cardToShow.getChildren().add(conversionLabel);
        cardToShow.setPrefSize(height, width);

        return cardToShow;
    }


    @Override
    public Node getNodeToShow() {
        return getNodeToShow(PREF_HEIGHT, PREF_WIDTH);
    }

    @Override
    public Modifiable getModified() {
        int vpts = super.getModifiedVictoryPoints(originalLeaderCard);
        Collection<Requirement> requirementColors = super.getModifiedRequirements();
        if(super.isModified()){
            modified = true;
        }

        String path;
        if(modified){
            path = MODIFIED_PATH;
        } else {
            path = originalLeaderCard.getPath();
        }
        modifiedLeaderCard = new AssignWhiteMarble(vpts,originalLeaderCard.getResourceType(),requirementColors, path);
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
        Label leaderLabel = new Label("LeaderCard Assign White Marble ");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //req & pts
        parts.getChildren().add(super.createReqsAndPtsToModify(originalLeaderCard));

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAssignWhiteMarble extends CustomEligibleCard {
    private AssignWhiteMarble originalLeaderCard;
    private Map<DevelopmentColor,Map<Spinner<Integer>,Spinner<Integer>>> modifiableRequirements;
    private AssignWhiteMarble modifiedLeaderCard;


    public CustomAssignWhiteMarble(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AssignWhiteMarble) leaderCard;
            modifiableRequirements = new HashMap<>();
            for(Requirement requirement : originalLeaderCard.getRequirements()){
                RequirementColor requirementColor = (RequirementColor) requirement;
                modifiableRequirements.put(requirementColor.getColor(),new HashMap<>());
            }
        }
        else {
            modifiedLeaderCard = (AssignWhiteMarble) leaderCard;
        }
    }

    @Override
    public Node getModifiedNodeToShow() {
        VBox cardToShow = new VBox();

        Label vptsLabel = new Label("Victory Points: " + modifiedLeaderCard.getVictoryPoints());
        cardToShow.getChildren().add(vptsLabel);

        VBox reqsBox = new VBox();
        Label reqsLabel = new Label("Requires : ");
        reqsBox.getChildren().add(reqsLabel);
        for(Requirement requirement : modifiedLeaderCard.getRequirements()){
            RequirementColor requirementColor = (RequirementColor) requirement;
            HBox singleReq = new HBox();
            Label quantityLabel = new Label(requirementColor.getQuantity() + " Development Cards ");
            Rectangle reqColor = new Rectangle();
            reqColor.setFill(requirementColor.getColor().toPaint());
            String level;
            if(requirementColor.getLevel()==0){
                level = "any ";
            } else {
                level = requirementColor.getLevel() + " ";
            }
            Label lvlLabel = new Label("of level " + level);
            singleReq.getChildren().add(quantityLabel);
            singleReq.getChildren().add(reqColor);
            singleReq.getChildren().add(lvlLabel);
            reqsBox.getChildren().add(singleReq);
        }
        cardToShow.getChildren().add(reqsBox);

        Label conversionLabel = new Label("White marbles converted into: " + modifiedLeaderCard.getResourceType());
        cardToShow.getChildren().add(conversionLabel);
        return cardToShow;
    }


    @Override
    public Modifiable getModified() {
        boolean modified = false;
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalLeaderCard.getVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
            modified = true;
        }
        Collection<Requirement> requirementColors = new ArrayList<>();
        for(DevelopmentColor colorRequired : modifiableRequirements.keySet()) {
            int quantity = 1;
            int level = 1;
            for (Requirement requirement : originalLeaderCard.getRequirements()) {
                RequirementColor requirementColor = (RequirementColor) requirement;
                if(requirementColor.getColor().equals(colorRequired)) {
                    level = requirementColor.getLevel();
                    if (modifiableRequirements.get(colorRequired).getValue() != null) {
                        quantity = modifiableRequirements.get(colorRequired).getValue();
                        modified = true;
                    } else {
                        quantity = requirementColor.getQuantity();
                    }
                }
                RequirementColor modifiedRequirementColor = new RequirementColor(level, quantity, colorRequired);
                requirementColors.add(modifiedRequirementColor);
            }
        }
        String path;
        if(modified){
            path = null;
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
        parts.getChildren().add(super.createRequirementsAndVictoryPoints(originalLeaderCard));

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAssignWhiteMarble extends CustomLeaderCard{
    private AssignWhiteMarble originalLeaderCard;
    private Map<DevelopmentColor, Spinner<Integer>> modifiableRequirements;
    private AssignWhiteMarble modifiedLeaderCard;


    public CustomAssignWhiteMarble(LeaderCard leaderCard, boolean toModify) {
        if(toModify) {
            this.originalLeaderCard = (AssignWhiteMarble) leaderCard;
            modifiableRequirements = new HashMap<>();
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
    public Node getToModify() {
        createToModify();
        return cardToModify;
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
            int quantity;
            int level;
            for (Requirement requirement : originalLeaderCard.getRequirements()) {
                RequirementColor requirementColor = (RequirementColor) requirement;
                level = requirementColor.getLevel();
                if (modifiableRequirements.get(colorRequired).getValue() != null) {
                    quantity = modifiableRequirements.get(colorRequired).getValue();
                } else {
                    quantity = requirementColor.getQuantity();
                }
                RequirementColor modifiedRequirementColor = new RequirementColor(level, quantity, colorRequired);
                requirementColors.add(modifiedRequirementColor);
            }
        }
        modifiedLeaderCard = new AssignWhiteMarble(vpts,originalLeaderCard.getResourceType(),requirementColors, originalLeaderCard.getPath());
        return modifiedLeaderCard;
    }

    private void createToModify(){
        HBox modifiableCard = new HBox();
        VBox lines = new VBox();
        Label leaderLabel = new Label("LeaderCard Assign White Marble ");
        lines.getChildren().add(leaderLabel);
        HBox parts = new HBox();

        //pts
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
            Label levelLabel;
            if(requirementColor.getLevel()!=0) {
                levelLabel = new Label("Level " + requirementColor.getLevel() + " ");
            }else{
                levelLabel = new Label("Any");
            }
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, requirementColor.getQuantity()));
            modifiableRequirements.put(requirementColor.getColor(),actualCost);
            singleCardRequired.getChildren().add(colorLabel);
            singleCardRequired.getChildren().add(levelLabel);
            singleCardRequired.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleCardRequired);
        }
        parts.getChildren().add(requirementPart);

        lines.getChildren().add(parts);
        modifiableCard.getChildren().add(lines);
        cardToModify = modifiableCard;
    }
}

package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class CustomRequirement extends CustomClass{
    private Requirement originalRequirement;
    private Node requirementToModify;
    private Spinner<Integer> modifiableLevel = null;
    private Spinner<Integer> modifiableCardQuantity = null;
    private Spinner<Integer> modifiableResourceQuantity = null;
    private Requirement modifiedRequirement;
    private boolean isColor;


    public CustomRequirement(Requirement requirement){
        new CustomRequirement(requirement, false);
    }
    public CustomRequirement(Requirement requirement, boolean toModify){
        isColor = requirement.getClass().equals(RequirementColor.class);
        if(toModify){
            this.originalRequirement = requirement;
        } else {
            modifiedRequirement = requirement;
        }
    }

    @Override
    public Node getNodeToShow() {
        HBox singleReq = new HBox();
        if(isColor) {
            RequirementColor requirementColor = (RequirementColor) originalRequirement;
            Label quantityLabel = new Label(requirementColor.getQuantity() + " Development Cards ");
            singleReq.getChildren().add(quantityLabel);

            Rectangle reqColor = new Rectangle();
            reqColor.setFill(requirementColor.getColor().toPaint());
            singleReq.getChildren().add(reqColor);

            String level;
            if (requirementColor.getLevel() == 0) {
                level = "any ";
            } else {
                level = requirementColor.getLevel() + " ";
            }
            Label lvlLabel = new Label("of level " + level);
            singleReq.getChildren().add(lvlLabel);
        } else {
            RequirementResource requirementResource = (RequirementResource) originalRequirement;
            Label quantityLabel = new Label(requirementResource.getQuantity() + " of ");
            singleReq.getChildren().add(quantityLabel);

            Label resourceLabel = new Label(requirementResource.getResourceType() + " ");
            singleReq.getChildren().add(resourceLabel);
        }
        return singleReq;
    }

    @Override
    public Modifiable getModified() {
        if(isColor) {
            RequirementColor requirementColor = (RequirementColor) originalRequirement;
            int quantity;
            if(modifiableCardQuantity.getValue()==null){
                quantity = requirementColor.getLevel();
            } else {
                quantity = modifiableCardQuantity.getValue();
                if(quantity!=requirementColor.getQuantity()) {
                    modified = true;
                }
            }
            int level;
            if(modifiableLevel.getValue()==null){
                level = requirementColor.getLevel();
            } else {
                level = modifiableLevel.getValue();
                if(level!=requirementColor.getLevel()) {
                    modified = true;
                }
            }
            modifiedRequirement = new RequirementColor(level, quantity, requirementColor.getColor());
        }
        else {
            RequirementResource requirementResource = (RequirementResource) originalRequirement;
            int quantity;
            if(modifiableResourceQuantity.getValue()==null){
                quantity = requirementResource.getQuantity();
            } else {
                quantity = modifiableResourceQuantity.getValue();
                if(quantity!= requirementResource.getQuantity()) {
                    modified = true;
                }
            }
            modifiedRequirement = new RequirementResource(quantity,requirementResource.getResourceType());
        }
        return modifiedRequirement;
    }

    @Override
    public Node getNodeToModify() {
        createNodeToModify();
        return requirementToModify;
    }

    private void createNodeToModify() {
        HBox singleRequirement = new HBox();
        if(isColor) {
            RequirementColor requirementColor = (RequirementColor) originalRequirement;
            //quantity
            Spinner<Integer> actualQuantity = new Spinner<>();
            actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, requirementColor.getQuantity()));
            modifiableCardQuantity = actualQuantity;
            singleRequirement.getChildren().add(actualQuantity);
            //color
            Label colorLabel = new Label(" development cards " + requirementColor.getColor() + " which level is ");
            singleRequirement.getChildren().add(colorLabel);
            //level
            Spinner<Integer> actualLevel = new Spinner<>();
            actualLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, requirementColor.getLevel()));
            modifiableLevel = actualLevel;
            singleRequirement.getChildren().add(actualLevel);
            Label anylvlLabel = new Label(" (0 means any level)");
            singleRequirement.getChildren().add(anylvlLabel);
        }
        else {
            RequirementResource requirementResource = (RequirementResource) originalRequirement;
            //resource
            Label resourceTypeLabel = new Label(requirementResource.getResourceType().toString());
            singleRequirement.getChildren().add(resourceTypeLabel);
            //quantity
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, requirementResource.getQuantity()));
            modifiableResourceQuantity = actualCost;
            singleRequirement.getChildren().add(actualCost);
        }

        requirementToModify = singleRequirement;

    }

}

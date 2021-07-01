package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;

import java.util.HashMap;

public class CustomRequirement extends CustomClass{
    private Requirement originalRequirement;
    private Node requirementToModify;
    private Spinner<Integer> modifiableLevel = null;
    private Spinner<Integer> modifiableCardQuantity = null;
    private Spinner<Integer> modifiableResourceQuantity = null;
    private Requirement modifiedRequirement;
    private boolean isColor;


    public CustomRequirement(Requirement requirement, boolean toModify){
        isColor = requirement.getClass().equals(RequirementColor.class);
        if(toModify){
            this.originalRequirement = requirement;
        } else {
            modifiedRequirement = requirement;
        }
    }

    @Override
    public Node getModifiedNodeToShow() {
        return null;
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
            }
            int level;
            if(modifiableLevel.getValue()==null){
                level = requirementColor.getLevel();
            } else {
                level = modifiableLevel.getValue();
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

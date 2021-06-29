package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class CustomDevelopmentCard extends CustomClass {
    private DevelopmentCard originalDevelopmentCard;
    private ArrayList<RequirementResource> originalRequirements;
    private Node cardToModify;
    private DevelopmentColor color;
    private int level;
    private Spinner<Integer> victoryPoints;
    private Map<ResourceType,Spinner<Integer>> requirements;
    private Map<ResourceType,Spinner<Integer>> input;
    private Map<ResourceType,Spinner<Integer>> output;
    private Spinner<Integer> faithPoints;
    private String path;
    private DevelopmentCard modifiedDevelopmentCard;

    public CustomDevelopmentCard(DevelopmentCard developmentCard) {
        originalDevelopmentCard = developmentCard;
        originalRequirements = new ArrayList<>();
        for(Requirement requirement : originalDevelopmentCard.getRequirements()){
            RequirementResource requirementResource = (RequirementResource) requirement;
            originalRequirements.add(requirementResource);
        }


        victoryPoints = new Spinner<>();
        requirements = new HashMap<>();
        input = new HashMap<>();
        output = new HashMap<>();
        faithPoints = new Spinner<>();
        color = developmentCard.getColor();
        level = developmentCard.getLevel();
        path = developmentCard.getPath();

    }

    private void createCardToModify(){
        HBox singleCard = new HBox();
        VBox lines = new VBox();
        Label developmentLabel = new Label("Development Card " + originalDevelopmentCard.getColor() + " " + originalDevelopmentCard.getLevel());
        lines.getChildren().add(developmentLabel);
        HBox parts = new HBox();

        //pts
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        victoryPoints = new Spinner<>();
        victoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalDevelopmentCard.getVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(victoryPoints);
        parts.getChildren().add(pointsPart);

        //reqs
        VBox requirementPart = new VBox();
        Label reqLabel = new Label("Requirements");
        requirementPart.getChildren().add(reqLabel);
        for (Requirement requirement : originalDevelopmentCard.getRequirements()) {
            HBox singleResource = new HBox();
            RequirementResource requirementResource = (RequirementResource) requirement;
            Label resourceTypeLabel = new Label(requirementResource.getResourceType().toString());
            Spinner<Integer> actualCost = new Spinner<>();
            actualCost.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, requirementResource.getQuantity()));
            requirements.put(requirementResource.getResourceType(),actualCost);
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualCost);
            requirementPart.getChildren().add(singleResource);
        }
        parts.getChildren().add(requirementPart);

        //tr
        TradingRule tradingRule = originalDevelopmentCard.getTradingRule();
        HBox tr = new HBox();

        //in
        VBox inputBox = new VBox();
        Label inLabel = new Label("input");
        inputBox.getChildren().add(inLabel);
        for(ResourceType resourceType : tradingRule.getInput().keySet()){
            Label resourceTypeLabel = new Label(resourceType.toString());
            Spinner<Integer> actualQuantity = new Spinner<>();
            actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, tradingRule.getInput().get(resourceType)));
            input.put(resourceType,actualQuantity);
            HBox singleResource = new HBox();
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualQuantity);
            inputBox.getChildren().add(singleResource);
        }
        tr.getChildren().add(inputBox);

        //out
        VBox outputBox = new VBox();
        Label outLabel = new Label("output");
        outputBox.getChildren().add(outLabel);
        for(ResourceType resourceType : tradingRule.getOutput().keySet()){
            Label resourceTypeLabel = new Label(resourceType.toString());
            Spinner<Integer> actualQuantity = new Spinner<>();
            actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, tradingRule.getOutput().get(resourceType)));
            output.put(resourceType,actualQuantity);
            HBox singleResource = new HBox();
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualQuantity);
            outputBox.getChildren().add(singleResource);
        }
        if(tradingRule.getFaithPoints()>0){
            Label faithPtsLabel = new Label("FaithPoints");
            Spinner<Integer> actualFaithPoints = new Spinner<>();
            actualFaithPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, tradingRule.getFaithPoints()));
            faithPoints = actualFaithPoints;
            HBox singleResource = new HBox();
            singleResource.getChildren().add(faithPtsLabel);
            singleResource.getChildren().add(actualFaithPoints);
            outputBox.getChildren().add(singleResource);
        }
        tr.getChildren().add(outputBox);
        parts.getChildren().add(tr);
        lines.getChildren().add(parts);
        singleCard.getChildren().add(lines);
        cardToModify = singleCard;
    }

    public Node getToModify() {
        createCardToModify();
        return cardToModify;
    }
    @Override
    public Modifiable getModified(){
        return null;
    }
    public DevelopmentCard getModified1() {
        int vpts;
        if(victoryPoints.getValue()==null){
            vpts = originalDevelopmentCard.getVictoryPoints();
        } else {
            vpts = victoryPoints.getValue();
        }
        Collection<Requirement> requirementResources = new ArrayList<>();
        for(ResourceType resourceRequired : requirements.keySet()){
            int quantity = 1;
            if(requirements.get(resourceRequired).getValue() == null){
                for(RequirementResource requirementResource : originalRequirements){
                    if(requirementResource.getResourceType().equals(resourceRequired)){
                        quantity = requirementResource.getQuantity();

                    }
                }
            } else {
                quantity = requirements.get(resourceRequired).getValue();
            }
            RequirementResource requirementResource = new RequirementResource(quantity,resourceRequired);
            requirementResources.add(requirementResource);
        }
        Map<ResourceType,Integer> in = new HashMap<>();
        for(ResourceType resourceIn : input.keySet()){
            int quantity;
            if(input.get(resourceIn).getValue()==null){
                quantity = originalDevelopmentCard.getTradingRule().getInput().get(resourceIn);
            } else {
                quantity = input.get(resourceIn).getValue();
            }
            in.put(resourceIn,quantity);
        }
        Map<ResourceType,Integer> out = new HashMap<>();
        for(ResourceType resourceOut : output.keySet()){
            int quantity;
            if(output.get(resourceOut).getValue()==null){
                quantity = originalDevelopmentCard.getTradingRule().getOutput().get(resourceOut);
            } else {
                quantity = output.get(resourceOut).getValue();
            }
            out.put(resourceOut,quantity);
        }
        int fpts;
        if(faithPoints.getValue() == null){
            fpts = originalDevelopmentCard.getTradingRule().getFaithPoints();
        } else {
            fpts = faithPoints.getValue();
        }
        TradingRule tr = new TradingRule(in,out,fpts);
        modifiedDevelopmentCard = new DevelopmentCard(requirementResources, color,level,vpts,tr,path);
        return modifiedDevelopmentCard;
    }

    @Override
    public ImageView getModifiedImageView(){
        return new ImageView();
    }
}

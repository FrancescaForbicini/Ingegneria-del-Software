package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.ResourceType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class CustomTradingRule extends CustomClass{
    private TradingRule originalTradingRule;
    private Node tradingRuleToModify;
    private Map<ResourceType, Spinner<Integer>> modifiableInput;
    private Map<ResourceType, Spinner<Integer>> modifiableOutput;
    private Spinner<Integer> modifiableFaithPoints;
    private TradingRule modifiedTradingRule;

    public CustomTradingRule (TradingRule originalTradingRule, boolean toModify){
        if(toModify) {
            this.originalTradingRule = originalTradingRule;
            modifiableInput = new HashMap<>();
            modifiableOutput = new HashMap<>();
            modifiableFaithPoints = new Spinner<>();
        }
        else {
            modifiedTradingRule = originalTradingRule;
        }
    }
    @Override
    public Node getNodeToShow() {
        return null;
    }

    @Override
    public Node getNodeToModify() {
        createToModify();
        return tradingRuleToModify;
    }

    private void createToModify() {
        HBox tr = new HBox();

        //in
        VBox inputBox = new VBox();
        Label inLabel = new Label("input");
        inputBox.getChildren().add(inLabel);
        for (ResourceType resourceType : originalTradingRule.getInput().keySet()) {
            Label resourceTypeLabel = new Label(resourceType.toString());
            Spinner<Integer> actualQuantity = new Spinner<>();
            actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, originalTradingRule.getInput().get(resourceType)));
            modifiableInput.put(resourceType, actualQuantity);
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
        for (ResourceType resourceType : originalTradingRule.getOutput().keySet()) {
            Label resourceTypeLabel = new Label(resourceType.toString());
            Spinner<Integer> actualQuantity = new Spinner<>();
            actualQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, originalTradingRule.getOutput().get(resourceType)));
            modifiableOutput.put(resourceType, actualQuantity);
            HBox singleResource = new HBox();
            singleResource.getChildren().add(resourceTypeLabel);
            singleResource.getChildren().add(actualQuantity);
            outputBox.getChildren().add(singleResource);
        }
        Label faithPtsLabel = new Label("FaithPoints");
        Spinner<Integer> actualFaithPoints = new Spinner<>();
        actualFaithPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9, originalTradingRule.getFaithPoints()));
        modifiableFaithPoints = actualFaithPoints;
        HBox singleResource = new HBox();
        singleResource.getChildren().add(faithPtsLabel);
        singleResource.getChildren().add(actualFaithPoints);
        outputBox.getChildren().add(singleResource);

        tr.getChildren().add(outputBox);
        tradingRuleToModify = tr;
    }

    @Override
    public Modifiable getModified() {
        Map<ResourceType,Integer> in = new HashMap<>();
        for(ResourceType resourceIn : modifiableInput.keySet()){
            int quantity;
            if(modifiableInput.get(resourceIn).getValue()==null){
                quantity = originalTradingRule.getInput().get(resourceIn);
            } else {
                quantity = modifiableInput.get(resourceIn).getValue();
                if(quantity!=originalTradingRule.getInput().get(resourceIn)) {
                    modified = true;
                }
            }
            in.put(resourceIn,quantity);
        }
        Map<ResourceType,Integer> out = new HashMap<>();
        for(ResourceType resourceOut : modifiableOutput.keySet()){
            int quantity;
            if(modifiableOutput.get(resourceOut).getValue()==null){
                quantity = originalTradingRule.getOutput().get(resourceOut);
            } else {
                quantity = modifiableOutput.get(resourceOut).getValue();
                if(quantity!=originalTradingRule.getOutput().get(resourceOut)) {
                    modified = true;
                }
            }
            out.put(resourceOut,quantity);
        }
        int fpts;
        if(modifiableFaithPoints.getValue() == null){
            fpts = originalTradingRule.getFaithPoints();
        } else {
            fpts = modifiableFaithPoints.getValue();
            if(fpts!= originalTradingRule.getFaithPoints()) {
                modified = true;
            }
        }
        modifiedTradingRule = new TradingRule(in,out,fpts);
        return modifiedTradingRule;
    }
}

package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProductionDTO extends ActionMessageDTO {
    private  TradingRule tradingRuleChosen;
    private  ArrayList<ResourceType> inputAnyChosen;
    private  ArrayList<ResourceType> outputAnyChosen;
    private  Map<ResourceType,Integer> inputChosenFromWarehouse;
    private  Map<ResourceType,Integer> inputChosenFromStrongbox;

    public TradingRule getTradingRuleChosen() {
        return tradingRuleChosen;
    }

    public void setTradingRuleChosen(TradingRule tradingRuleChosen) {
        this.tradingRuleChosen = tradingRuleChosen;
    }

    public ArrayList<ResourceType> getInputAnyChosen() {
        return inputAnyChosen;
    }

    public void setInputAnyChosen(ArrayList<ResourceType> inputAnyChosen) {
        this.inputAnyChosen = inputAnyChosen;
    }

    public ArrayList<ResourceType> getOutputAnyChosen() {
        return outputAnyChosen;
    }

    public void setOutputAnyChosen(ArrayList<ResourceType> outputAnyChosen) {
        this.outputAnyChosen = outputAnyChosen;
    }

    public Map<ResourceType, Integer> getInputChosenFromWarehouse() {
        return inputChosenFromWarehouse;
    }

    public void setInputChosenFromWarehouse(Map<ResourceType, Integer> inputChosenFromWarehouse) {
        this.inputChosenFromWarehouse = inputChosenFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputChosenFromStrongbox() {
        return inputChosenFromStrongbox;
    }

    public void setInputChosenFromStrongbox(Map<ResourceType, Integer> inputChosenFromStrongbox) {
        this.inputChosenFromStrongbox = inputChosenFromStrongbox;
    }

    @Override
    public String getRelatedAction() {
        return "ActivateProduction";
    }
}

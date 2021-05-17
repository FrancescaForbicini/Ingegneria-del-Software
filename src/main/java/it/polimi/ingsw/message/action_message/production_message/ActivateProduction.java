package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProduction extends TurnActionMessageDTO {
    private static final long serialVersionUID = 1739486477341608203L;
    ArrayList<TradingRule> tradingRulesToChoose;
    ArrayList<ResourceType> inputAnyToChoose;
    ArrayList<ResourceType> outputAnyToChoose;
    Map<ResourceType,Integer> inputFromStrongBoxToChoose;
    Map<ResourceType,Integer> inputFromWarehouseToChoose;


    public ArrayList<TradingRule> getTradingRulesToChoose() {
        return tradingRulesToChoose;
    }

    public ArrayList<ResourceType> getInputAnyToChoose() {
        return inputAnyToChoose;
    }

    public ArrayList<ResourceType> getOutputAnyToChoose() {
        return outputAnyToChoose;
    }

    public Map<ResourceType, Integer> getInputFromStrongBoxToChoose() {
        return inputFromStrongBoxToChoose;
    }

    public Map<ResourceType, Integer> getInputFromWarehouseToChoose() {
        return inputFromWarehouseToChoose;
    }
}

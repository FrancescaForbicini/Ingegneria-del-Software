package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProductionDTO extends TurnActionMessageDTO {
    private final ArrayList<TradingRule> tradingRulesToChoose;
    private final ArrayList<ResourceType> inputAnyToChoose;
    private final ArrayList<ResourceType> outputAnyToChoose;
    private final Map<ResourceType,Integer> inputFromStrongBoxToChoose;
    private final Map<ResourceType,Integer> inputFromWarehouseToChoose;


    public ActivateProductionDTO(ArrayList<TradingRule> tradingRulesToChoose, ArrayList<ResourceType> inputAnyToChoose, ArrayList<ResourceType> outputAnyToChoose, Map<ResourceType, Integer> inputFromStrongBoxToChoose, Map<ResourceType, Integer> inputFromWarehouseToChoose) {
        this.tradingRulesToChoose = tradingRulesToChoose;
        this.inputAnyToChoose = inputAnyToChoose;
        this.outputAnyToChoose = outputAnyToChoose;
        this.inputFromStrongBoxToChoose = inputFromStrongBoxToChoose;
        this.inputFromWarehouseToChoose = inputFromWarehouseToChoose;
    }

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

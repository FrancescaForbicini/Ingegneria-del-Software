package it.polimi.ingsw.message.action_message.ProductionMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class InputFromWhere extends ActionMessage {
    Map<ResourceType,Integer> inputFromWarehouse;
    Map<ResourceType,Integer> inputFromStrongbox;

    public void setInputFromWarehouse(Map<ResourceType, Integer> inputFromWarehouse) {
        this.inputFromWarehouse = inputFromWarehouse;
    }

    public void setInputFromStrongbox(Map<ResourceType, Integer> inputFromStrongbox) {
        this.inputFromStrongbox = inputFromStrongbox;
    }

    public Map<ResourceType, Integer> getInputFromWarehouse() {
        return inputFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputFromStrongbox() {
        return inputFromStrongbox;
    }
}

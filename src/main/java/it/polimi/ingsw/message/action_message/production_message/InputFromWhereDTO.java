package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class InputFromWhereDTO extends TurnActionMessageDTO {
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

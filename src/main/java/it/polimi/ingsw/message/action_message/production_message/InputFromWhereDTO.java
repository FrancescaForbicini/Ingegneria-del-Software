package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class InputFromWhereDTO extends ActionMessageDTO {
    private final Map<ResourceType,Integer> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;

    public InputFromWhereDTO(Map<ResourceType, Integer> inputFromWarehouse, Map<ResourceType, Integer> inputFromStrongbox) {
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
    }

    public Map<ResourceType, Integer> getInputFromWarehouse() {
        return inputFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputFromStrongbox() {
        return inputFromStrongbox;
    }

    @Override
    public String getRelatedAction() {
        // TODO
        return null;
    }
}

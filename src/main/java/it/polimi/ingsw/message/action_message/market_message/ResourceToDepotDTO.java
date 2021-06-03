package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class ResourceToDepotDTO extends ActionMessageDTO {
    private final Map<ResourceType,Integer> resourceToDepot;

    public Map<ResourceType, Integer> getResourceToDepot() {
        return resourceToDepot;
    }

    public ResourceToDepotDTO(Map<ResourceType, Integer> resourceToDepot) {
        this.resourceToDepot = resourceToDepot;
    }

    @Override
    public String getRelatedAction() {
        return null;
    }
}

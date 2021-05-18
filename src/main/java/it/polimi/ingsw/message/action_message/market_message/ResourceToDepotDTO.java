package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class ResourceToDepotDTO extends TurnActionMessageDTO {
    Map<ResourceType,Integer> resourceToDepot;

    public void setResourceToDepot(Map<ResourceType, Integer> resourceToDepot) {
        this.resourceToDepot = resourceToDepot;
    }

    public Map<ResourceType, Integer> getResourceToDepot() {
        return resourceToDepot;
    }
}

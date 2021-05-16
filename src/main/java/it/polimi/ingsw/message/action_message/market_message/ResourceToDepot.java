package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class ResourceToDepot extends ActionMessageDTO {
    private static final long serialVersionUID = -4486163497428833637L;
    Map<ResourceType,Integer> resourceToDepot;

    public void setResourceToDepot(Map<ResourceType, Integer> resourceToDepot) {
        this.resourceToDepot = resourceToDepot;
    }

    public Map<ResourceType, Integer> getResourceToDepot() {
        return resourceToDepot;
    }
}

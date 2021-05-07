package it.polimi.ingsw.message.action_message.MarketMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class ResourceToDepot extends ActionMessage {
    Map<ResourceType,Integer> resourceToDepot;

    public void setResourceToDepot(Map<ResourceType, Integer> resourceToDepot) {
        this.resourceToDepot = resourceToDepot;
    }

    public Map<ResourceType, Integer> getResourceToDepot() {
        return resourceToDepot;
    }
}

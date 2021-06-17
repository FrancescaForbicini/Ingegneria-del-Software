package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class ResourcesChosen {
    private final Map<ResourceType,Integer> resourcesTakenFromWarehouse;
    private final Map<ResourceType,Integer> resourcesTakenFromStrongbox;

    public ResourcesChosen(Map<ResourceType, Integer> resourcesTakenFromWarehouse, Map<ResourceType, Integer> resourcesTakenFromStrongbox) {
        this.resourcesTakenFromWarehouse = resourcesTakenFromWarehouse;
        this.resourcesTakenFromStrongbox = resourcesTakenFromStrongbox;
    }

    public Map<ResourceType, Integer> getResourcesTakenFromWarehouse() {
        return resourcesTakenFromWarehouse;
    }


    public Map<ResourceType, Integer> getResourcesTakenFromStrongbox() {
        return resourcesTakenFromStrongbox;
    }


    public void addResourcesTakenFromMarket(ResourceType resourceType,int amount){
        this.resourcesTakenFromWarehouse.merge(resourceType,amount,Integer::sum);
    }

    public void addResourcesTakenFromStrongbox(ResourceType resourceType,int amount){
        this.resourcesTakenFromStrongbox.merge(resourceType,amount,Integer::sum);
    }
}

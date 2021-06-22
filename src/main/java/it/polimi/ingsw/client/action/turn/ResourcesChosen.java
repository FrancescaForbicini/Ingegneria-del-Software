package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourcesChosen {
    private final Map<ResourceType, Map<Integer, Integer>> resourcesTakenFromWarehouse;
    private final Map<ResourceType,Integer> resourcesTakenFromStrongbox;

    public ResourcesChosen(Map<ResourceType, Map<Integer, Integer>> resourcesTakenFromWarehouse, Map<ResourceType, Integer> resourcesTakenFromStrongbox) {
        this.resourcesTakenFromWarehouse = resourcesTakenFromWarehouse;
        this.resourcesTakenFromStrongbox = resourcesTakenFromStrongbox;
    }

    public Map<ResourceType, Map<Integer, Integer>> getResourcesTakenFromWarehouse() {
        return resourcesTakenFromWarehouse;
    }


    public Map<ResourceType, Integer> getResourcesTakenFromStrongbox() {
        return resourcesTakenFromStrongbox;
    }


    public void addResourcesTakenFromWarehouse(ResourceType resourceType, int depotID, int amount){
        if(amount != 0) {
            if (this.resourcesTakenFromWarehouse.containsKey(resourceType)) {
                this.resourcesTakenFromWarehouse.get(resourceType).merge(depotID, amount, Integer::sum);
            } else {
                this.resourcesTakenFromWarehouse.put(resourceType, new HashMap<>());
                this.resourcesTakenFromWarehouse.get(resourceType).merge(depotID, amount, Integer::sum);
            }
        }
    }

    public void addResourcesTakenFromStrongbox(ResourceType resourceType,int amount){
        if(amount != 0) {
            this.resourcesTakenFromStrongbox.merge(resourceType, amount, Integer::sum);
        }
    }
}

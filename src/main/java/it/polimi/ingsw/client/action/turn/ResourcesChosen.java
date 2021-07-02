package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Resources chosen from warehouse or strongbox
 */
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

    /**
     * Adds resources chosen from warehouse
     *
     * @param resourceType the type of resource to take
     * @param depotID the depot where to take the resource
     * @param amount the quantity of the resource to take from the depot
     */
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

    /**
     * Adds resources chosen from strongbox
     *
     * @param resourceType the type of resource to take
     * @param amount the quantity of the resource to take
     */
    public void addResourcesTakenFromStrongbox(ResourceType resourceType,int amount){
        if(amount != 0) {
            this.resourcesTakenFromStrongbox.merge(resourceType, amount, Integer::sum);
        }
    }
}

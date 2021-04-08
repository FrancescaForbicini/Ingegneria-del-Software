package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.ResourceType;

/**
 * Representation of storage for resources
 * In each depot different ResourceTypes can not coexist and the quantity of the current one is limited by the level of the depot
 */
public class WarehouseDepot {
    private final int level;
    private ResourceType resourceType;
    private int quantity = 0;
    private  final boolean additional;

    /**
     * Creates a new Depot
     * @param level the maximum available quantity of ResourceType
     * @param resourceType current type of resource int this depot
     */
    public WarehouseDepot(int level,ResourceType resourceType,boolean additional){
        this.level = level;
        this.resourceType = resourceType;
        this.additional = additional;
    }

    /**
     * Add given amount of Resource if there is enough space to contain it, otherwise throws an exception
     * @param type type of resource added, must be the same of the depot (except if the depot has type Any, in that case any other type is fine)
     * @param quantityAdded how much quantity to add, must be positive and smaller the the level of the depot
     */
    public boolean addResource(ResourceType type, int quantityAdded) {
        if (quantityAdded > 0 && !type.equals(ResourceType.Any) && (resourceType.equals(type) || resourceType.equals(ResourceType.Any)) && quantity + quantityAdded <= level) {
            quantity += quantityAdded;
            if (resourceType.equals(ResourceType.Any) && quantity!=0) {
                resourceType = type;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes requested amount of Resource stored in it if there is enough available, otherwise throws an exception
     * @param quantityRemoved how much quantity is requested to remove
     */
    public boolean removeResource(int quantityRemoved) {
        if (quantityRemoved > 0 && quantity - quantityRemoved >= 0) {
            quantity -= quantityRemoved;
            if (!additional && quantity == 0) {
                resourceType = ResourceType.Any;
            }
            return true;
        }
        return false;
    }


    public int getLevel() {
        return level;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAdditional() {
        return additional;
    }

    /**
     * Checks how many slots are still empty
     * @return number of free slots
     */
    public int getAvailableSpace(){
        return this.level-this.quantity;
    }

    /**
     * Checks if there is at least 1 slot available
     * @return true if there are available slots
     */
    public boolean isSpaceAvailable(){
        return isEmpty() || quantity<level;
    }

    /**
     * Checks if all the slots are available
     * @return true if the Depot is empty
     */
    public boolean isEmpty(){
        return quantity==0;
    }
}

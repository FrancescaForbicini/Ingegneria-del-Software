package it.polimi.ingsw.model.warehouse;


import it.polimi.ingsw.model.requirement.ResourceType;

/**
 * Representation of storage for resources
 * In each depot different ResourceTypes can not coexist and
 * the quantity of the current one is limited by the level of the depot
 */
public class WarehouseDepot {
    private final int depotID;
    private final int level;
    private ResourceType resourceType;
    private int quantity;
    private boolean additional;

    /**
     * Creates a new Depot
     *
     * @param level the maximum available quantity of ResourceType
     * @param resourceType current type of resource int this depot
     */
    public WarehouseDepot(ResourceType resourceType, int level, boolean additional, int depotID){
        this.depotID = depotID;
        this.level = level;
        this.resourceType = resourceType;
        this.additional = additional;
        this.quantity = 0;
    }

    /**
     * Checks if a resource can be added in this depot
     *
     * @param type type of resource to add
     * @param quantityAdded quantity of the resource to add
     * @return true iff the resource can be added
     */
    public boolean checkAddResource(ResourceType type, int quantityAdded) {
        if (quantityAdded == 0)
            return true;
        return quantityAdded > 0 && !type.equals(ResourceType.Any) && (resourceType.equals(type) || resourceType.equals(ResourceType.Any)) && quantity + quantityAdded <= level;
    }

    /**
     * Checks if it possibile to move a resource
     *
     * @param resourceToMove resource that has to be moved
     * @param quantityToMove quantity of the resource that has to be moved
     * @param adding true iff the resource has to be added,false if it has to be removed
     * @return true iff the resource can be added or removed
     */
    public boolean isPossibleToMoveResource(ResourceType resourceToMove, int quantityToMove, boolean adding){
        return (adding && isPossibleToAddResource(resourceToMove, quantityToMove)) ||
                (!adding && isPossibleToRemoveResource(resourceToMove, quantityToMove));
    }


    /**
     * Checks if the resource can be added
     *
     * @param resourceToAdd type of resource to add
     * @param quantityToAdd quantity of the resource to add
     * @return true iff the resource can be add
     */
    private boolean isPossibleToAddResource(ResourceType resourceToAdd, int quantityToAdd){
        return !this.isFull() && quantity+quantityToAdd <= level && (this.isEmpty() || resourceType.equals(resourceToAdd));
    }

    /**
     * Checks if the resource can be removed
     *
     * @param resourceToRemove type of the resource to remove
     * @param quantityToRemove quantity of the resource to remove
     * @return true iff the resource can be removed
     */
    private boolean isPossibleToRemoveResource(ResourceType resourceToRemove, int quantityToRemove){
        return !this.isEmpty() && quantity-quantityToRemove >= 0 && resourceType.equals(resourceToRemove);
    }
    /**
     * Add given amount of Resource if there is enough space to contain it
     *
     * @param type type of resource added, must be the same of the depot (except if the depot has type Any, in that case any other type is fine)
     * @param quantityAdded how much quantity to add, must be positive and smaller the the level of the depot
     * @return true if it has been possible to add to requested amount of given resource to the depot
     */
    public boolean addResource(ResourceType type, int quantityAdded) {
        if (checkAddResource(type,quantityAdded)) {
            quantity += quantityAdded;
            if (resourceType.equals(ResourceType.Any) && quantity != 0) {
                resourceType = type;
            }
            return true;
        }
        return false;
    }

    /**
     * Removes requested amount of Resource stored in it if there is enough available
     *
     * @param quantityRemoved how much quantity is requested to remove
     * @return true if it has been possible to remove the given amount of resource from the depot
     */
    public boolean removeResource(int quantityRemoved) {
        if(quantityRemoved == 0){
            return  true;
        }
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

    public void setAdditional (boolean additional){
        this.additional = additional;
    }

    public boolean isAdditional() {
        return additional;
    }

    public int getDepotID() {
        return depotID;
    }
    /**
     * Checks how many slots are still empty
     *
     * @return number of free slots
     */
    public int getAvailableSpace(){
        return this.level-this.quantity;
    }

    /**
     * Checks if there is no slot available
     *
     * @return true if there are not available slots
     */
    public boolean isFull(){
        return quantity==level;
    }

    /**
     * Checks if all the slots are available
     *
     * @return true if the Depot is empty
     */
    public boolean isEmpty(){
        return quantity==0;
    }

    /**
     * Prints the depot
     *
     * @return string to show the depot
     */
    @Override
    public String toString(){
        return "\nDepot " + depotID + (isAdditional() ? " (additional)" : "") + ": " +  quantity + " " + resourceType.convertColor();
    }

}

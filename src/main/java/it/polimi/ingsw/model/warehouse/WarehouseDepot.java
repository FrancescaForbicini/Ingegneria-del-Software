package it.polimi.ingsw.model.warehouse;


import it.polimi.ingsw.model.requirement.ResourceType;

/**
 * Representation of storage for resources
 * In each depot different ResourceTypes can not coexist and the quantity of the current one is limited by the level of the depot
 */
public class WarehouseDepot {
    private final int depotID;
    private final int level;
    private ResourceType resourceType;
    private int quantity;
    private boolean additional;

    /**
     * Creates a new Depot
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

    public boolean checkAddResource(ResourceType type, int quantityAdded) {
        if (quantityAdded == 0)
            return true;
        return quantityAdded > 0 && !type.equals(ResourceType.Any) && (resourceType.equals(type) || resourceType.equals(ResourceType.Any)) && quantity + quantityAdded <= level;
    }

    public boolean isPossibleToMoveResource(ResourceType resourceToMove, int quantityMoved, boolean adding){
        if(quantityMoved==0){
            return true;
        }
        return (adding && isPossibleToAddResource(resourceToMove, quantityMoved)) ||
                (!adding && isPossibleToRemoveResource(resourceToMove,quantityMoved));
    }

    private boolean isPossibleToAddResource(ResourceType resourceToAdd, int quantityAdded){
        return quantityAdded > 0 && !this.isFull() && quantity+quantityAdded <= level && (this.isEmpty() || resourceType.equals(resourceToAdd));
    }

    private boolean isPossibleToRemoveResource(ResourceType resourceToRemove, int quantityRemoved){
        return quantityRemoved > 0 && !this.isEmpty() && quantity-quantityRemoved >= 0 && resourceType.equals(resourceToRemove);
    }
    /**
     * Add given amount of Resource if there is enough space to contain it, otherwise throws an exception
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
     * Removes requested amount of Resource stored in it if there is enough available, otherwise throws an exception
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
     * @return number of free slots
     */
    public int getAvailableSpace(){
        return this.level-this.quantity;
    }

    /**
     * Checks if there is no slot available
     * @return true if there are not available slots
     */
    public boolean isFull(){
        return quantity==level;
    }

    /**
     * Checks if all the slots are available
     * @return true if the Depot is empty
     */
    public boolean isEmpty(){
        return quantity==0;
    }

    @Override
    public String toString(){
        return "\nDepot " + depotID + (isAdditional() ? " (additional)" : "") + ": " +  quantity + " " + resourceType.convertColor();
        //return "\nDepot " + (depotID) + " " + (isEmpty() ? "is empty" : "Resource:  " + resourceType + "Quantity: " + quantity + ((isAdditional()) ? "has an additional depot" : "" ));
    }

}

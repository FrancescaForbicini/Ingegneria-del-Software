package it.polimi.ingsw.model;

public class RequirementResource extends Requirement{
    private int quantity;
    private ResourceType resource;

    /**Initiliazes the Requirement Resource
     *
     * @param quantity: used to specify the quantity of the resource
     * @param resource: used to specify the type of the resource
     */
    public RequirementResource(int quantity, ResourceType resource) {
        this.quantity = quantity;
        this.resource = resource;
    }
    public int getQuantity(){
        return quantity;
    }

    public ResourceType getResource() {
        return resource;
    }

    /**Used to check if a player has the right resources
     *
     * @param player: used to specify the player
     * @return true if a player has the right resources, false if not.
     */
    @Override
    public boolean isSatisfied(Player player) {
        int count=0;
        count=player.getpersonalBoard().strongbox.get(resource);
        if (count>=quantity && count!=0)
            return true;
        for(WarehouseDepot warehouseDepot: player.getPersonalBoard().warehouse.getDepot()){
            if (warehouseDepot.getResourceType().equals(resource))
                count+=warehouseDepot.getQuantity();
                if ( count!=0 &&  count>=quantity)
                    return true;
        }
        return false;
    }
}

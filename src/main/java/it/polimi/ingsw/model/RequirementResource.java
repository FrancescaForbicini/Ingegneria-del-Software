package it.polimi.ingsw.model;

import it.polimi.ingsw.model.warehouse.Warehouse;

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

    /**
     *Used to check if a player has the right type of resources and the right quantity
     * @param player: used to specify the player
     * @return true if a player has the right resources, false if not.
     */
    @Override
    public boolean isSatisfied(Player player) {
        int count = 0;
        count = player.getpersonalBoard().strongbox.get(resource) + player.getpersonalBoard().warehouse.getQuantity(resource);
        if (count >= quantity && count != 0)
            return true;
        return false;
    }
}

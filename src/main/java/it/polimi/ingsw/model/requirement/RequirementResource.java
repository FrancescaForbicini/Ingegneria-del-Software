package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

public class RequirementResource extends Requirement {
    private ResourceType resourceType;
    private int quantity;

    /**
     *
     * @param quantity: used to specify the quantity of the resource
     * @param resourceType: used to specify the type of the resource
     */
    public RequirementResource(int quantity, ResourceType resourceType) {
        this.quantity = quantity;
        this.resourceType = resourceType;
    }

    public int getQuantity(){
        return quantity;
    }

    public ResourceType getResource() {
        return resourceType;
    }

    /**
     * Checks if a player has the right type of resources and the right quantity
     *
     */
    @Override
    public boolean isSatisfied(Player player) {
        int resourceAmount = player.getPersonalBoard().getResourceAmount(resourceType);
        if (player.isDiscount(resourceType))
            resourceAmount+= -player.applyDiscount(resourceType) ;
        return resourceAmount >= this.quantity;
    }

    @Override
    public String toString(){
        return "Requirements: Resource: " + resourceType.convertColor() + " Quantity : " + quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequirementResource that = (RequirementResource) o;

        if (quantity != that.quantity) return false;
        return resourceType == that.resourceType;
    }

}

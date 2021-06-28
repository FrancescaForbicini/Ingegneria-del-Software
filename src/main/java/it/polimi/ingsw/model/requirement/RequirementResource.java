package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Requirements of resources
 */
public class RequirementResource extends Requirement {
    private final ResourceType resourceType;
    private final int quantity;

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

    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Checks if a player has the right type of resources and the right quantity
     *
     * @param player used to check if the requirement is satisfied
     * @return true iff the player has the right amount of resources
     */
    @Override
    public boolean isSatisfied(Player player) {
        int resourceAmount = player.getPersonalBoard().getResourceQuantity(resourceType);
        if (player.hasDiscountForResource(resourceType))
            resourceAmount+= -player.applyDiscount(resourceType) ;
        return resourceAmount >= this.quantity;
    }

    /**
     * Prints in the requirements resources
     * @return the string to print
     */
    @Override
    public String toString(){
        return quantity + " " + resourceType.convertColor();
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

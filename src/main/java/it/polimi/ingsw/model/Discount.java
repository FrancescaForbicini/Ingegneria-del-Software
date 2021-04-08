package it.polimi.ingsw.model;

import java.util.Collection;


public class Discount extends LeaderCardStrategy{

    /**Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints : used to specify the victory points
     * @param resourceType: resource that can be discounted
     * @param requirement: resource needed to activate the ability
     */
    public Discount(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super(victoryPoints, resourceType, requirement);
    }
    /**Check if a player has the requirements to activate a discount
     *
     * @param player: used to specify the player that wants activate a card
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player)){
            player.addDiscount(resourcetype);
        }
    }
}

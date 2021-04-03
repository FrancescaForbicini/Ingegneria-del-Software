package it.polimi.ingsw.model;

import java.util.Collection;

public class Discount extends LeaderCardStrategy{

    /**Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints : used to specify the victory points
     * @param resourceType : used to specify the resources that are needed to activate the card
     * @param requirement: used to verify if a player has the right resources to use the card
     */
    public Discount(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super(victoryPoints, resourceType, requirement);
    }

    @Override
    public boolean activate() {
        //TODO Player
        return super.activate();
    }
}

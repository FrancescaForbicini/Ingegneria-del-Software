package it.polimi.ingsw.model;

import java.util.Collection;

public abstract class LeaderCardStrategy extends Eligible{
    private int victoryPoints;
    private ResourceType resourceType;

    /**Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints : used to specify the victory points
     * @param resourceType : used to specify the resources that are needed to activate the card
     * @param requirement : used to specify the requirements to activate or buy a card
     */
    public LeaderCardStrategy(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super((Collection<Requirement>) requirement);
        this.victoryPoints = victoryPoints;
        this.resourceType = resourceType;
    }
    public int getVictoryPoints(){
        return victoryPoints;
    }
    public ResourceType getResourceType() {

        return resourceType;
    }
    public boolean activate(){
        // TODO Player
        return false;
    }
}

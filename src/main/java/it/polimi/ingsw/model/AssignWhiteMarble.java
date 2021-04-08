package it.polimi.ingsw.model;

import java.util.Collection;

public class AssignWhiteMarble extends LeaderCardStrategy{
    /**Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints : used to specify the victory points
     * @param resourceType : used to specify the resources that are needed to activate the card
     * @param requirement: used to verify if a player has the right resources to use the card
     */

    public AssignWhiteMarble(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super(victoryPoints, resourceType, requirement);
    }
    /**Check if a player has the requirements to activate the ability to set a white marble in a different color
     *
     * @param player: used to specify the player that wants activate a card
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player)){
            player.addWhiteMarble(resourcetype);
        }
    }


}

package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

/**
 * Removes resources from strongbox or warehouse
 */
public interface RemoveResources {

    /**
     * Removes resources from warehouse or strongbox
     *
     * @param resourceToTake resources to remove
     * @param player player that has to remove the resources
     * @param inputFromWarehouse resources to take from the warehouse
     * @param inputFromStrongbox resources to take from strongbox
     */
    static void removeResources(ResourceType resourceToTake, Player player, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        if(inputFromStrongbox.containsKey(resourceToTake)) {
            player.getPersonalBoard().removeResourceFromStrongbox(resourceToTake, inputFromStrongbox.get(resourceToTake));
        }
        if(inputFromWarehouse.containsKey(resourceToTake)){
            for(Integer depotID : inputFromWarehouse.get(resourceToTake).keySet()){
                player.getPersonalBoard().removeResourceFromWarehouse(resourceToTake, inputFromWarehouse.get(resourceToTake).get(depotID), depotID);
            }
        }
    }

    /**
     * Checks if the the total amount of resources can be removed
     *
     * @param player player that has to remove the resources
     * @return true iff the player has the correct amount that has to be removed
     */
    boolean isUserInputCorrect(Player player);

    /**
     * Checks if the depots from where to take the resources are correct
     *
     * @param player player that has to remove the resources
     * @param inputResource resource to remove
     * @param inputFromWarehouse depots where to take the resource
     * @return true iff the depots have the rights amount to be removed
     */
    static boolean areDepotsRight(Player player, ResourceType inputResource, Map<ResourceType,Map<Integer, Integer>> inputFromWarehouse) {
        int totalQuantityToRemoveFromWarehouse = 0;
        for (Integer depotID : inputFromWarehouse.get(inputResource).keySet()) {
            if(inputFromWarehouse.containsKey(inputResource)) {
                int quantityToRemove = inputFromWarehouse.get(inputResource).get(depotID);
                if (!player.getWarehouse().canMoveResource(inputResource, quantityToRemove, depotID, false)) {
                    //cannot remove this quantity of this resource from this depot
                    return false;
                }
                totalQuantityToRemoveFromWarehouse += quantityToRemove;
            }
        }
        return player.getPersonalBoard().getResourceQuantityFromWarehouse(inputResource) >= totalQuantityToRemoveFromWarehouse;
    }

    /**
     * Checks if the player has chosen the right place where to take the resources
     *
     * @param inputResource  resource to take
     * @param player player that has to remove the resource
     * @param inputFromWarehouse resources available in the warehouse
     * @param inputFromStrongbox resources available in the strongbox
     * @return true iff the the player has chosen the right place
     */
    static boolean arePlacesRight(ResourceType inputResource, Player player, Map<ResourceType, Map<Integer, Integer>> inputFromWarehouse, Map<ResourceType, Integer> inputFromStrongbox) {
        if (!inputFromStrongbox.containsKey(inputResource) && !inputFromWarehouse.containsKey(inputResource)) {
            return false;
        }
        if (inputFromStrongbox.containsKey(inputResource) &&
                player.getPersonalBoard().getResourceQuantityFromStrongbox(inputResource) < inputFromStrongbox.get(inputResource)) {
            //player has not enough resources in strongbox
            return false;
        }
        if (inputFromWarehouse.containsKey(inputResource)) {
            //there's not enough resources in depots or the depots from which take resources are wrong
            return RemoveResources.areDepotsRight(player, inputResource, inputFromWarehouse);
        }
        return true;
    }

    /**
     * Checks if the the player has the right amount of resource
     *
     * @param inputResource resource to take
     * @param totalAmountToTake amount of the resource to take
     * @param inputFromWarehouse amount of the resource available in the warehouse
     * @param inputFromStrongbox amount of the resource available in the strongbox
     * @return iff there is the right amount
     */
    static boolean isInputQuantityRight(ResourceType inputResource, int totalAmountToTake, Map<ResourceType, Map<Integer, Integer>> inputFromWarehouse, Map<ResourceType, Integer> inputFromStrongbox){
        int totalInputProposed = 0;
        if(inputFromWarehouse.containsKey(inputResource)) {
            for (Integer depotID : inputFromWarehouse.get(inputResource).keySet()) {
                totalInputProposed += inputFromWarehouse.get(inputResource).get(depotID);
            }
        }
        if(inputFromStrongbox.containsKey(inputResource)) {
            totalInputProposed += inputFromStrongbox.get(inputResource);
        }
        return totalInputProposed == totalAmountToTake;
    }
}

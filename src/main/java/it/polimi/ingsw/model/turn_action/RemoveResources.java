package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public interface RemoveResources {
    static  void removeResources(ResourceType resourceToTake, Player player, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        if(inputFromStrongbox.containsKey(resourceToTake)){
            try {
                player.getPersonalBoard().removeResourceFromStrongbox(resourceToTake, inputFromStrongbox.get(resourceToTake));
            } catch (NotEnoughResourcesException e) {
                e.printStackTrace();
            }
        }
        if(inputFromWarehouse.containsKey(resourceToTake)){
            for(Integer depotID : inputFromWarehouse.get(resourceToTake).keySet()){
                player.getPersonalBoard().removeResourceFromWarehouse(resourceToTake, inputFromWarehouse.get(resourceToTake).get(depotID), depotID);
            }
        }
    }

    boolean isUserInputCorrect(Player player);

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

    static boolean removeResources(int totalAmountToRemove, ResourceType resourceToRemove, Player player, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        return false;




        /* old version
        int quantityStillToRemove = 0;
        if (inputFromWarehouse.containsKey(resourceToRemove) ){
            for(Integer depotID : inputFromWarehouse.get(resourceToRemove).keySet()){
                quantityStillToRemove += inputFromWarehouse.get(resourceToRemove).get(depotID);
            }
        }
        if (inputFromStrongbox.containsKey(resourceToRemove))
            quantityStillToRemove += inputFromStrongbox.get(resourceToRemove);
        if (quantityStillToRemove == totalAmountToRemove) {
            //user tries to remove the required quantity of resources
            if (inputFromWarehouse.containsKey(resourceToRemove)) {
                //user wants to remove some of this resource from warehouse
                for (Integer depotID : inputFromWarehouse.get(resourceToRemove).keySet()) {
                    if(!player.getWarehouse().removeResource(inputFromWarehouse.get(resourceToRemove).get(depotID), depotID)){
                        return false;
                    }
                    quantityStillToRemove -= inputFromWarehouse.get(resourceToRemove).get(depotID);
                }
            }
            if (inputFromStrongbox.containsKey(resourceToRemove)) {
                //user wants to remove some of this resource from strongbox
                player.getStrongbox().replace(resourceToRemove, player.getStrongbox().get(resourceToRemove), player.getStrongbox().get(resourceToRemove) - inputFromStrongbox.get(resourceToRemove));
            }
            return true;
        }
        return false;

         */
    }
}

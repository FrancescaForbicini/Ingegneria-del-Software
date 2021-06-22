package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public interface RemoveResources {
    static boolean removeResources(int totalAmountToRemove, ResourceType resourceToRemove, Player player, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        int quantityToRemoveSpecifiedByUser = 0;
        //TODO complete review
        if (inputFromWarehouse.containsKey(resourceToRemove) ){
            for(Integer depotID : inputFromWarehouse.get(resourceToRemove).keySet()){
                quantityToRemoveSpecifiedByUser += inputFromWarehouse.get(resourceToRemove).get(depotID);
            }
            //quantityToRemoveSpecifiedByUser = inputFromWarehouse.get(resourceToRemove);
        }
        if (inputFromStrongbox.containsKey(resourceToRemove))
            quantityToRemoveSpecifiedByUser += inputFromStrongbox.get(resourceToRemove);
        if (quantityToRemoveSpecifiedByUser < totalAmountToRemove) //TODO maybe check != instead of <
            //user tries to remove less resources than required
            return false;
        if (inputFromWarehouse.containsKey(resourceToRemove)) {
            //user wants to remove some of this resource from warehouse
            for(Integer depotID : inputFromWarehouse.get(resourceToRemove).keySet()) {
                player.getWarehouse().removeResource(inputFromWarehouse.get(resourceToRemove).get(depotID), depotID);
                quantityToRemoveSpecifiedByUser -= inputFromWarehouse.get(resourceToRemove).get(depotID);
            }
            /*if (inputFromWarehouse.get(resourceToRemove) < totalAmountToRemove) {
                player.getWarehouse().removeResource(inputFromWarehouse.get(resourceToRemove), player.getWarehouse().findDepotsByType(resourceToRemove).getDepotID());
                quantityToRemoveSpecifiedByUser -= inputFromWarehouse.get(resourceToRemove);
                player.getStrongbox().put(resourceToRemove, player.getStrongbox().get(resourceToRemove) - quantityToRemoveSpecifiedByUser);
              } else//it seems to do the same thing done in the if-branch
                    player.getWarehouse().removeResource(totalAmountToRemove, player.getWarehouse().findDepotsByType(resourceToRemove).getDepotID());
             */

        }else
        if (inputFromStrongbox.containsKey(resourceToRemove)) {
            //user wants to remove some of this resource from strongbox
            if (inputFromStrongbox.get(resourceToRemove) >= totalAmountToRemove)
                player.getStrongbox().put(resourceToRemove, player.getStrongbox().get(resourceToRemove) - inputFromStrongbox.get(resourceToRemove));
            else
                return false;
        }
        else
            return false;
        return true;
    }
}

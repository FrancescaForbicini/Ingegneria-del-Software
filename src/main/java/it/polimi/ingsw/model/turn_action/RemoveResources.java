package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public interface RemoveResources {
    static boolean removeResources(int totalAmountToRemove, ResourceType resourceToRemove, Player player, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
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
    }
}

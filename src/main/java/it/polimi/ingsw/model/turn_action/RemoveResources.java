package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public interface RemoveResources {
    static boolean removeResources(int amount, ResourceType resourceType, Player player, Map<ResourceType,Integer> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        int quantity = 0;
        if (inputFromWarehouse.containsKey(resourceType) ){
            quantity = inputFromWarehouse.get(resourceType);
        }
        if (inputFromStrongbox.containsKey(resourceType))
            quantity += inputFromStrongbox.get(resourceType);
        if (quantity < amount)
            return false;
        if (inputFromWarehouse.containsKey(resourceType)) {
            if (inputFromWarehouse.get(resourceType) < amount) {
                player.getWarehouse().removeResource(inputFromWarehouse.get(resourceType), player.getWarehouse().findDepotsByType(resourceType).getDepotID());
                player.getStrongbox().replace(resourceType, inputFromStrongbox.get(resourceType),amount - inputFromWarehouse.get(resourceType));
            } else
                player.getWarehouse().removeResource(amount, player.getWarehouse().findDepotsByType(resourceType).getDepotID());
        }else
        if (inputFromStrongbox.containsKey(resourceType)) {
            if (inputFromStrongbox.get(resourceType) >= amount)
                player.getStrongbox().replace(resourceType,inputFromStrongbox.get(resourceType), amount - inputFromStrongbox.get(resourceType));
            else
                return false;
        }
        else
            return false;
        return true;
    }
}

package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.View;

import java.util.HashMap;

public interface Remove {

    static Player clone(Player player ){
        Player playerClone = new Player(player.getUsername());
        for (WarehouseDepot warehouseDepot: player.getWarehouse().getAllDepots()) {
            if (warehouseDepot.isAdditional()) {
                WarehouseDepot depot = new WarehouseDepot(warehouseDepot.getResourceType(),warehouseDepot.getLevel(),warehouseDepot.isAdditional(),warehouseDepot.getDepotID());
                depot.addResource(warehouseDepot.getResourceType(),warehouseDepot.getQuantity());
                playerClone.getWarehouse().addAdditionalDepot(depot.getResourceType(), depot.getQuantity());
            }
            else{
                playerClone.getWarehouse().addResource(warehouseDepot.getResourceType(),warehouseDepot.getQuantity(),warehouseDepot.getDepotID());
            }
        }
        for (ResourceType resourceType: player.getStrongbox().keySet()){
            playerClone.getStrongbox().put(resourceType,player.getStrongbox().get(resourceType));
        }
        return playerClone;
    }
    static void inputFrom(View view, ResourcesChosen resourcesChosen, ResourceType resourceType, Player playerClone, int amountResourceToTake){
       int quantityStrongbox;
       int quantityWarehouse;
       quantityWarehouse = playerClone.getWarehouse().getQuantity(resourceType);
       quantityStrongbox = playerClone.getStrongbox().get(resourceType);
       if (amountResourceToTake != 0) {
            if (quantityStrongbox == 0) {
                playerClone.getWarehouse().removeResource(amountResourceToTake, playerClone.getWarehouse().findDepotsByType(resourceType).getDepotID());
                resourcesChosen.addResourcesTakenFromWarehouse(resourceType,amountResourceToTake);
            }
            else if (quantityWarehouse == 0) {
                playerClone.getStrongbox().replace(resourceType, quantityStrongbox, quantityStrongbox - amountResourceToTake);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceType,amountResourceToTake);
            }
            else if (quantityWarehouse + quantityStrongbox == amountResourceToTake) {
                playerClone.getWarehouse().removeResource(amountResourceToTake - quantityStrongbox,playerClone.getWarehouse().findDepotsByType(resourceType).getDepotID());
                playerClone.getStrongbox().replace(resourceType,quantityStrongbox,quantityStrongbox - amountResourceToTake - quantityWarehouse);
                resourcesChosen.addResourcesTakenFromWarehouse(resourceType,amountResourceToTake - quantityStrongbox );
                resourcesChosen.addResourcesTakenFromStrongbox(resourceType,quantityStrongbox);
            }
            else
            if (quantityWarehouse + quantityStrongbox > amountResourceToTake) {
                int quantityTakenFromStrongbox;
                int quantityTakenFromWarehouse;
                ResourcesChosen resourceChosenClient = new ResourcesChosen(new HashMap<>(),new HashMap<>());
                do {
                    resourceChosenClient = view.inputFrom(resourceType, quantityStrongbox, quantityWarehouse);
                    quantityTakenFromWarehouse = 0;
                    quantityTakenFromStrongbox = 0;
                    if (resourceChosenClient.getResourcesTakenFromWarehouse().containsKey(resourceType))
                        quantityTakenFromWarehouse = resourceChosenClient.getResourcesTakenFromWarehouse().get(resourceType);
                    if (resourceChosenClient.getResourcesTakenFromStrongbox().containsKey(resourceType))
                        quantityTakenFromStrongbox = resourceChosenClient.getResourcesTakenFromStrongbox().get(resourceType);
                }while (quantityTakenFromStrongbox > quantityStrongbox || quantityTakenFromWarehouse > quantityWarehouse || quantityTakenFromWarehouse + quantityTakenFromStrongbox != amountResourceToTake);
                if (quantityTakenFromWarehouse > 0) {
                    playerClone.getWarehouse().removeResource(quantityTakenFromWarehouse, playerClone.getWarehouse().findDepotsByType(resourceType).getDepotID());
                    resourcesChosen.addResourcesTakenFromWarehouse(resourceType,resourceChosenClient.getResourcesTakenFromWarehouse().get(resourceType));
                }
                if (quantityTakenFromStrongbox > 0){
                    playerClone.getStrongbox().replace(resourceType,playerClone.getStrongbox().get(resourceType),playerClone.getStrongbox().get(resourceType) - quantityTakenFromStrongbox);
                    resourcesChosen.addResourcesTakenFromStrongbox(resourceType,resourceChosenClient.getResourcesTakenFromStrongbox().get(resourceType));
                }
            }
        }
    }
}

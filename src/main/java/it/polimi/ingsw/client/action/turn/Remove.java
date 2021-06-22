package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public interface Remove {

    static Player clone(Player player ){
        Player playerClone = new Player(player.getUsername());
        playerClone.loadFromSettings();
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
    static void inputFrom(View view, ResourcesChosen resourcesChosen, ResourceType resourceToChoose, Player playerClone, int amountResourceToTake) {
        int quantityStrongbox;
        int quantityWarehouse;
        quantityWarehouse = playerClone.getWarehouse().getQuantity(resourceToChoose);
        quantityStrongbox = playerClone.getStrongbox().get(resourceToChoose);
        if (amountResourceToTake != 0) {
            if (quantityStrongbox == 0) {
                //resources are taken from warehouse
                takeOnlyFromWarehouse(view, playerClone, resourceToChoose, amountResourceToTake, resourcesChosen);
            } else if (quantityWarehouse == 0) {
                //resources are taken from strongbox
                playerClone.getStrongbox().replace(resourceToChoose, quantityStrongbox, quantityStrongbox - amountResourceToTake);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToChoose, amountResourceToTake);
            } else if (quantityWarehouse + quantityStrongbox == amountResourceToTake) {
                //player has no choice
                ArrayList<WarehouseDepot> possibleDepots = playerClone.getWarehouse().getPossibleDepotsToMoveResources(resourceToChoose, amountResourceToTake, false);
                for (WarehouseDepot possibleDepot : possibleDepots) {
                    resourcesChosen.addResourcesTakenFromWarehouse(resourceToChoose, possibleDepot.getDepotID(), possibleDepot.getQuantity());
                    playerClone.getWarehouse().removeResource(possibleDepot.getQuantity(), possibleDepot.getDepotID());
                }
                playerClone.getStrongbox().replace(resourceToChoose, quantityStrongbox, 0);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToChoose, quantityStrongbox);
            } else if (quantityWarehouse + quantityStrongbox > amountResourceToTake) {
                //player choices are needed
                int quantityTakenFromStrongbox;
                quantityTakenFromStrongbox = view.chooseQuantity(quantityStrongbox);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToChoose, quantityTakenFromStrongbox);
                amountResourceToTake -= quantityTakenFromStrongbox;
                takeOnlyFromWarehouse(view, playerClone, resourceToChoose, amountResourceToTake, resourcesChosen);
            }
        }
    }

    static private void takeOnlyFromWarehouse(View view, Player playerClone, ResourceType resourceToChoose, int amountResourceToTake, ResourcesChosen resourcesChosen){
        ArrayList<WarehouseDepot> possibleDepots = playerClone.getWarehouse().getPossibleDepotsToMoveResources(resourceToChoose, amountResourceToTake, false);
        if(possibleDepots.size() == 1){
            playerClone.getWarehouse().removeResource(amountResourceToTake, possibleDepots.get(0).getDepotID());
            resourcesChosen.addResourcesTakenFromWarehouse(resourceToChoose, possibleDepots.get(0).getDepotID(), amountResourceToTake);
        } else {
            int amountAlreadyTaken = 0;
            int amountFromSpecifiedDepot;
            WarehouseDepot specifiedDepot;
            do {
                specifiedDepot = possibleDepots.get(view.chooseDepot(possibleDepots));
                amountFromSpecifiedDepot = view.chooseQuantity(specifiedDepot.getQuantity());
                playerClone.getWarehouse().removeResource(amountFromSpecifiedDepot, specifiedDepot.getDepotID());
                resourcesChosen.addResourcesTakenFromWarehouse(resourceToChoose, specifiedDepot.getDepotID(), amountFromSpecifiedDepot);
                amountAlreadyTaken += amountFromSpecifiedDepot;
            } while (amountAlreadyTaken < amountResourceToTake);
        }
    }
}

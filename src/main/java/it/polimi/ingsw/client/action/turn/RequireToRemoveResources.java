package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

/**
 * Removes resources from strongbox or warehouse
 */
public interface RequireToRemoveResources {

    /**
     * Clones the player
     * @param player the player to copy
     * @return the copy of the player
     */
    static Player clone(Player player ){
        Player playerClone = new Player(player.getUsername());
        playerClone.loadFromSettings();
        for (WarehouseDepot warehouseDepot: player.getWarehouse().getAllDepots()) {
            if (warehouseDepot.isAdditional()) {
                playerClone.getWarehouse().addAdditionalDepot(warehouseDepot.getResourceType(), warehouseDepot.getLevel());
                playerClone.getWarehouse().getDepot(warehouseDepot.getDepotID()).get().addResource(warehouseDepot.getResourceType(),warehouseDepot.getQuantity());
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

    /**
     * Remove an amount of resources from a playerClone's storages
     * For each Resource (if not automatically handled) will be asked to the user from which storage take it (Warehouse or Strongbox)
     * Each Resource removed will be added to ResourcesChosen attribute to send it to the server
     * @param view
     * @param resourcesChosen the resources already chosen to take from the warehouse or strongbox
     * @param resourceToTake the resource to take from warehouse or strongbox
     * @param playerClone the copy of the player
     * @param amountResourceToTake the quantity of the resource to take
     */
    static void removeResourceFromPlayerClone(View view, ResourcesChosen resourcesChosen, ResourceType resourceToTake, Player playerClone, int amountResourceToTake) {
        int quantityStrongbox;
        int quantityWarehouse;
        quantityWarehouse = playerClone.getWarehouse().getQuantity(resourceToTake);
        quantityStrongbox = playerClone.getStrongbox().get(resourceToTake);
        if (amountResourceToTake != 0) {
            if (quantityStrongbox == 0) {
                //resources are taken from warehouse
                takeOnlyFromWarehouse(view, playerClone, resourceToTake, amountResourceToTake, resourcesChosen);
            } else if (quantityWarehouse == 0) {
                //resources are taken from strongbox
                playerClone.getStrongbox().replace(resourceToTake, quantityStrongbox, quantityStrongbox - amountResourceToTake);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToTake, amountResourceToTake);
            } else if (quantityWarehouse + quantityStrongbox == amountResourceToTake) {
                //player has no choice
                ArrayList<WarehouseDepot> possibleDepots = playerClone.getWarehouse().getPossibleDepotsToMoveResources(resourceToTake, false);
                for (WarehouseDepot possibleDepot : possibleDepots) {
                    resourcesChosen.addResourcesTakenFromWarehouse(resourceToTake, possibleDepot.getDepotID(), possibleDepot.getQuantity());
                    playerClone.getWarehouse().removeResource(possibleDepot.getQuantity(), possibleDepot.getDepotID());
                }
                playerClone.getStrongbox().replace(resourceToTake, quantityStrongbox, 0);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToTake, quantityStrongbox);
            } else if (quantityWarehouse + quantityStrongbox > amountResourceToTake) {
                //player choices are needed
                int quantityTakenFromStrongbox;
                view.showMessage("Choose the quantity of " + resourcesChosen.toString() + " from the strongbox: ");
                quantityTakenFromStrongbox = view.chooseQuantity(quantityStrongbox);
                resourcesChosen.addResourcesTakenFromStrongbox(resourceToTake, quantityTakenFromStrongbox);
                amountResourceToTake -= quantityTakenFromStrongbox;
                takeOnlyFromWarehouse(view, playerClone, resourceToTake, amountResourceToTake, resourcesChosen);
            }
        }
    }

    /**
     * Takes only from warehouse if the player can not take the resource from the warehouse
     * @param view
     * @param playerClone the copy of the player
     * @param resourceToTake the resource to take from warehouse
     * @param amountResourceToTake the quantity of the resource to take from warehouse
     * @param resourcesChosen the resources already chosen to take from warehouse or strongbox
     */
    static private void takeOnlyFromWarehouse(View view, Player playerClone, ResourceType resourceToTake, int amountResourceToTake, ResourcesChosen resourcesChosen){
        int amountRemoved;
        int chosenDepotID;
        do {
            ArrayList<WarehouseDepot> possibleDepots = playerClone.getWarehouse().getPossibleDepotsToMoveResources(resourceToTake, false);
            if (possibleDepots.size() == 0) {
                return;
            }
            if (possibleDepots.size() == 1) {
                amountRemoved = amountResourceToTake;
                chosenDepotID = possibleDepots.get(0).getDepotID();
                view.showMessage(amountRemoved + " " + resourceToTake + " can only be taken from depot " + chosenDepotID + " so ");
            } else {
                amountRemoved = 1;
                view.showMessage("Choose from which depot sell 1 " + resourceToTake);
                chosenDepotID = possibleDepots.get(view.chooseDepot(possibleDepots)).getDepotID();
            }
            view.showMessage(amountRemoved + " " + resourceToTake + " will be taken from depot " + chosenDepotID + "\n");
            playerClone.getWarehouse().removeResource(amountRemoved, chosenDepotID);
            resourcesChosen.addResourcesTakenFromWarehouse(resourceToTake, chosenDepotID, amountRemoved);
            amountResourceToTake -= amountRemoved;
        }while(amountResourceToTake>0);
    }
}

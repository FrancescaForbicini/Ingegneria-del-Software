package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.Map;

public class SortWarehouse implements TurnAction {
    private Map<ResourceType,Integer> sortWarehouse;
    private boolean isSorted;
    public SortWarehouse(Map<ResourceType,Integer> sortWarehouse){
        this.sortWarehouse = sortWarehouse;
        this.isSorted = false;
    }

    @Override
    public void play(Player player) {
        int newDepot;
        int oldDepot;
        int newQuantity;
        int oldQuantity;
        ResourceType oldResourceType;
        WarehouseDepot warehouseDepot;
        if(sortWarehouse == null) {
            isSorted = true;
        }
        else{
            for (ResourceType newResourceType: sortWarehouse.keySet()){
                newDepot = sortWarehouse.get(newResourceType);
                warehouseDepot = player.getWarehouse().findDepotsByType(newResourceType);
                if (warehouseDepot.getDepotID() != newDepot){
                    oldQuantity = warehouseDepot.getQuantity();
                    oldDepot = warehouseDepot.getDepotID();
                    oldResourceType = warehouseDepot.getResourceType();
                    newQuantity = player.getWarehouse().getDepot(newDepot).get().getQuantity();
                    player.getWarehouse().getDepot(oldDepot).get().removeResource(oldQuantity);
                    player.getWarehouse().getDepot(newDepot).get().removeResource(newQuantity);
                    player.getWarehouse().getDepot(oldDepot).get().addResource(newResourceType,newQuantity);
                    player.getWarehouse().getDepot(newDepot).get().addResource(oldResourceType,oldQuantity);
                }
            }
            isSorted = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isSorted;
    }
}
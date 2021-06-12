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

            }
            isSorted = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isSorted;
    }
}

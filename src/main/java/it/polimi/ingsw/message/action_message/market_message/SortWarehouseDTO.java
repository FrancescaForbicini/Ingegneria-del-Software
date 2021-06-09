package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.client.action.SortWarehouse;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class SortWarehouseDTO extends ActionMessageDTO {
    private final Map<ResourceType,Integer> sortWarehouse;
    public SortWarehouseDTO(Map<ResourceType,Integer> sortWarehouse){
        this.sortWarehouse = sortWarehouse;
    }

    public Map<ResourceType, Integer> getSortWarehouse() {
        return sortWarehouse;
    }

    @Override
    public String getRelatedAction() {
        return SortWarehouse.class.getName();
    }
}

package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.warehouse.Warehouse;

public class SortWarehouseDTO extends TurnActionMessageDTO {
    private final Warehouse warehouse;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public SortWarehouseDTO(Warehouse warehouse) {
        this.warehouse = warehouse;
    }


}

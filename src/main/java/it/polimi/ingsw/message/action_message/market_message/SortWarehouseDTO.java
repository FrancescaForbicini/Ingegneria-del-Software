package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.model.warehouse.Warehouse;

public class SortWarehouseDTO{
    private final Warehouse warehouse;
    public Warehouse getWarehouse() {
        return warehouse;
    }

    public SortWarehouseDTO(Warehouse warehouse) {
        this.warehouse = warehouse;
    }


}

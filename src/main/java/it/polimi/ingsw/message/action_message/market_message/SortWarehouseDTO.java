package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.model.warehouse.Warehouse;

public class SortWarehouseDTO extends TakeFromMarketDTO{

    public Warehouse warehouse;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}

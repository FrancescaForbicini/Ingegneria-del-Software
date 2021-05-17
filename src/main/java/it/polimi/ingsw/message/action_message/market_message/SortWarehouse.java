package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.warehouse.Warehouse;

public class SortWarehouse extends TakeFromMarket{
    public Warehouse warehouse;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}

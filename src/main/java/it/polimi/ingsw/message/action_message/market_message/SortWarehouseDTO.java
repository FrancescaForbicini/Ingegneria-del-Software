package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.client.action.turn.SortWarehouse;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;

public class SortWarehouseDTO extends ActionMessageDTO {
    private final int depotID1;
    private final int depotID2;
    public SortWarehouseDTO(int depotID1, int depotID2){
        this.depotID1 = depotID1;
        this.depotID2 = depotID2;
    }

    public int getDepotID1() {
        return depotID1;
    }

    public int getDepotID2() {
        return depotID2;
    }

    @Override
    public String getRelatedAction() {
        return SortWarehouse.class.getName();
    }
}

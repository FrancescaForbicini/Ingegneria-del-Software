package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.Map;

public class SortWarehouse implements TurnAction {
    private final int depotID1;
    private final int depotID2;

    public SortWarehouse(int depotID1, int depotID2) {
        this.depotID1 = depotID1;
        this.depotID2 = depotID2;
    }

    @Override
    public void play(Player player) {
        player.getWarehouse().switchResource(depotID1,depotID2);
    }

}

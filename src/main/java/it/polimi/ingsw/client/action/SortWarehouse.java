package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SortWarehouse extends ClientAction {
    public SortWarehouse(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        ArrayList<Integer> depotIDs = new ArrayList<>();
        ArrayList<WarehouseDepot> depots = new ArrayList<>();
        int choice;
        WarehouseDepot firstDepot;
        WarehouseDepot secondDepot;
        int depotID1 = -1;
        int depotID2 = -1;
        //TODO useful?
        for(WarehouseDepot warehouseDepot : clientGameObserverProducer.getCurrentPlayer().getWarehouse().getAllDepots()){
            WarehouseDepot depotToAdd = new WarehouseDepot(warehouseDepot.getResourceType(), warehouseDepot.getLevel(), warehouseDepot.isAdditional(), warehouseDepot.getDepotID());
            depotToAdd.addResource(warehouseDepot.getResourceType(),warehouseDepot.getQuantity());
            depots.add(depotToAdd);
        }
        do {
            view.showMessage("\nChoose the first depot to switch: ");
            choice = view.choose(depots);
            firstDepot = depots.get(choice);
            view.showMessage("Choose the second depot to switch: ");
            choice = view.choose(depots);
            secondDepot = depots.get(choice);
            if (firstDepot.getQuantity() <= secondDepot.getLevel() && secondDepot.getQuantity() <= firstDepot.getLevel() &&
                    (!(firstDepot.isAdditional() || secondDepot.isAdditional()) ||
                            (firstDepot.getResourceType().equals(secondDepot.getResourceType())))) {
                //can be switched
                depotID1 = firstDepot.getDepotID();
                depotID2 = secondDepot.getDepotID();
            } else {
                view.showMessage("You cannot switch these two depots, please retry ");
            }
        }while (depotID1 > -1 && depotID2 > -1);
        clientConnector.sendMessage(new SortWarehouseDTO(depotID1, depotID2));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }

    @Override
    public boolean isDoable() {
        Warehouse warehouse = clientGameObserverProducer.getCurrentPlayer().getPersonalBoard().getWarehouse();
        boolean doableWithAddtionals = false;
        if(!(warehouse.getAdditionalDepots().size()==0)){
            for (WarehouseDepot additionalDepot : warehouse.getAdditionalDepots()){
                if(warehouse.getWarehouseDepots().stream().
                        anyMatch(warehouseDepot -> warehouseDepot.getResourceType().equals(additionalDepot.getResourceType()) &&
                                warehouseDepot.getQuantity() <= additionalDepot.getLevel() &&
                                    additionalDepot.getQuantity() <= warehouseDepot.getLevel())){
                    doableWithAddtionals = true;
                    break;
                }
            }
        }
        return !warehouse.isEmpty() && !warehouse.isFull() &&
                    (!warehouse.isFullNoAdditionals() || doableWithAddtionals);
    }
}

package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SortWarehouse extends ClientAction {
    public SortWarehouse(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        ArrayList<WarehouseDepot> depots;
        int choice;
        WarehouseDepot firstDepot;
        WarehouseDepot secondDepot;
        int depotID1 = -1;
        int depotID2 = -1;
        do {
            depots = new ArrayList<>();
            for(WarehouseDepot warehouseDepot : clientGameObserverProducer.getCurrentPlayer().getWarehouse().getAllDepots()){
                WarehouseDepot depotToAdd = new WarehouseDepot(warehouseDepot.getResourceType(), warehouseDepot.getLevel(), warehouseDepot.isAdditional(), warehouseDepot.getDepotID());
                depotToAdd.addResource(warehouseDepot.getResourceType(),warehouseDepot.getQuantity());
                depots.add(depotToAdd);
            }
            view.showMessage("\nChoose the first depot to switch: ");
            choice = view.chooseDepot(depots, clientGameObserverProducer.getCurrentPlayer().getWarehouse());
            firstDepot = depots.get(choice);
            cleanDepots(depots, firstDepot);
            view.showMessage("Choose the second depot to switch: ");
            choice = view.chooseDepot(depots, clientGameObserverProducer.getCurrentPlayer().getWarehouse());
            secondDepot = depots.get(choice);
            if ((canSwitchQuantityDepot(firstDepot, secondDepot) && canSwitchAdditionalDepot(firstDepot, secondDepot))) {
                //can be switched
                depotID1 = firstDepot.getDepotID();
                depotID2 = secondDepot.getDepotID();
            } else {
                view.showMessage("You cannot switch these two depots, please retry ");
            }
        }while (depotID1 == -1 && depotID2 == -1);
        clientConnector.sendMessage(new SortWarehouseDTO(depotID1, depotID2));
    }

    public static  boolean canSwitchQuantityDepot(WarehouseDepot depot1, WarehouseDepot depot2){
        return depot1.getQuantity() <= depot2.getLevel() && depot2.getQuantity() <= depot1.getLevel();
    }
    public static boolean canSwitchAdditionalDepot(WarehouseDepot depot1, WarehouseDepot depot2){
        return (depot1.isAdditional() || depot2.isAdditional()) || !depot1.getResourceType().equals(depot2.getResourceType());
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
    }

    @Override
    public boolean isDoable() {
        Warehouse warehouse = clientGameObserverProducer.getCurrentPlayer().getPersonalBoard().getWarehouse();
        boolean doableWithAddtionals = false;
        if(!(warehouse.getAdditionalDepots().size()==0)){
            for (WarehouseDepot additionalDepot : warehouse.getAdditionalDepots()){
                if(warehouse.getWarehouseDepots().stream().
                        anyMatch(warehouseDepot -> warehouseDepot.getResourceType().equals(additionalDepot.getResourceType()) &&
                                canSwitchQuantityDepot(additionalDepot, warehouseDepot))){
                    doableWithAddtionals = true;
                    break;
                }
            }
        }
        return !warehouse.isEmpty() && !warehouse.isFull() &&
                    (!warehouse.isFullNoAdditional() || doableWithAddtionals);
    }

    private void cleanDepots(ArrayList<WarehouseDepot> depots, WarehouseDepot firstDepot){
        depots.removeIf(depot -> depot.getDepotID()!=firstDepot.getDepotID() && (!canSwitchQuantityDepot(depot,firstDepot) || !canSwitchAdditionalDepot(depot,firstDepot)));
    }
}

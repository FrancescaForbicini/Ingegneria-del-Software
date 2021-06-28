package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Sorts the warehouse
 */
public class SortWarehouse extends ClientAction {
    public SortWarehouse(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * Do not removes this actions from the actions available,
     * because the player can make this action more than once in a turn
     *
     * @param from the actions available
     */
    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }

    /**
     * Checks if the player can sort the warehouse
     * @return iff the warehouse is not empty or full
     */
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

    /**
     * Sorts the warehouse
     */
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
            choice = view.chooseDepot(depots);
            firstDepot = depots.get(choice);
            updateDepots(depots, firstDepot);
            if (depots.size() == 0) {
                view.showMessage("You can't move this depot");
                depotID1 = -1;
                depotID2 = -1;
            }
            else {
                view.showMessage("Choose the second depot to switch: ");
                choice = view.chooseDepot(depots);
                secondDepot = depots.get(choice);
                if ((canSwitchQuantityDepot(firstDepot, secondDepot) && canSwitchAdditionalDepot(firstDepot, secondDepot))) {
                    //can be switched
                    depotID1 = firstDepot.getDepotID();
                    depotID2 = secondDepot.getDepotID();
                } else {
                    view.showMessage("You cannot switch these two depots, please retry ");
                }
            }
        }while (depotID1 == -1 && depotID2 == -1);
        clientConnector.sendMessage(new SortWarehouseDTO(depotID1, depotID2));
    }

    /**
     * Checks if two resources can be switched from depot1 to depot2
     *
     * @param depot1 actual depot of the resource that has to be moved
     * @param depot2 new depot for the resource that has to be moved
     * @return true iff the resource can be moved
     */
    public static  boolean canSwitchQuantityDepot(WarehouseDepot depot1, WarehouseDepot depot2){
        return depot1.getQuantity() <= depot2.getLevel() && depot2.getQuantity() <= depot1.getLevel();
    }

    /**
     * Checks if two resources can be switched from depot1 to depot2, even if one of these is additional
     *
     * @param depot1 actual depot of the resource that has to be moved
     * @param depot2 new depot for the resource that has to be moved
     * @return true iff the resource can be moved
     */
    public static boolean canSwitchAdditionalDepot(WarehouseDepot depot1, WarehouseDepot depot2){
        if (depot1.isAdditional() && depot2.isAdditional())
            return depot1.getResourceType().equals(depot2.getResourceType());
        if ((!depot1.isAdditional() && depot2.isAdditional()) || (depot1.isAdditional() && !depot2.isAdditional()))
            return depot1.getResourceType().equals(depot2.getResourceType());
        return true;
    }


    /**
     * Updates depots available
     * @param depots all the depots available
     * @param firstDepot the depot where the resources has been moved to another depot
     */
    private void updateDepots(ArrayList<WarehouseDepot> depots, WarehouseDepot firstDepot){
        depots.removeIf(depot -> depot.getDepotID() == firstDepot.getDepotID() || (depot.getDepotID()!=firstDepot.getDepotID() && (!canSwitchQuantityDepot(depot,firstDepot) || !canSwitchAdditionalDepot(depot,firstDepot))));
    }
}

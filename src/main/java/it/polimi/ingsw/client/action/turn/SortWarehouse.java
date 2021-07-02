package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Sorts the warehouse
 */
public class SortWarehouse extends ClientAction {
    public SortWarehouse(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
     * Checks if the player can sort the warehouse
     *
     * @return iff the warehouse is not empty or full
     */
    @Override
    public boolean isDoable() {
        Warehouse warehouse = clientGameObserverProducer.getCurrentPlayer().getPersonalBoard().getWarehouse();
        boolean doableWithAdditional = false;
        if(warehouse.getAdditionalDepots().size()!=0){
            for (WarehouseDepot additionalDepot : warehouse.getAdditionalDepots()){
                if(warehouse.getWarehouseDepots().stream().
                        anyMatch(warehouseDepot -> canSwitchDepots(warehouse.getWarehouseDepots(), warehouseDepot,additionalDepot) ||
                                canSwitchDepots(warehouse.getWarehouseDepots(), additionalDepot,warehouseDepot))){
                    doableWithAdditional = true;
                    break;
                }
            }
        }
        return !warehouse.isEmpty() && !warehouse.isFull() &&
                (!warehouse.isFullNoAdditional() || doableWithAdditional);
    }

    /**
     * {@inheritDoc}
     *
     * Sorts the warehouse
     */
    @Override
    public void doAction() {
        ArrayList<WarehouseDepot> depots;
        int choice;
        int depotID1 = -1;
        int depotID2 = -1;
        WarehouseDepot firstDepot;
        WarehouseDepot secondDepot;
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
                if (canSwitchDepots(depots, firstDepot,secondDepot)) {
                    depotID1 = firstDepot.getDepotID();
                    depotID2 = secondDepot.getDepotID();
                }
                else{
                    view.showMessage("You cannot switch these two depots, please retry ");
                }
            }
        }while (depotID1 == -1 && depotID2 == -1);
        clientConnector.sendMessage(new SortWarehouseDTO(depotID1, depotID2));
    }

    /**
     * Checks if it is possible to switch resources between firstDepot and secondDepot accordingly to depots status
     * If one of them is additional all possible resources are moved from firstDepot to secondDepot
     *
     * @param depots represents the actual status of the player's depots
     * @param firstDepot depot where the resources are taken if one of the two is additional
     * @param secondDepot depot where the resources are put if one of the two is additional
     * @return true iff it is possible to move resources between the two depots
     */
    private boolean canSwitchDepots(ArrayList<WarehouseDepot> depots, WarehouseDepot firstDepot, WarehouseDepot secondDepot){
        return canSwitchByQuantity(firstDepot,secondDepot) && canSwitchByResource(depots, firstDepot,secondDepot);
    }

    /**
     * Checks if two resources can be switched from depot1 to depot2
     *
     * @param depot1 actual depot of the resource that has to be moved
     * @param depot2 new depot for the resource that has to be moved
     * @return true iff the resource can be moved
     */
    private static boolean canSwitchByQuantity(WarehouseDepot depot1, WarehouseDepot depot2){
        if (!depot1.isAdditional() && !depot2.isAdditional())
            return depot1.getQuantity() <= depot2.getLevel() && depot2.getQuantity() <= depot1.getLevel();
        return !depot1.isEmpty() && !depot2.isFull();
    }

    /**
     * Checks if two resources can be switched from depot1 to depot2, even if one of these is additional
     *
     * @param depot1 actual depot of the resource that has to be moved
     * @param depot2 new depot for the resource that has to be moved
     * @return true iff the resource can be moved
     */
    private static boolean canSwitchByResource(ArrayList<WarehouseDepot> depots, WarehouseDepot depot1, WarehouseDepot depot2){
        if (depot1.isAdditional() && depot2.isAdditional())
            return depot1.getResourceType().equals(depot2.getResourceType());
        if ((!depot1.isAdditional() && depot2.isAdditional()))
            return depot1.getResourceType().equals(depot2.getResourceType());
        if ((depot1.isAdditional() && !depot2.isAdditional()))
            return (depot1.getResourceType().equals(depot2.getResourceType()) || depot2.isEmpty()) &&
                    depots.stream().anyMatch(depot -> depot.getDepotID()!=depot2.getDepotID() && depot.getResourceType().equals(depot1.getResourceType()));
        return true;
    }


    /**
     * Updates depots available
     *
     * @param depots all the depots available
     * @param firstDepot the depot where the resources has been moved to another depot
     */
    private void updateDepots(ArrayList<WarehouseDepot> depots, WarehouseDepot firstDepot){
        depots.removeIf(depot -> depot.getDepotID() == firstDepot.getDepotID() || (depot.getDepotID()!=firstDepot.getDepotID() && (!canSwitchByQuantity(firstDepot,depot) || !canSwitchByResource(depots, firstDepot,depot))));
    }
}

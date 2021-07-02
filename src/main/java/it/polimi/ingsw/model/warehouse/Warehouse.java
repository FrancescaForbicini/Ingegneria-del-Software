package it.polimi.ingsw.model.warehouse;


import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Math.min;

/**
 * Represents the Player's storage for Resources obtained from the Market
 * Into warehouseDepots different depots with same type cannot coexist
 */
public class Warehouse {
    private final LinkedHashSet<WarehouseDepot> warehouseDepots;
    private final ArrayList<WarehouseDepot> additionalDepots;
    private int lastDepotID = 0;

    public Warehouse(){
        this.warehouseDepots = new LinkedHashSet<>();
        for(int i=1;i<4;i++){
            warehouseDepots.add(new WarehouseDepot(ResourceType.Any,i,false,i));
        };
        lastDepotID = 4;
        this.additionalDepots = new ArrayList<>();
    }

    /**
     * Adds a depot to the additional ones
     *
     * @param level of the additional depot
     * @param resourceType of the additional depot
     */
    public void addAdditionalDepot(ResourceType resourceType, int level){
        additionalDepots.add(new WarehouseDepot(resourceType,level,true,lastDepotID));
        lastDepotID++;
    }


    /**
     * Checks if it is possible to add a new depot obtained by a leader card
     *
     * @param depot depot to add
     * @param type type of resource contained in the depot
     * @return true iff the depot can be added
     */
    private static boolean canAddOnAdditional(WarehouseDepot depot, ResourceType type) {
        return depot.isAdditional() && depot.getResourceType().equals(type);
    }

    /**
     * Checks if it is possible to add a new depot
     *
     * @param depot depot to add
     * @param other depot already present with the same resource
     * @param type resource contained in the depot
     * @return true iff the depot can be added
     */
    private static boolean canAddOnNonAdditional(WarehouseDepot depot, Optional<WarehouseDepot> other, ResourceType type) {
        return other.map(warehouseDepot -> !depot.isAdditional() && warehouseDepot.getDepotID() == depot.getDepotID()).orElseGet(() -> !depot.isAdditional());

    }

    /**
     * Total amount of all the resources
     *
     * @return total amount of all resources
     */
    public int getTotalQuantity(){
        int totalAmount = 0;
        for (WarehouseDepot warehouseDepot : getAllDepots()) {
            totalAmount += warehouseDepot.getQuantity();
        }
        return totalAmount;
    }

    /**
     * Adds the given quantity of resource to the depot passed
     *
     * @param type type of resource needed to add, can respect the rules of single depots and warehouse
     * @param quantity how much quantity needs to be added
     * @param depotID where adding the resource to
     * @return true if it has been possible to add the given amount of resource to the given depot
     */
    public boolean addResource(ResourceType type, int quantity, int depotID){
        if(getDepot(depotID).isPresent()) {
            WarehouseDepot depot = getDepot(depotID).get();
            if (canAddOnAdditional(depot, type) || canAddOnNonAdditional(depot, Optional.ofNullable(findDepotsByType(type)), type)) {
                return depot.addResource(type, quantity);
            }
        }
        return false;
    }

    /**
     * Removes the given quantity of resource from the given depot
     *
     * @param quantity how much quantity needs to be removed
     * @param depotID where removing the resource from
     * @return true if it has been possible to remove the given amount of resource from the given depot
     */
    public boolean removeResource(int quantity, int depotID){
        if(getDepot(depotID).isPresent()){
            WarehouseDepot depot = getDepot(depotID).get();
            return depot.removeResource(quantity);
        }
        return false;
    }

    public  ArrayList<WarehouseDepot> getWarehouseDepots(){
        return new ArrayList<>(warehouseDepots);
    }

    public ArrayList<WarehouseDepot> getAdditionalDepots(){
        return additionalDepots;
    }

    /**
     * Gets all depots present in the warehouse
     *
     * @return list of depots
     */
    public ArrayList<WarehouseDepot> getAllDepots(){
        ArrayList<WarehouseDepot> allDepots = new ArrayList<>(warehouseDepots);
        allDepots.addAll(additionalDepots);
        return allDepots;
    }

    /**
     * Gets the possible depot to move the resources
     *
     * @param resourceToMove resource that has to be moved
     * @param adding true if the resource has to be add, false if it has to be removed
     * @return lists of depots where the resource can be added or removed
     */
    public ArrayList<WarehouseDepot> getPossibleDepotsToMoveResources(ResourceType resourceToMove, boolean adding){
        ArrayList<WarehouseDepot> possibleDepots = new ArrayList<>();
        possibleDepots.addAll(warehouseDepots);
        possibleDepots.addAll(additionalDepots);
        possibleDepots.removeIf(depot -> !depot.isPossibleToMoveResource(resourceToMove, 1, adding));
        ArrayList<WarehouseDepot> warehouseDepotsToRemove = new ArrayList<>();
        if(adding) {
            for (WarehouseDepot warehouseDepot: possibleDepots){
                if (warehouseDepot.isAdditional() && !warehouseDepot.getResourceType().equals(resourceToMove))
                    warehouseDepotsToRemove.add(warehouseDepot);
                else
                    if (!warehouseDepot.isAdditional() &&
                            warehouseDepots.stream().anyMatch(depot -> depot.getDepotID() != warehouseDepot.getDepotID() &&
                                    depot.getResourceType().equals(resourceToMove)))
                        warehouseDepotsToRemove.add(warehouseDepot);
            }
        }
        possibleDepots.removeAll(warehouseDepotsToRemove);
        return possibleDepots;
    }

    /**
     * Checks if the resource can be moved
     *
     * @param resourceToMove resource to move
     * @param quantityToMove quantity of the resource to move
     * @param depotID depot where there is the resource
     * @param adding true if the resource has to be adding,false if it has to be remove
     * @return true iff the resource can be added or removed
     */
    public boolean canMoveResource(ResourceType resourceToMove, int quantityToMove, int depotID, boolean adding){
        if(getDepot(depotID).isPresent()) {
            return getDepot(depotID).get().isPossibleToMoveResource(resourceToMove, quantityToMove, adding);
        }
        return false;
    }

    /**
     * Checks if it is possible to move some quantity of a resource regardless of the placement
     *
     * @param resourceToMove to be moved
     * @param quantityToMove amount to be moved
     * @param adding true iff adding
     * @return true iff it is possible to move the given quantity of resource in the requested way
     */
    public boolean canMoveResource(ResourceType resourceToMove, int quantityToMove, boolean adding){
        return getAllDepots().stream().anyMatch(depot -> depot.isPossibleToMoveResource(resourceToMove, quantityToMove, adding));
    }

    /**
     * Switches resources between two given depots
     *
     * @param fromDepotID first depotID
     * @param toDepotID second depotID
     * @return true if it has been possible to switch resources according to the rules
     */
    public boolean switchResource(int fromDepotID,int toDepotID){
        WarehouseDepot d1 = getDepot(fromDepotID).get();
        WarehouseDepot d2 = getDepot(toDepotID).get();
        ResourceType t1 = d1.getResourceType();
        ResourceType t2 = d2.getResourceType();
        int q1 = d1.getQuantity();
        int q2 = d2.getQuantity();
        if (!d1.isAdditional() && !d2.isAdditional())
            return d1.removeResource(q1) && d1.addResource(t2, q2) && d2.removeResource(q2) && d2.addResource(t1, q1);
        int quantityToMove = min(q1,d2.getAvailableSpace());
        if (!d1.isAdditional() && d2.isAdditional())
            return t1.equals(t2) &&  d1.removeResource(quantityToMove) && d2.addResource(t2,quantityToMove);
        if (d1.isAdditional() && !d2.isAdditional())
            return d1.removeResource(quantityToMove) && d2.addResource(t1,quantityToMove) && (t1.equals(t2) || d2.isEmpty());
        return false;
    }

    /**
     * Checks if the resources can be switched or moved from first depot to second depot
     *
     * @param fromDepotID where to take resources when moving instead of switching
     * @param toDepotID where to put resources when moving instead of switching
     * @return
     */
    public boolean canSwitchDepots(int fromDepotID, int toDepotID){
        boolean canSwitchByQuantity = false;
        WarehouseDepot firstDepot = this.getDepot(fromDepotID).get();
        WarehouseDepot secondDepot = this.getDepot(toDepotID).get();
        if (!firstDepot.isAdditional() && !secondDepot.isAdditional())
            canSwitchByQuantity = firstDepot.getQuantity() <= secondDepot.getLevel() && secondDepot.getQuantity() <= firstDepot.getLevel();
        else
            canSwitchByQuantity = !firstDepot.isEmpty() && !secondDepot.isFull();
        if (firstDepot.isAdditional() && secondDepot.isAdditional())
            return canSwitchByQuantity && firstDepot.getResourceType().equals(secondDepot.getResourceType());
        else {
            if ((!firstDepot.isAdditional() && secondDepot.isAdditional()))
                return canSwitchByQuantity && firstDepot.getResourceType().equals(secondDepot.getResourceType());
            if (firstDepot.isAdditional() && !secondDepot.isAdditional())
                return canSwitchByQuantity && (firstDepot.getResourceType().equals(secondDepot.getResourceType()) || secondDepot.isEmpty());
        }
        return canSwitchByQuantity;
    }

    /**
     * Finds a list of depots, from a given list, which has the same resource type as the given one
     *
     * @param type type needed to be matched
     * @return list of all matching depots found
     */
    public WarehouseDepot findDepotsByType(ResourceType type){
        for (WarehouseDepot warehouseDepot : warehouseDepots) {
            if (warehouseDepot.getResourceType().equals(type)) {
                return warehouseDepot;
            }
        }
        return null;
    }


    /**
     * Gives the amount of given resource which is in the warehouse
     *
     * @param type resource asked for
     * @return total amount of given resource
     */
    public int getQuantity(ResourceType type){
        WarehouseDepot depot = findDepotsByType(type);
        int nonAdditional = 0;
        int additional = 0;
        for (WarehouseDepot warehouseDepot: getAdditionalDepots()){
            if (warehouseDepot.getResourceType().equals(type)){
                additional += warehouseDepot.getQuantity();
            }
        }
        if (depot != null)
            nonAdditional = depot.getQuantity();
        return additional + nonAdditional;
    }

    /**
     * Gives a depot from the warehouse which matches the requested level
     *
     * @param depotID ID requested
     * @return the matching depot, in case the level is over the boundaries the closest depot is returned,
     * null if there is no depot of given level between 1 and 3 (it should be never returned)
     */
    public Optional<WarehouseDepot> getDepot(int depotID){
        return Stream.concat(warehouseDepots.stream(), additionalDepots.stream())
                .filter(depot -> depot.getDepotID()==depotID).findFirst();
    }

    /**
     * Checks if warehouse is empty
     *
     * @return true iff all the depots are empty
     */
    public boolean isEmpty(){
        return getAllDepots().stream().allMatch(WarehouseDepot::isEmpty);
    }

    /**
     * Checks if warehouse is full
     *
     * @return true iff all depots are full
     */
    public boolean isFull(){
        return getAllDepots().stream().allMatch(WarehouseDepot::isFull);
    }

    /**
     * Checks if warehouse (additional depots excluded) is full
     *
     * @return true iff all and only the default depots are full
     */
    public boolean isFullNoAdditional(){
        return getWarehouseDepots().stream().allMatch(WarehouseDepot::isFull);
    }

    @Override
    public String toString(){
        StringBuilder print = new StringBuilder();
        print.append("WAREHOUSE").append("\n");
        for (int i = 0; i < this.getAllDepots().size(); i++){
            print.append(getAllDepots().get(i).toString());
        }
        return print.toString();
    }

    /**
     * Gets total amount of resources
     *
     * @return total amount of all resources
     */
    public int getResourceTotal() {
        return Stream.concat(warehouseDepots.stream(), additionalDepots.stream())
                .mapToInt(WarehouseDepot::getQuantity).sum();
    }
}

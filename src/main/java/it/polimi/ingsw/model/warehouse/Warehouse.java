package it.polimi.ingsw.model.warehouse;


import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the Player's storage for Resources obtained from the Market
 * Into warehouseDepots different depots with same type cannot coexist
 */
public class Warehouse {
    private final LinkedHashSet<WarehouseDepot> warehouseDepots;
    private ArrayList<WarehouseDepot> additionalDepots;
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
     * @param level of the additional depot
     * @param resourceType of the additional depot
     */
    public void addAdditionalDepot(ResourceType resourceType, int level){
        additionalDepots.add(new WarehouseDepot(resourceType,level,true,lastDepotID));
        lastDepotID++;
    }


    private static boolean canAddOnAdditional(WarehouseDepot depot, ResourceType type) {
        return depot.isAdditional() && depot.getResourceType().equals(type);
    }

    private static boolean canAddOnNonAdditional(WarehouseDepot depot, Optional<WarehouseDepot> other, ResourceType type) {
        if (other.isPresent()) {
            return !depot.isAdditional() && other.get().getDepotID() == depot.getDepotID();
        } else {
            return !depot.isAdditional();
        }

    }

    /**
     * Adds the given quantity of resource to the depot passed
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

    public ArrayList<WarehouseDepot> getAllDepots(){
        ArrayList<WarehouseDepot> allDepots = new ArrayList<>(warehouseDepots);
        allDepots.addAll(additionalDepots);
        return allDepots;
    }

    public ArrayList<WarehouseDepot> getPossibleDepotsToMoveResources(ResourceType resourceToMove, boolean adding){
        ArrayList<WarehouseDepot> possibleDepots = new ArrayList<>();
        possibleDepots.addAll(warehouseDepots);
        possibleDepots.addAll(additionalDepots);
        possibleDepots.removeIf(depot -> !depot.isPossibleToMoveResource(resourceToMove, adding));
        return possibleDepots;
    }

    /**
     * Switches resources between two given depots
     * @param depotID1 first depotID
     * @param depotID2 second depotID
     * @return true if it has been possible to switch resources according to the rules
     */
    public boolean switchResource(int depotID1,int depotID2){
        if(getDepot(depotID1).isPresent() && getDepot(depotID2).isPresent()) {
            WarehouseDepot d1 = getDepot(depotID1).get();
            WarehouseDepot d2 = getDepot(depotID2).get();
            int q1 = d1.getQuantity();
            int q2 = d2.getQuantity();
            if (q1 > d2.getLevel() || q2 > d1.getLevel() || (d1.isAdditional() && d2.isAdditional())) {
                return false;
            } else {
                ResourceType t1 = d1.getResourceType();
                ResourceType t2 = d2.getResourceType();
                if (!t1.equals(t2) && (d1.isAdditional() || d2.isAdditional())) {
                    return false;
                } else {
                    return d1.removeResource(q1) && d1.addResource(t2, q2) && d2.removeResource(q2) && d2.addResource(t1, q1);
                }
            }
        }
        return false;
    }

    /**
     * Finds a list of depots, from a given list, which has the same resource type as the given one
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
     * @param type resource asked for
     * @return total amount of given resource
     */
    public int getQuantity(ResourceType type){
        WarehouseDepot depot = findDepotsByType(type);//TODO review method
        int nonAdditional = 0;
        int additional = additionalDepots.stream()
                .filter(d -> d.getResourceType().equals(type))
                .mapToInt(WarehouseDepot::getQuantity).sum();
        if (depot != null)
            nonAdditional = depot.getQuantity();
        return additional + nonAdditional;
    }

    /**
     * Gives a depot from the warehouse which matches the requested level
     * @param depotID ID requested
     * @return the matching depot, in case the level is over the boundaries the closest depot is returned,
     * null if there is no depot of given level between 1 and 3 (it should be never returned)
     */
    public Optional<WarehouseDepot> getDepot(int depotID){
        return Stream.concat(warehouseDepots.stream(), additionalDepots.stream())
                .filter(depot -> depot.getDepotID()==depotID).findFirst();
    }

    public boolean isEmpty(){
        return getAllDepots().stream().allMatch(WarehouseDepot::isEmpty);
    }

    public boolean isEmptyNoAdditional(){
        return getAdditionalDepots().stream().allMatch(WarehouseDepot::isEmpty);
    }

    public boolean isFull(){
        return getAllDepots().stream().allMatch(WarehouseDepot::isFull);
    }

    public boolean isFullNoAdditional(){
        return getWarehouseDepots().stream().allMatch(WarehouseDepot::isFull);
    }
    @Override
    public String toString(){
        StringBuilder print = new StringBuilder();
        print.append("WAREHOUSE").append("\n");
        for (int i = 0; i < this.getWarehouseDepots().size(); i++){
                print.append(getWarehouseDepots().get(i).toString());
        }
        return print.toString();
    }

    public int getResourceTotal() {
        return Stream.concat(warehouseDepots.stream(), additionalDepots.stream())
                .mapToInt(WarehouseDepot::getQuantity).sum();
    }
}

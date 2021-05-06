package it.polimi.ingsw.model.warehouse;


import it.polimi.ingsw.model.cards.AdditionalDepot;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;

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
        for(int i=0;i<3;i++){
            lastDepotID++;
            warehouseDepots.add(new WarehouseDepot(ResourceType.Any,i+1,false,lastDepotID));
        }
        this.additionalDepots = new ArrayList<>();
    }

    /**
     * Adds a depot to the additional ones
     * @param level of the additional depot
     * @param resourceType of the additional depot
     */
    public void addAdditionalDepot(ResourceType resourceType, int level){
        lastDepotID++;
        additionalDepots.add(new WarehouseDepot(resourceType,level,true,lastDepotID));
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
            ArrayList<WarehouseDepot> sameTypeDepots = findDepotsByType(new ArrayList<>(warehouseDepots), type);
            if (depot.isAdditional() || (!depot.isAdditional() && (sameTypeDepots.isEmpty() || sameTypeDepots.stream().allMatch(warehouseDepot -> warehouseDepot.equals(depot))))) {
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
     * @param list where to search the depots
     * @param type type needed to be matched
     * @return list of all matching depots found
     */
    private ArrayList<WarehouseDepot> findDepotsByType(ArrayList<WarehouseDepot> list, ResourceType type){
        ArrayList<WarehouseDepot> depots = new ArrayList<>();
        for (WarehouseDepot warehouseDepot : list) {
            if (warehouseDepot.getResourceType().equals(type)) {
                depots.add(warehouseDepot);
            }
        }
        return depots;
    }


    /**
     * Gives the amount of given resource which is in the warehouse
     * @param type resource asked for
     * @return total amount of given resource
     */
    public int getQuantity(ResourceType type){
        int tot = 0;
        ArrayList<WarehouseDepot> depots = findDepotsByType(new ArrayList<>(warehouseDepots),type);
        depots.addAll(findDepotsByType(additionalDepots,type));
        for(WarehouseDepot warehouseDepot : depots){
            tot += warehouseDepot.getQuantity();
        }
        return tot;
    }

    /**
     * Gives a depot from the warehouse which matches the requested level
     * @param depotID ID requested
     * @return the matching depot, in case the level is over the boundaries the closest depot is returned,
     * null if there is no depot of given level between 1 and 3 (it should be never returned)
     */
    public Optional<WarehouseDepot> getDepot(int depotID){
        ArrayList<WarehouseDepot> allDepots = new ArrayList<>();
        allDepots.addAll(warehouseDepots);
        allDepots.addAll(additionalDepots);
        return allDepots.stream().filter(depot -> depot.getDepotID()==depotID).findFirst();
    }
}
